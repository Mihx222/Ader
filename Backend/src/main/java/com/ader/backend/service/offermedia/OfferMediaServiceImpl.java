package com.ader.backend.service.offermedia;

import com.ader.backend.entity.offermedia.OfferMedia;
import com.ader.backend.entity.offermedia.OfferMediaDto;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.OfferMediaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OfferMediaServiceImpl implements OfferMediaService {

    private static final Logger log = LoggerFactory.getLogger(OfferMediaServiceImpl.class);
    private final OfferMediaRepository offerMediaRepository;

    public OfferMediaServiceImpl(OfferMediaRepository offerMediaRepository) {
        this.offerMediaRepository = offerMediaRepository;
    }

    @Override
    public ResponseEntity<List<OfferMediaDto>> getAllOfferMedia() {
        return ResponseEntity.ok(OfferMediaDto.toDto(offerMediaRepository.findAll()));
    }

    @Override
    public ResponseEntity<List<OfferMediaDto>> getAllOfferMediaForOffer(Long offerId) {
        return ResponseEntity.ok(OfferMediaDto.toDto(offerMediaRepository.findAllByOfferId(offerId)));
    }

    @Override
    public ResponseEntity<Object> createOfferMedia(OfferMedia offerMedia) {
        String errorMessage;

        try {
            offerMediaRepository.save(offerMedia);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        log.info("Successfully created offer media: [{}]", offerMedia);
        return ResponseEntity.ok(OfferMediaDto.toDto(offerMedia));
    }

    @Override
    public ResponseEntity<Object> updateOfferMedia(Long id, OfferMedia offerMedia) {
        String errorMessage;
        OfferMedia offerMediaToUpdate = offerMediaRepository.findById(id).orElse(null);

        if (offerMediaToUpdate == null) {
            errorMessage = "Offer media with id: [" + id + "] does not exist!";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
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
                return ResponseEntity.badRequest().body(errorMessage);
            }
        }

        log.info("Successfully updated offer media. New offer media: [{}]", offerMediaToUpdate);
        return ResponseEntity.ok(OfferMediaDto.toDto(offerMediaToUpdate));
    }

    @Override
    public ResponseEntity<Object> deleteOfferMedia(Long id) {
        String errorMessage;
        String successMessage;

        try {
            offerMediaRepository.deleteById(id);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        successMessage = "Successfully deleted offer media with id: [" + id + "]";
        log.info(successMessage);
        return ResponseEntity.ok(successMessage);
    }
}
