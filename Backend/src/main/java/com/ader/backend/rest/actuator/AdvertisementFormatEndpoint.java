package com.ader.backend.rest.actuator;

import com.ader.backend.rest.dto.AdvertisementFormatDto;
import com.ader.backend.service.advertisementformat.AdvertisementFormatService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Endpoint(id = "format")
public class AdvertisementFormatEndpoint {

    private final AdvertisementFormatService advertisementFormatService;

    public AdvertisementFormatEndpoint(AdvertisementFormatService advertisementFormatService) {
        this.advertisementFormatService = advertisementFormatService;
    }

    @ReadOperation
    public ResponseEntity<List<AdvertisementFormatDto>> getFormats() {
        return ResponseEntity.ok(
                AdvertisementFormatDto.toDto(advertisementFormatService.getAllAdvertisementFormats())
        );
    }
}
