package com.ader.backend.rest.dto;

import com.ader.backend.entity.OfferMedia;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class OfferMediaDto {

    private Long id;
    private Long offerId;
    private String path;

    public static List<OfferMediaDto> toDto(List<OfferMedia> offerMedia) {
        return offerMedia.stream().map(OfferMediaDto::toDto).collect(Collectors.toList());
    }

    public static OfferMediaDto toDto(OfferMedia offerMedia) {
        return new OfferMediaDto(
                offerMedia.getId(),
                offerMedia.getOffer().getId(),
                offerMedia.getPath()
        );
    }
}
