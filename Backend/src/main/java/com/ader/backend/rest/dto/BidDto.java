package com.ader.backend.rest.dto;

import com.ader.backend.entity.Bid;
import com.ader.backend.entity.BidStatus;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class BidDto {

    private Long id;
    private Long offerId;
    private String userEmail;
    private PersonaDto persona;
    private Boolean acceptInitialRequirements;
    private Boolean freeProductSample;
    private String compensation;
    private BidStatus bidStatus;

    public static List<BidDto> toDto(@NotNull List<Bid> bids) {
        return bids.stream().map(BidDto::toDto).collect(Collectors.toList());
    }

    public static BidDto toDto(@NotNull Bid bid) {
        return BidDto.builder()
                .id(bid.getId())
                .offerId(bid.getOffer().getId())
                .persona(PersonaDto.toDto(bid.getPersona()))
                .userEmail(bid.getUser().getEmail())
                .acceptInitialRequirements(bid.getAcceptInitialRequirements())
                .freeProductSample(bid.getFreeProductSample())
                .compensation(bid.getCompensation())
                .bidStatus(bid.getBidStatus())
                .build();
    }
}
