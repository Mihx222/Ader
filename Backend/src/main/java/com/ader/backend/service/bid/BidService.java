package com.ader.backend.service.bid;

import com.ader.backend.entity.Bid;
import com.ader.backend.rest.dto.BidDto;

import java.util.List;

public interface BidService {

  List<Bid> getBids();

  List<Bid> getBidsByUser(String userEmail);

  List<Bid> getBidsByOffer(Long offerId);

  Bid getBidByUserEmailAndOfferId(String userEmail, Long offerId);

  void acceptBids(List<Bid> bids);

  Bid createBid(BidDto bid);

  Bid updateBid(Long id, Bid bid);

  void updateStatus(Long id, String newStatus);

  String deleteBid(Long id);
}
