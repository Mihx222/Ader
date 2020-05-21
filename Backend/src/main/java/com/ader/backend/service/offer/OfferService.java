package com.ader.backend.service.offer;

import com.ader.backend.entity.Offer;
import com.ader.backend.entity.OfferStatus;

import java.util.List;

public interface OfferService {

    List<Offer> getAllOffers();

    List<Offer> getAllForUser(String userEmail);

    Offer getOffer(Long id);

    List<Offer> getByUserEmailAndBidsExist(String userEmail);

    List<Offer> getAllByAssignedUserEmail(String userEmail);

    List<Offer> getAllCompletedForUser(String userEmail);

    Offer createOffer(Offer offer);

    Offer updateOffer(Long id, Offer offer);

    void updateOfferStatus(Long offerId, OfferStatus offerStatus);

    String deleteOffer(Long id);

    void checkAndUpdateExpiredOffers();
}
