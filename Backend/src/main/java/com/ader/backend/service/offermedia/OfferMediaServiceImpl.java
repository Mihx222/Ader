package com.ader.backend.service.offermedia;

import com.ader.backend.entity.OfferMedia;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.OfferMediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OfferMediaServiceImpl implements OfferMediaService {

    private final OfferMediaRepository offerMediaRepository;

    @Override
    public List<OfferMedia> getAllOfferMedia() {
        return offerMediaRepository.findAll();
    }

    @Override
    public List<OfferMedia> getAllOfferMediaForOffer(Long offerId) {
        return offerMediaRepository.findAllByOfferId(offerId);
    }

    @Override
    public OfferMedia createOfferMedia(OfferMedia offerMedia) {
        String errorMessage;

        try {
            offerMediaRepository.save(offerMedia);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        log.info("Successfully created offer media: [{}]", offerMedia);
        return offerMedia;
    }

    @Override
    public OfferMedia updateOfferMedia(Long id, OfferMedia offerMedia) {
        String errorMessage;
        OfferMedia offerMediaToUpdate = offerMediaRepository.findById(id).orElse(null);

        if (offerMediaToUpdate == null) {
            errorMessage = "Offer media with id: [" + id + "] does not exist!";
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        } else {
            try {
                BeanUtils.copyProperties(
                        offerMedia,
                        offerMediaToUpdate,
                        BeanHelper.getNullPropertyNames(offerMedia, true)
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

        log.info("Successfully updated offer media. New offer media: [{}]", offerMediaToUpdate);
        return offerMediaToUpdate;
    }

    @Override
    public String deleteOfferMedia(Long id) {
        String errorMessage;
        String successMessage;

        try {
            offerMediaRepository.deleteById(id);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        successMessage = "Successfully deleted offer media with id: [" + id + "]";
        log.info(successMessage);
        return successMessage;
    }
}
