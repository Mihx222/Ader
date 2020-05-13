package com.ader.backend.repository;

import com.ader.backend.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query(nativeQuery = true,
            value = "select * from offers " +
                    "where expire_date < now() " +
                    "and not offer_status = ?1"
    )
    List<Offer> findAllByExpireDateAndOfferStatus(String offerStatus);
}
