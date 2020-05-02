package com.ader.backend.rest;

import com.ader.backend.rest.dto.OfferImageDto;
import com.ader.backend.service.offerimage.OfferImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/offerimage")
@Slf4j
@RequiredArgsConstructor
public class OfferImageController {

    private final OfferImageService offerImageService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OfferImageDto>> getAll() {
        log.info("Requested all offer media");
        return ResponseEntity.ok(OfferImageDto.toDto(offerImageService.getAllOfferMedia()));
    }

    @GetMapping("{id}")
    public ResponseEntity<List<OfferImageDto>> getAllForOffer(@PathVariable Long id) {
        log.info("Requested all offer media for offer with id: [{}]", id);
        return ResponseEntity.ok(OfferImageDto.toDto(offerImageService.getAllOfferMediaForOffer(id)));
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
    @PostMapping("upload")
    public ResponseEntity<OfferImageDto> uploadImage(@RequestParam("imageFile") MultipartFile image) throws IOException {
        log.info("Requested uploading new image with size: [{}]", image.getBytes().length);
        return ResponseEntity.ok(OfferImageDto.toDto(offerImageService.uploadImage(image)));
    }

//    @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
//    @PutMapping("{id}")
//    public ResponseEntity<OfferImageDto> updateOfferMedia(@PathVariable Long id, @RequestBody OfferImage offerImage) {
//        log.info("Requested updating offer media with id: [{}], new payload: [{}]", id, offerImage);
//        return ResponseEntity.ok(OfferImageDto.toDto(offerImageService.updateOfferMedia(id, offerImage)));
//    }

    @PreAuthorize("isAuthenticated() and hasRole('ADVERTISER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        log.info("Requested deleting offer media with id: [{}]", id);
        return ResponseEntity.ok(offerImageService.deleteImage(id));
    }
}
