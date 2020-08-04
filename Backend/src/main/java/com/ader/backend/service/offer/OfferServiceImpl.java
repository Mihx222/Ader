package com.ader.backend.service.offer;

import com.ader.backend.entity.AdvertisementFormat;
import com.ader.backend.entity.Bid;
import com.ader.backend.entity.BidStatus;
import com.ader.backend.entity.Category;
import com.ader.backend.entity.File;
import com.ader.backend.entity.Offer;
import com.ader.backend.entity.OfferStatus;
import com.ader.backend.entity.Status;
import com.ader.backend.entity.User;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.OfferRepository;
import com.ader.backend.service.advertisementformat.AdvertisementFormatService;
import com.ader.backend.service.bid.BidService;
import com.ader.backend.service.category.CategoryService;
import com.ader.backend.service.file.FileService;
import com.ader.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

  private static final int ESP32_PORT = 8090;
  private static final String ESP32_IP = "192.168.0.15";

  private final OfferRepository offerRepository;
  private final UserService userService;
  private final CategoryService categoryService;
  private final AdvertisementFormatService advertisementFormatService;
  private FileService fileService;
  private BidService bidService;

  @Autowired
  public void setBidService(BidService bidService) {
    this.bidService = bidService;
  }

  @Autowired
  public void setFileService(FileService fileService) {
    this.fileService = fileService;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Offer> getAllOffers() {
    List<Offer> offers = offerRepository.findAllByOfferStatus(OfferStatus.OPEN.name());

    offers.forEach(offer -> {
      List<File> files = new ArrayList<>(offer.getFiles());
      offer.getFiles().clear();
      offer.getFiles().addAll(fileService.decompressFile(files));
    });

    return offers;
  }

  @Override
  public List<Offer> getAllForUser(String userEmail) {
    return offerRepository.findAllByAuthor_Email(userEmail);
  }

  @Override
  @Transactional(readOnly = true)
  public Offer getOffer(Long id) {
    Offer fetchedOffer = offerRepository.findById(id).orElse(null);

    if (fetchedOffer == null) {
      String errorMessage = "No offer found for id: [" + id + "]";
      log.error(errorMessage);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    } else {
      List<File> files = new ArrayList<>(fetchedOffer.getFiles());
      fetchedOffer.getFiles().clear();
      fetchedOffer.getFiles().addAll(fileService.decompressFile(files));
      return fetchedOffer;
    }
  }

  @Override
  public List<Offer> getByUserEmailAndBidsExist(String userEmail) {
    return offerRepository.findAllByUser_EmailAndBidsExist(userEmail);
  }

  @Override
  public List<Offer> getAllByAssignedUserEmail(String userEmail) {
    return offerRepository.findAllByAssignedUser(userEmail);
  }

  @Override
  public List<Offer> getAllCompletedForUser(String userEmail) {
    return offerRepository.findAllCompletedForUser(userEmail);
  }

  @Override
  public Offer createOffer(Offer offer) {
    String errorMessage;

    List<Category> categories = new ArrayList<>();
    offer.getCategories().forEach(category -> categories.add(categoryService.getCategory(category.getName())));
    offer.getCategories().clear();
    offer.getCategories().addAll(categories);

    List<AdvertisementFormat> formats = new ArrayList<>();
    offer.getAdvertisementFormats().forEach(format -> formats.add(advertisementFormatService.getAdvertisementFormat(format.getName())));
    offer.getAdvertisementFormats().clear();
    offer.getAdvertisementFormats().addAll(formats);

    List<File> files = new ArrayList<>();
    offer.getFiles().forEach(file -> files.add(Objects.requireNonNull(fileService.findByUuid(file.getUuid()))));
    offer.getFiles().clear();
    offer.getFiles().addAll(files);

    offer.setAuthor(userService.getAuthenticatedUser());

    try {
      offerRepository.save(offer);
      files.forEach(file -> file.setOffer(offer));
      sendLedRequest();
    } catch (Exception ex) {
      errorMessage = ex.getMessage();
      log.error(errorMessage);
      throw new ResponseStatusException(
              HttpStatus.UNPROCESSABLE_ENTITY,
              errorMessage
      );
    }

    log.info("Created new offer: {}", offer);
    return offer;
  }

  private void sendLedRequest() {
    WebClient client = WebClient.create("http://" + ESP32_IP + ":" + ESP32_PORT);
    WebClient.RequestBodySpec request = client.method(HttpMethod.POST).uri("/");
    request.exchange().block();
  }

  @Override
  public Offer updateOffer(Long id, Offer offer) {
    User authenticatedUser = userService.getAuthenticatedUser();
    Offer offerToUpdate = offerRepository.findById(id).orElse(null);
    String errorMessage;

    if (offerToUpdate == null) {
      errorMessage = "Offer with id [" + id + "] not found!";
      log.error(errorMessage);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    } else {
      if (!authenticatedUser.getCreatedOffers().isEmpty() &&
              authenticatedUser.getCreatedOffers().contains(offerToUpdate)) {
        BeanUtils.copyProperties(
                offer,
                offerToUpdate,
                BeanHelper.getNullPropertyNames(offer, true)
        );

        log.info("Updated offer with id: [{}]", id);
        return offerToUpdate;
      } else {
        errorMessage = "You cannot update an offer that you didn't create. Id: [" + id + "]!";
        log.error(errorMessage);
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
      }
    }
  }

  @Override
  public void deassignFromOffer(String assigneeName, String offerId, String bidStatus) {
    String errorMessage;
    User assignee = userService.getUser(assigneeName);
    Offer offer = offerRepository.findById(Long.parseLong(offerId)).orElse(null);
    Bid bid = bidService.getBidByUserEmailAndOfferId(assigneeName, Long.parseLong(offerId));

    if (offer == null) {
      errorMessage = "Offer with id [" + offerId + "] not found!";
      log.error(errorMessage);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    offer.getAssignees().remove(assignee);
    bid.setBidStatus(BidStatus.valueOf(bidStatus));
  }

  @Override
  public void updateOfferStatus(Long offerId, String offerStatus) {
    String errorMessage;
    Offer offer = offerRepository.findById(offerId).orElse(null);

    if (offer == null) {
      errorMessage = "Offer with id [" + offerId + "] not found!";
      log.error(errorMessage);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    for (OfferStatus status : OfferStatus.getValues()) {
      if (status.getName().equals(offerStatus)) offer.setOfferStatus(status);
    }
  }

  @Override
  public String deleteOffer(Long id) {
    Offer offerToDelete = offerRepository.findById(id).orElse(null);

    if (offerToDelete == null) {
      String errorMessage = "Offer with id: [" + id + "] not found!";
      log.error(errorMessage);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
    }

    offerToDelete.setStatus(Status.DELETED);

    String successMessage = "Deleted offer with id: [" + id + "]";
    log.info(successMessage);
    return successMessage;
  }

  @Override
  public void checkAndUpdateExpiredOffers() {
    List<Offer> expiredOffers = offerRepository.findAllByExpireDateAndOfferStatus(OfferStatus.EXPIRED.name());

    if (!expiredOffers.isEmpty()) {
      log.info("Found " + expiredOffers.size() + " expired offers. Changing their status to EXPIRED...");
      expiredOffers.forEach(offer -> offer.setOfferStatus(OfferStatus.EXPIRED));
    } else {
      log.debug("No expired offers found.");
    }
  }
}
