package com.ader.backend.rest.dto;

import com.ader.backend.entity.Offer;
import com.ader.backend.entity.OfferStatus;
import com.ader.backend.entity.Status;
import com.ader.backend.entity.User;
import lombok.Builder;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class OfferDto {

    private Long id;
    private String name;
    private String description;
    private String expireDate;
    private String authorName;
    private List<String> assigneeNames;
    private List<CategoryDto> categories;
    private List<FileDto> files;
    private List<BidDto> bids;
    private List<AdvertisementFormatDto> advertisementFormats;
    private Boolean freeProductSample;
    private Boolean advertisementReview;
    private String compensation;
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
                .assigneeNames(offer.getAssignees() == null ? null : offer.getAssignees().stream().map(User::getEmail).collect(Collectors.toList()))
                .categories(CategoryDto.toDto(offer.getCategories()))
                .files(FileDto.toDto(offer.getFiles()))
                .bids(BidDto.toDto(offer.getBids()))
                .advertisementFormats(AdvertisementFormatDto.toDto(offer.getAdvertisementFormats()))
                .freeProductSample(offer.getFreeProductSample() == null ? Boolean.FALSE : offer.getFreeProductSample())
                .advertisementReview(offer.getAdvertisementReview() == null ? Boolean.TRUE : offer.getAdvertisementReview())
                .compensation(offer.getCompensation())
                .offerStatus(offer.getOfferStatus())
                .status(offer.getStatus())
                .build();
    }
}
