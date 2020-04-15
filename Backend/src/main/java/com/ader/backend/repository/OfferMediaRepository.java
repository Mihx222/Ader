package com.ader.backend.repository;

import com.ader.backend.entity.offermedia.OfferMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferMediaRepository extends JpaRepository<OfferMedia, Long> {

    @Query("SELECT offermedia " +
            "FROM OfferMedia offermedia " +
            "JOIN Offer offer ON offer = offermedia.offer " +
            "WHERE offer.id = ?1")
    List<OfferMedia> findAllByOfferId(Long offerId);
}
