package com.ader.backend.service.bid;

import com.ader.backend.entity.Bid;

import java.util.List;

public interface BidService {

    List<Bid> getBids();

    List<Bid> getBidsByUser(String userEmail);

    List<Bid> getBidsByOffer(Long offerId);

    Bid createBid(Bid bid);

    Bid updateBid(Long id, Bid bid);

    String deleteBid(Long id);
}
