package com.ader.backend.service.offer;

import com.ader.backend.entity.*;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.OfferRepository;
import com.ader.backend.service.advertisementformat.AdvertisementFormatService;
import com.ader.backend.service.category.CategoryService;
import com.ader.backend.service.file.FileService;
import com.ader.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final AdvertisementFormatService advertisementFormatService;

    @Override
    @Transactional(readOnly = true)
    public List<Offer> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();

        offers.forEach(offer -> offer.setFiles(fileService.decompressFile(offer.getFiles())));

        return offers;
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
            fetchedOffer.setFiles(fileService.decompressFile(fetchedOffer.getFiles()));
            return fetchedOffer;
        }
    }

    @Override
    public Offer createOffer(Offer offer) {
        String errorMessage;

        List<Category> categories = new ArrayList<>();
        offer.getCategories().forEach(category -> categories.add(categoryService.getCategory(category.getName())));
        offer.setCategories(categories);

        List<AdvertisementFormat> formats = new ArrayList<>();
        offer.getAdvertisementFormats().forEach(format -> formats.add(advertisementFormatService.getAdvertisementFormat(format.getName())));
        offer.setAdvertisementFormats(formats);

        List<File> files = new ArrayList<>();
        offer.getFiles().forEach(file -> files.add(Objects.requireNonNull(fileService.findByUuid(file.getUuid()))));
        offer.setFiles(files);

        offer.setAuthor(userService.getAuthenticatedUser());

        try {
            offerRepository.save(offer);
            files.forEach(file -> file.setOffer(offer));
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
