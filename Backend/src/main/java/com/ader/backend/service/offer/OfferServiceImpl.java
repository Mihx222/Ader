package com.ader.backend.service.offer;

import com.ader.backend.entity.Status;
import com.ader.backend.entity.offer.Offer;
import com.ader.backend.entity.offer.OfferDto;
import com.ader.backend.entity.user.User;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.OfferRepository;
import com.ader.backend.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@Slf4j
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final UserService userService;

    public OfferServiceImpl(OfferRepository offerRepository,
                            UserService userService) {
        this.offerRepository = offerRepository;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<OfferDto>> getAllOffers() {
        return ResponseEntity.ok(OfferDto.toDto(offerRepository.findAll()));
    }

    @Override
    public ResponseEntity<Object> getOffer(Long id) {
        Offer fetchedOffer = offerRepository.findById(id).orElse(null);

        if (fetchedOffer == null) {
            String errorMessage = "No offer found for id: [" + id + "]";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } else {
            return ResponseEntity.ok(OfferDto.toDto(fetchedOffer));
        }
    }

    @Override
    public ResponseEntity<Object> createOffer(Offer offer) {
        String errorMessage;


        try {
            offerRepository.save(offer);
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
            log.error(errorMessage);
            return ResponseEntity.unprocessableEntity().body(errorMessage);
        }

        log.info("Created new offer: {}", offer);
        return ResponseEntity.ok(OfferDto.toDto(offer));
    }

    @Override
    public ResponseEntity<Object> updateOffer(Long id, Offer offer) {
        User authenticatedUser = userService.getAuthenticatedUser();
        Offer offerToUpdate = offerRepository.findById(id).orElse(null);
        String errorMessage;

        if (offerToUpdate == null) {
            errorMessage = "Offer with id [" + id + "] not found!";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } else {
            if (!authenticatedUser.getCreatedOffers().isEmpty() &&
                    authenticatedUser.getCreatedOffers().contains(offerToUpdate)) {
                BeanUtils.copyProperties(
                        offer,
                        offerToUpdate,
                        BeanHelper.getNullPropertyNames(offer, true)
                );

                log.info("Updated offer with id: [{}]", id);
                return ResponseEntity.ok(OfferDto.toDto(offerToUpdate));
            } else {
                errorMessage = "You cannot update an offer that you didn't create. Id: [" + id + "]!";
                log.error(errorMessage);
                return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
            }
        }
    }

    @Override
    public ResponseEntity<Object> deleteOffer(Long id) {
        Offer offerToDelete = offerRepository.findById(id).orElse(null);

        if (offerToDelete == null) {
            String errorMessage = "Offer with id: [" + id + "] not found!";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        offerToDelete.setStatus(Status.DELETED);

        log.info("Deleted offer with id: [{}]", id);
        return ResponseEntity.ok("Deleted offer with id: [" + id + "]");
    }
}
