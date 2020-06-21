package com.ader.backend.rest;

import com.ader.backend.entity.Bid;
import com.ader.backend.rest.dto.BidDto;
import com.ader.backend.rest.dto.OfferDto;
import com.ader.backend.service.bid.BidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/bid")
@Slf4j
@RequiredArgsConstructor
public class BidController {

  private final BidService bidService;

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<List<BidDto>> getAllBids() {
    log.info("Requested all bids");
    return ResponseEntity.ok(BidDto.toDto(bidService.getBids()));
  }

  @PreAuthorize("isAuthenticated() and hasRole('USER')")
  @GetMapping("user/{userEmail}")
  public ResponseEntity<List<BidDto>> getAllBidsByUser(@PathVariable String userEmail) {
    log.info("Requested all bids for user with email: [{}]", userEmail);
    return ResponseEntity.ok(BidDto.toDto(bidService.getBidsByUser(userEmail)));
  }

  @PreAuthorize("isAuthenticated() and hasRole('USER')")
  @GetMapping("get")
  public ResponseEntity<BidDto> getBidByUserAndOffer(
          @RequestParam("userEmail") String userEmail, @RequestParam("offerId") Long offerId
  ) {
    log.info("Requested bid for user with email: [{}] and offer with id: [{}]", userEmail, offerId);
    Bid bid = bidService.getBidByUserEmailAndOfferId(userEmail, offerId);
    if (bid == null) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.ok(BidDto.toDto(bid));
    }
  }

  @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
  @PostMapping("accept")
  public ResponseEntity<OfferDto> acceptBids(@RequestBody List<Bid> bids) {
    log.info("Accepting [{}] bids.", bids.size());
    bidService.acceptBids(bids);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
  @GetMapping("offer/{offerId}")
  public ResponseEntity<List<BidDto>> getAllBidsByOffer(@PathVariable Long offerId) {
    log.info("Requested all bids for offer with id: [{}]", offerId);
    return ResponseEntity.ok(BidDto.toDto(bidService.getBidsByOffer(offerId)));
  }

  @PreAuthorize("isAuthenticated() and hasRole('USER')")
  @PostMapping("add")
  public ResponseEntity<BidDto> createBid(@RequestBody BidDto bidDto) {
    log.info("Requested creating new bid with payload: [{}]", bidDto);
    return ResponseEntity.ok(BidDto.toDto(bidService.createBid(bidDto)));
  }

  @PreAuthorize("isAuthenticated() and hasRole('USER')")
  @PutMapping("{id}")
  public ResponseEntity<BidDto> updateBid(@PathVariable Long id, @RequestBody Bid bid) {
    log.info("Requested updating bid with id: [{}], with new payload: [{}]", id, bid);
    return ResponseEntity.ok(BidDto.toDto(bidService.updateBid(id, bid)));
  }

  @SuppressWarnings("rawtypes")
  @PreAuthorize("isAuthenticated()")
  @PostMapping("{id}/status")
  public ResponseEntity updateStatus(@PathVariable Long id, @RequestParam String newStatus) {
    log.info("Requested updating bid status with id: [{}], with new status: [{}]", id, newStatus);
    bidService.updateStatus(id, newStatus);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteBid(@PathVariable Long id) {
    log.info("Requested deleting bid with id: [{}]", id);
    return ResponseEntity.ok(bidService.deleteBid(id));
  }
}
