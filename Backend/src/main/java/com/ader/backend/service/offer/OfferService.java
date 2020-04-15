package com.ader.backend.service.offer;

import com.ader.backend.entity.offer.Offer;
import com.ader.backend.entity.offer.OfferDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OfferService {

    ResponseEntity<List<OfferDto>> getAllOffers();

    ResponseEntity<Object> getOffer(Long id);

    ResponseEntity<Object> createOffer(Offer offer);

    ResponseEntity<Object> updateOffer(Long id, Offer offer);

    ResponseEntity<Object> deleteOffer(Long id);
}
