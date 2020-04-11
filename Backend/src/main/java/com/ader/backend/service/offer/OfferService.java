package com.ader.backend.service.offer;

import com.ader.backend.entity.offer.Offer;
import com.ader.backend.entity.offer.OfferDto;
import liquibase.exception.DatabaseException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OfferService {

    ResponseEntity<List<OfferDto>> getAllOffers();

    ResponseEntity<OfferDto> getOffer(Long id);

    ResponseEntity<OfferDto> createOffer(Offer offer) throws DatabaseException;

    ResponseEntity<OfferDto> updateOffer(Long id, Offer offer);

    ResponseEntity<String> deleteOffer(Long id);
}
