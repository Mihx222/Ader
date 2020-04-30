package com.ader.backend.rest.dto;

import com.ader.backend.entity.Bid;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class BidDto {

    private Long id;
    private Long offerId;
    private String userEmail;

    public static List<BidDto> toDto(@NotNull List<Bid> bids) {
        return bids.stream().map(BidDto::toDto).collect(Collectors.toList());
    }

    public static BidDto toDto(@NotNull Bid bid) {
        return new BidDto(bid.getId(), bid.getOffer().getId(), bid.getUser().getEmail());
    }
}
