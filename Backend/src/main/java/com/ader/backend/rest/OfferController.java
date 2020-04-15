package com.ader.backend.rest;

import com.ader.backend.entity.offer.Offer;
import com.ader.backend.entity.offer.OfferDto;
import com.ader.backend.service.offer.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/offer")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public ResponseEntity<List<OfferDto>> getOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getOffer(@PathVariable Long id) {
        return offerService.getOffer(id);
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
    @PostMapping("add")
    public ResponseEntity<Object> createOffer(@RequestBody Offer offer) {
        return offerService.createOffer(offer);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("{id}")
    public ResponseEntity<Object> updateOffer(@PathVariable Long id, @RequestBody Offer offer) {
        return offerService.updateOffer(id, offer);
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteOffer(@PathVariable Long id) {
        return offerService.deleteOffer(id);
    }
}
