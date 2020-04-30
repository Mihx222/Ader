package com.ader.backend.service.offer;

import com.ader.backend.entity.Offer;
import com.ader.backend.entity.Status;
import com.ader.backend.entity.User;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.OfferRepository;
import com.ader.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final UserService userService;

    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public Offer getOffer(Long id) {
        Offer fetchedOffer = offerRepository.findById(id).orElse(null);

        if (fetchedOffer == null) {
            String errorMessage = "No offer found for id: [" + id + "]";
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        } else {
            return fetchedOffer;
        }
    }

    @Override
    public Offer createOffer(Offer offer) {
        String errorMessage;

        try {
            offerRepository.save(offer);
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
}
