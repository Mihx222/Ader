package com.ader.backend.entity.offer;

import com.ader.backend.entity.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class OfferDto {

    private Long id;
    private String name;
    private String description;
    private String authorName;
    private Status status;

    public static List<OfferDto> toDto(List<Offer> offers) {
        return offers.stream().map(OfferDto::toDto).collect(Collectors.toList());
    }

    public static OfferDto toDto(Offer offer) {
        return OfferDto.builder()
                .id(offer.getId())
                .name(offer.getName())
                .description(offer.getDescription())
                .authorName(offer.getAuthor().getBrandName())
                .status(offer.getStatus())
                .build();
    }
}
