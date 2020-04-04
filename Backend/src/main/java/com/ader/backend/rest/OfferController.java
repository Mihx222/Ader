package com.ader.backend.rest;

import com.ader.backend.entity.Offer;
import com.ader.backend.entity.dto.OfferDto;
import com.ader.backend.service.OfferService;
import org.springframework.http.ResponseEntity;
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
    public List<OfferDto> getOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("{id}")
    public ResponseEntity<OfferDto> getOffer(@PathVariable Long id) {
        return offerService.getOffer(id);
    }

    @PostMapping("add")
    public OfferDto createOffer(@RequestBody Offer offer) {
        return offerService.createOffer(offer);
    }

    @PutMapping("{id}")
    public ResponseEntity<OfferDto> updateOffer(@PathVariable Long id, @RequestBody Offer offer) {
        return offerService.updateOffer(id, offer);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOffer(@PathVariable Long id) {
        return offerService.deleteOffer(id);
    }
}
