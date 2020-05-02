package com.ader.backend.rest.dto;

import com.ader.backend.entity.OfferImage;
import lombok.Builder;
import lombok.Data;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
public class OfferImageDto {

    private Long offerId;
    private String name;
    private String type;
    private byte[] picByte;

    public static List<OfferImageDto> toDto(List<OfferImage> offerImages) {
        return offerImages.stream().map(OfferImageDto::toDto).collect(Collectors.toList());
    }

    public static OfferImageDto toDto(OfferImage offerImage) {
        return OfferImageDto.builder()
                .offerId(offerImage.getOffer() != null ? offerImage.getOffer().getId() : null)
                .name(new String(Base64.getDecoder().decode(
                        Objects.requireNonNull(offerImage.getName()))
                ))
                .type(offerImage.getType())
                .picByte(offerImage.getPicByte())
                .build();
    }
}
