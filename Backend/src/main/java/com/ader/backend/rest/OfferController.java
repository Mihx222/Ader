package com.ader.backend.rest;

import com.ader.backend.entity.Offer;
import com.ader.backend.rest.dto.OfferDto;
import com.ader.backend.service.offer.OfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("ALL")
@CrossOrigin("*")
@RestController
@RequestMapping("rest/offer")
@Slf4j
@RequiredArgsConstructor
public class OfferController {

  private final OfferService offerService;

  @GetMapping
  public ResponseEntity<List<OfferDto>> getOffers() {
    log.info("Requested all offers");
    return ResponseEntity.ok(OfferDto.toDto(offerService.getAllOffers()));
  }

  @GetMapping("{id}")
  public ResponseEntity<OfferDto> getOffer(@PathVariable Long id) {
    log.info("Requested offer with id: [{}]", id);
    return ResponseEntity.ok(OfferDto.toDto(offerService.getOffer(id)));
  }

  @GetMapping("/user/{userEmail}")
  public ResponseEntity<List<OfferDto>> getOffersByUser(
          @PathVariable String userEmail,
          @RequestParam(required = false) boolean hasBids,
          @RequestParam(required = false) boolean assigned,
          @RequestParam(required = false) boolean completed
  ) {
    log.info("Requested offers for user: [{}].", userEmail);
    if (hasBids) {
      return ResponseEntity.ok(OfferDto.toDto(offerService.getByUserEmailAndBidsExist(userEmail)));
    } else if (assigned) {
      return ResponseEntity.ok(OfferDto.toDto(offerService.getAllByAssignedUserEmail(userEmail)));
    } else if (completed) {
      return ResponseEntity.ok(OfferDto.toDto(offerService.getAllCompletedForUser(userEmail)));
    } else {
      return ResponseEntity.ok(OfferDto.toDto(offerService.getAllForUser(userEmail)));
    }
  }

  @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
  @PostMapping("add")
  public ResponseEntity<OfferDto> createOffer(@RequestBody Offer offer) {
    log.info("Requested creating new offer with payload: [{}]", offer);
    return ResponseEntity.ok(OfferDto.toDto(offerService.createOffer(offer)));
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping("{id}")
  public ResponseEntity<OfferDto> updateOffer(@PathVariable Long id, @RequestBody Offer offer) {
    log.info("Requested updating offer with id: [{}], new payload: [{}]", id, offer);
    return ResponseEntity.ok(OfferDto.toDto(offerService.updateOffer(id, offer)));
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("deassign")
  public ResponseEntity deassignUser(@RequestParam String assigneeName, @RequestParam String offerId, @RequestParam String bidStatus) {
    log.info("Requested deassigning user with email: [{}], from offer with id: [{}]", assigneeName, offerId);
    offerService.deassignFromOffer(assigneeName, offerId, bidStatus);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("{id}/status")
  public ResponseEntity updateStatus(@PathVariable Long id, @RequestParam String newStatus) {
    log.info("Requested updating status of offer with [{}] to: [{}]", id, newStatus);
    offerService.updateOfferStatus(id, newStatus);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteOffer(@PathVariable Long id) {
    log.info("Requested deleting offer with id: [{}]", id);
    return ResponseEntity.ok(offerService.deleteOffer(id));
  }
}
