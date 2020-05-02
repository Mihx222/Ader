package com.ader.backend.repository;

import com.ader.backend.entity.OfferImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OfferImageRepository extends JpaRepository<OfferImage, Long> {

    @Query("SELECT offermedia " +
            "FROM OfferImage offermedia " +
            "JOIN Offer offer ON offer = offermedia.offer " +
            "WHERE offer.id = ?1")
    List<OfferImage> findAllByOfferId(Long offerId);

    Optional<OfferImage> findByName(String name);
}
