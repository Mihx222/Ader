package com.ader.backend.rest;

import com.ader.backend.entity.offermedia.OfferMedia;
import com.ader.backend.entity.offermedia.OfferMediaDto;
import com.ader.backend.service.offermedia.OfferMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/offermedia")
@Slf4j
public class OfferMediaController {

    private final OfferMediaService offerMediaService;

    public OfferMediaController(OfferMediaService offerMediaService) {
        this.offerMediaService = offerMediaService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OfferMediaDto>> getAll() {
        log.info("Requested all offer media");
        return offerMediaService.getAllOfferMedia();
    }

    @GetMapping("{id}")
    public ResponseEntity<List<OfferMediaDto>> getAllForOffer(@PathVariable Long id) {
        log.info("Requested all offer media for offer with id: [{}]", id);
        return offerMediaService.getAllOfferMediaForOffer(id);
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
    @PostMapping("add")
    public ResponseEntity<Object> createOfferMedia(@RequestBody OfferMedia offerMedia) {
        log.info("Requested creating new offer media with payload: [{}]", offerMedia);
        return offerMediaService.createOfferMedia(offerMedia);
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
    @PutMapping("{id}")
    public ResponseEntity<Object> updateOfferMedia(
            @PathVariable Long id,
            @RequestBody OfferMedia offerMedia
    ) {
        log.info("Requested updating offer media with id: [{}], new payload: [{}]", id, offerMedia);
        return offerMediaService.updateOfferMedia(id, offerMedia);
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteOfferMedia(@PathVariable Long id) {
        log.info("Requested deleting offer media with id: [{}]", id);
        return offerMediaService.deleteOfferMedia(id);
    }
}
