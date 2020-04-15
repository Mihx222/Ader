package com.ader.backend.entity.offermedia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class OfferMediaDto {

    private Long id;
    private Long offerId;
    private String path;

    public static List<OfferMediaDto> toDto(List<OfferMedia> offerMedia) {
        return offerMedia.stream().map(OfferMediaDto::toDto).collect(Collectors.toList());
    }

    public static OfferMediaDto toDto(OfferMedia offerMedia) {
        return OfferMediaDto.builder()
                .id(offerMedia.getId())
                .offerId(offerMedia.getOffer().getId())
                .path(offerMedia.getPath())
                .build();
    }
}
