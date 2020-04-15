package com.ader.backend.service.bid;

import com.ader.backend.entity.bid.Bid;
import com.ader.backend.entity.bid.BidDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BidService {

    ResponseEntity<List<BidDto>> getBids();

    ResponseEntity<List<BidDto>> getBidsByUser(String userEmail);

    ResponseEntity<List<BidDto>> getBidsByOffer(Long offerId);

    ResponseEntity<Object> createBid(Bid bid);

    ResponseEntity<Object> updateBid(Long id, Bid bid);

    ResponseEntity<Object> deleteBid(Long id);
}
