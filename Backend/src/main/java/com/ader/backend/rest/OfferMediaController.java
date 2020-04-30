package com.ader.backend.rest;

import com.ader.backend.entity.OfferMedia;
import com.ader.backend.rest.dto.OfferMediaDto;
import com.ader.backend.service.offermedia.OfferMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/offermedia")
@Slf4j
@RequiredArgsConstructor
public class OfferMediaController {

    private final OfferMediaService offerMediaService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OfferMediaDto>> getAll() {
        log.info("Requested all offer media");
        return ResponseEntity.ok(OfferMediaDto.toDto(offerMediaService.getAllOfferMedia()));
    }

    @GetMapping("{id}")
    public ResponseEntity<List<OfferMediaDto>> getAllForOffer(@PathVariable Long id) {
        log.info("Requested all offer media for offer with id: [{}]", id);
        return ResponseEntity.ok(OfferMediaDto.toDto(offerMediaService.getAllOfferMediaForOffer(id)));
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
    @PostMapping("add")
    public ResponseEntity<OfferMediaDto> createOfferMedia(@RequestBody OfferMedia offerMedia) {
        log.info("Requested creating new offer media with payload: [{}]", offerMedia);
        return ResponseEntity.ok(OfferMediaDto.toDto(offerMediaService.createOfferMedia(offerMedia)));
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
    @PutMapping("{id}")
    public ResponseEntity<OfferMediaDto> updateOfferMedia(@PathVariable Long id, @RequestBody OfferMedia offerMedia) {
        log.info("Requested updating offer media with id: [{}], new payload: [{}]", id, offerMedia);
        return ResponseEntity.ok(OfferMediaDto.toDto(offerMediaService.updateOfferMedia(id, offerMedia)));
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOfferMedia(@PathVariable Long id) {
        log.info("Requested deleting offer media with id: [{}]", id);
        return ResponseEntity.ok(offerMediaService.deleteOfferMedia(id));
    }
}
