package com.ader.backend.entity.bid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class BidDto {

    private Long id;
    private Long offerId;
    private String userEmail;

    public static List<BidDto> toDto(List<Bid> bids) {
        return bids.stream().map(BidDto::toDto).collect(Collectors.toList());
    }

    public static BidDto toDto(Bid bid) {
        return BidDto.builder()
                .id(bid.getId())
                .offerId(bid.getOffer().getId())
                .userEmail(bid.getUser().getEmail())
                .build();
    }
}
