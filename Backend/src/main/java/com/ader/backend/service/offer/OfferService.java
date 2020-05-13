package com.ader.backend.service.offer;

import com.ader.backend.entity.Offer;

import java.util.List;

public interface OfferService {

    List<Offer> getAllOffers();

    Offer getOffer(Long id);

    Offer createOffer(Offer offer);

    Offer updateOffer(Long id, Offer offer);

    String deleteOffer(Long id);

    void checkAndUpdateExpiredOffers();
}
