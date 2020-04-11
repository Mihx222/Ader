package com.ader.backend.entity.offer;

import com.ader.backend.entity.Status;
import com.ader.backend.entity.bid.BidDto;
import com.ader.backend.entity.category.CategoryDto;
import com.ader.backend.entity.offermedia.OfferMediaDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class OfferDto {

    private Long id;
    private String name;
    private String description;
    private String expireDate;
    private String authorName;
    private String assigneeName;
    private List<CategoryDto> categories;
    private List<OfferMediaDto> offerMedia;
    private List<BidDto> bids;
    private OfferStatus offerStatus;
    private Status status;

    public static List<OfferDto> toDto(List<Offer> offers) {
        return offers.stream().map(OfferDto::toDto).collect(Collectors.toList());
    }

    public static OfferDto toDto(Offer offer) {
        return OfferDto.builder()
                .id(offer.getId())
                .name(offer.getName())
                .description(offer.getDescription())
                .expireDate(new SimpleDateFormat("EEEEE dd MMMMM yyyy HH:mm:ss").format(
                        offer.getExpireDate().getTime())
                )
                .authorName(offer.getAuthor().getBrandName())
                .assigneeName(offer.getAssignee() != null ? offer.getAssignee().getEmail() : null)
                .categories(CategoryDto.toDto(offer.getCategories()))
                .offerMedia(OfferMediaDto.toDto(offer.getOfferMedia()))
                .bids(BidDto.toDto(offer.getBids()))
                .offerStatus(offer.getOfferStatus())
                .status(offer.getStatus())
                .build();
    }
}
