package com.ader.backend.service.bid;

import com.ader.backend.entity.Bid;
import com.ader.backend.entity.BidStatus;
import com.ader.backend.entity.File;
import com.ader.backend.entity.Offer;
import com.ader.backend.entity.OfferStatus;
import com.ader.backend.entity.Persona;
import com.ader.backend.entity.User;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.BidRepository;
import com.ader.backend.rest.dto.BidDto;
import com.ader.backend.service.file.FileService;
import com.ader.backend.service.offer.OfferService;
import com.ader.backend.service.persona.PersonaService;
import com.ader.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

  private final BidRepository bidRepository;
  private OfferService offerService;
  private UserService userService;
  private final PersonaService personaService;
  private FileService fileService;

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setOfferService(OfferService offerService) {
    this.offerService = offerService;
  }

  @Autowired
  public void setFileService(FileService fileService) {
    this.fileService = fileService;
  }

  @Override
  public List<Bid> getBids() {
    return bidRepository.findAll();
  }

  @Override
  public List<Bid> getBidsByUser(String userEmail) {
    return bidRepository.findAllByUserEmail(userEmail);
  }

  @Override
  public List<Bid> getBidsByOffer(Long offerId) {
    return bidRepository.findAllByOfferId(offerId);
  }

  @Override
  public Bid getBidByUserEmailAndOfferId(String userEmail, Long offerId) {
    return bidRepository.findByUser_EmailAndOffer_Id(userEmail, offerId);
  }

  @Override
  public Bid createBid(BidDto bidDto) {
    String errorMessage;

    Bid bid = new Bid();
    Offer bidOffer = offerService.getOffer(bidDto.getOfferId());

    // Compress the files again to avoid their corruption on bid save
    List<File> files = new ArrayList<>(bidOffer.getFiles());
    bidOffer.getFiles().clear();
    bidOffer.getFiles().addAll(fileService.compressFile(files));

    User bidUser = userService.getAuthenticatedUser();
    Boolean initialRequirementsAccepted = bidDto.getAcceptInitialRequirements();

    Persona bidPersona = new Persona();
    bidPersona.setUser(bidUser);
    bidPersona.setActivity(bidDto.getPersona().getActivity());
    bidPersona.setAudience(bidDto.getPersona().getAudience());
    bidPersona.setSellingOrientation(bidDto.getPersona().getSellingOrientation());
    bidPersona.setValues(bidDto.getPersona().getValues());

    if (initialRequirementsAccepted) {
      bid.setCompensation(bidOffer.getCompensation());
      bid.setFreeProductSample(bidOffer.getFreeProductSample());
    } else {
      bid.setFreeProductSample(bidDto.getFreeProductSample());
      bid.setCompensation(bidDto.getCompensation());
    }

    bid.setAcceptInitialRequirements(initialRequirementsAccepted);
    bid.setOffer(bidOffer);
    bid.setPersona(bidPersona);
    bid.setUser(bidUser);

    try {
      personaService.createPersona(bidPersona);
      bidRepository.save(bid);
    } catch (Exception e) {
      errorMessage = e.getMessage();
      log.error(errorMessage);
      throw new ResponseStatusException(
              HttpStatus.UNPROCESSABLE_ENTITY,
              errorMessage
      );
    }

    log.info("Successfully created bid: [{}]", bid);
    return bid;
  }

  @Override
  public void acceptBids(List<Bid> bids) {
    final AtomicReference<Long> offerId = new AtomicReference<>();

    bids.forEach(bid -> {
      Bid managedBid = bidRepository.findById(bid.getId()).orElse(null);
      Offer bidOffer;
      User bidUser;

      if (managedBid == null) {
        String errorMessage = "No bid found for id: [" + bid.getId() + "]";
        log.error(errorMessage);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
      } else {
        bidOffer = offerService.getOffer(managedBid.getOffer().getId());

        // Compress the files again to avoid their corruption on bid update
        List<File> files = new ArrayList<>(bidOffer.getFiles());
        bidOffer.getFiles().clear();
        bidOffer.getFiles().addAll(fileService.compressFile(files));

        bidUser = userService.getUser(managedBid.getUser().getId());
        bidOffer.getAssignees().add(bidUser);
        managedBid.setBidStatus(BidStatus.ACCEPTED);

        if (offerId.get() == null) offerId.set(bidOffer.getId());
      }
    });

    offerService.updateOfferStatus(offerId.get(), OfferStatus.ASSIGNED.name());
  }

  @Override
  public Bid updateBid(Long id, Bid bid) {
    String errorMessage;
    Bid bidToUpdate = bidRepository.findById(id).orElse(null);

    if (bidToUpdate == null) {
      errorMessage = "Bid with id: [" + id + "] does not exist!";
      log.error(errorMessage);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    } else {
      try {
        BeanUtils.copyProperties(
                bid,
                bidToUpdate,
                BeanHelper.getNullPropertyNames(bid, true)
        );
      } catch (Exception e) {
        errorMessage = e.getMessage();
        log.error(errorMessage);
        throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                errorMessage
        );
      }
    }

    log.info("Successfully updated bid. New bid: [{}]", bidToUpdate);
    return bidToUpdate;
  }

  @Override
  public void updateStatus(Long id, String newStatus) {
    Bid bid = bidRepository.findById(id).orElseThrow();
    Offer offer = bid.getOffer();

    for (BidStatus status : BidStatus.getValues()) {
      if (status.getName().equals(newStatus)) bid.setBidStatus(status);
    }

    if (OfferStatus.IN_PROGRESS.getName().equals(offer.getOfferStatus().getName())) {
      List<Bid> offerBids = bidRepository.findAllByOfferId(offer.getId());
      if (offerBids.stream().allMatch(bidToCheck -> bidToCheck.getBidStatus().equals(BidStatus.APPROVED))) {
        offer.setOfferStatus(OfferStatus.COMPLETED);
      }
    }
  }

  @Override
  public String deleteBid(Long id) {
    String errorMessage;

    try {
      bidRepository.deleteById(id);
    } catch (Exception e) {
      errorMessage = e.getMessage();
      log.error(errorMessage);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    String successMessage = "Successfully deleted bid with id: [" + id + "]";
    log.info(successMessage);
    return successMessage;
  }
}
