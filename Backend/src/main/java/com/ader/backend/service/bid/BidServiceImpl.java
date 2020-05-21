package com.ader.backend.service.bid;

import com.ader.backend.entity.*;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.BidRepository;
import com.ader.backend.rest.dto.BidDto;
import com.ader.backend.service.offer.OfferService;
import com.ader.backend.service.persona.PersonaService;
import com.ader.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final OfferService offerService;
    private final UserService userService;
    private final PersonaService personaService;

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
                bidUser = userService.getUser(managedBid.getUser().getId());
                bidOffer.setAssignee(bidUser);

                if (offerId.get() == null) offerId.set(bidOffer.getId());

                if (!managedBid.getAcceptInitialRequirements()) {
                    bidOffer.setCompensation(managedBid.getCompensation());
                    bidOffer.setFreeProductSample(managedBid.getFreeProductSample());
                }
            }

            bidRepository.delete(managedBid);
        });

        offerService.updateOfferStatus(offerId.get(), OfferStatus.ASSIGNED);
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
