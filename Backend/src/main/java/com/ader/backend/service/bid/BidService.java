package com.ader.backend.service.bid;

import com.ader.backend.entity.Bid;
import com.ader.backend.rest.dto.BidDto;

import java.util.List;

public interface BidService {

    List<Bid> getBids();

    List<Bid> getBidsByUser(String userEmail);

    List<Bid> getBidsByOffer(Long offerId);

    Bid getBidByUserEmailAndOfferId(String userEmail, Long offerId);

    Bid createBid(BidDto bid);

    Bid updateBid(Long id, Bid bid);

    String deleteBid(Long id);
}
