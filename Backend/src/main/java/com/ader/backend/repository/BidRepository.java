package com.ader.backend.repository;

import com.ader.backend.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query("SELECT bid " +
            "FROM Bid bid " +
            "JOIN User user ON user = bid.user " +
            "WHERE user.email = ?1")
    List<Bid> findAllByUserEmail(String userEmail);

    @Query("SELECT bid " +
            "FROM Bid bid " +
            "JOIN Offer offer ON offer = bid.offer " +
            "WHERE offer.id = ?1")
    List<Bid> findAllByOfferId(Long offerId);

    Bid findByUser_EmailAndOffer_Id(String userEmail, Long offerId);
}
