package com.ader.backend.rest;

import com.ader.backend.entity.bid.Bid;
import com.ader.backend.entity.bid.BidDto;
import com.ader.backend.service.bid.BidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/bid")
@Slf4j
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BidDto>> getAllBids() {
        log.info("Requested all bids");
        return bidService.getBids();
    }

    @GetMapping("user/{userEmail}")
    public ResponseEntity<List<BidDto>> getAllBidsByUser(@PathVariable String userEmail) {
        log.info("Requested all bids for user with email: [{}]", userEmail);
        return bidService.getBidsByUser(userEmail);
    }

    @GetMapping("offer/{offerId}")
    public ResponseEntity<List<BidDto>> getAllBidsByOffer(@PathVariable Long offerId) {
        log.info("Requested all bids for offer with id: [{}]", offerId);
        return bidService.getBidsByOffer(offerId);
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PostMapping("add")
    public ResponseEntity<Object> createBid(@RequestBody Bid bid) {
        log.info("Requested creating new bid with payload: [{}]", bid);
        return bidService.createBid(bid);
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<Object> updateBid(@PathVariable Long id, @RequestBody Bid bid) {
        log.info("Requested updating bid with id: [{}], with new payload: [{}]", id, bid);
        return bidService.updateBid(id, bid);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteBid(@PathVariable Long id) {
        log.info("Requested deleting bid with id: [{}]", id);
        return bidService.deleteBid(id);
    }
}
