package com.ader.backend.service;

import com.ader.backend.entity.Offer;
import com.ader.backend.entity.dto.OfferDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OfferService {

    List<OfferDto> getAllOffers();

    ResponseEntity<OfferDto> getOffer(Long id);

    OfferDto createOffer(Offer offer);

    ResponseEntity<OfferDto> updateOffer(Long id, Offer offer);

    ResponseEntity<String> deleteOffer(Long id);
}
