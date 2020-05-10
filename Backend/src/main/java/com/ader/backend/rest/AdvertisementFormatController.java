package com.ader.backend.rest;

import com.ader.backend.entity.AdvertisementFormat;
import com.ader.backend.rest.dto.AdvertisementFormatDto;
import com.ader.backend.service.advertisementformat.AdvertisementFormatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/advertisement-format")
@Slf4j
@RequiredArgsConstructor
public class AdvertisementFormatController {

    private final AdvertisementFormatService advertisementFormatService;

    @GetMapping
    public ResponseEntity<List<AdvertisementFormatDto>> getAdvertisementFormats() {
        log.info("Requested all advertisement formats");
        return ResponseEntity.ok(AdvertisementFormatDto.toDto(advertisementFormatService.getAllAdvertisementFormats()));
    }

    @GetMapping("{id}")
    public ResponseEntity<AdvertisementFormatDto> getAdvertisementFormat(@PathVariable Long id) {
        log.info("Requested advertisement format with id: [{}]", id);
        return ResponseEntity.ok(AdvertisementFormatDto.toDto(advertisementFormatService.getAdvertisementFormat(id)));
    }

    @GetMapping("name/{name}")
    public ResponseEntity<AdvertisementFormatDto> getAdvertisementFormat(@PathVariable String name) {
        log.info("Requested advertisement format with name: [{}]", name);
        return ResponseEntity.ok(AdvertisementFormatDto.toDto(advertisementFormatService.getAdvertisementFormat(name)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("add")
    public ResponseEntity<AdvertisementFormatDto> createAdvertisementFormat(@RequestBody AdvertisementFormat advertisementFormat) {
        log.info("Requested creating new advertisement format with payload: [{}]", advertisementFormat);
        return ResponseEntity.ok(AdvertisementFormatDto.toDto(advertisementFormatService.createAdvertisementFormat(advertisementFormat)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<AdvertisementFormatDto> editAdvertisementFormat(@PathVariable Long id, @RequestBody AdvertisementFormat advertisementFormat) {
        log.info("Requested updating advertisement format with id: [{}], new payload: [{}]", id, advertisementFormat);
        return ResponseEntity.ok(AdvertisementFormatDto.toDto(advertisementFormatService.updateAdvertisementFormat(id, advertisementFormat)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("name/{name}")
    public ResponseEntity<AdvertisementFormatDto> editAdvertisementFormat(@PathVariable String name, @RequestBody AdvertisementFormat advertisementFormat) {
        log.info("Requested updating advertisement format with name: [{}], new payload: [{}]", name, advertisementFormat);
        return ResponseEntity.ok(AdvertisementFormatDto.toDto(advertisementFormatService.updateAdvertisementFormat(name, advertisementFormat)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAdvertisementFormat(@PathVariable Long id) {
        log.info("Requested deleting advertisement format with id: [{}]", id);
        return ResponseEntity.ok(advertisementFormatService.deleteAdvertisementFormat(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("name/{name}")
    public ResponseEntity<String> deleteAdvertisementFormat(@PathVariable String name) {
        log.info("Requested deleting advertisement format with name: [{}]", name);
        return ResponseEntity.ok(advertisementFormatService.deleteAdvertisementFormat(name));
    }
}
