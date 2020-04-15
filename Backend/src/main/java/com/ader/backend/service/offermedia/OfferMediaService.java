package com.ader.backend.service.offermedia;

import com.ader.backend.entity.offermedia.OfferMedia;
import com.ader.backend.entity.offermedia.OfferMediaDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OfferMediaService {

    ResponseEntity<List<OfferMediaDto>> getAllOfferMedia();

    ResponseEntity<List<OfferMediaDto>> getAllOfferMediaForOffer(Long offerId);

    ResponseEntity<Object> createOfferMedia(OfferMedia offerMedia);

    ResponseEntity<Object> updateOfferMedia(Long id, OfferMedia offerMedia);

    ResponseEntity<Object> deleteOfferMedia(Long id);
}
