package com.ader.backend.rest.dto;

import com.ader.backend.entity.Status;
import com.ader.backend.entity.User;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class UserDto {

    private Long id;
    private String brandName;
    private String brandWebsite;
    private String email;
    private Status status;
    private List<RoleDto> roles;
    private List<OfferDto> createdOffers;
    private List<OfferDto> acceptedOffers;
    private List<BidDto> bids;
    private List<PersonaDto> personas;

    public static List<UserDto> toDto(List<User> users) {
        return users.stream().map(UserDto::toDto).collect(Collectors.toList());
    }

    public static UserDto toDto(@NotNull User user) {
        return UserDto.builder()
                .id(user.getId())
                .brandName(user.getBrandName())
                .brandWebsite(user.getBrandWebsite())
                .email(user.getEmail())
                .roles(RoleDto.toDto(user.getRoles()))
                .createdOffers(OfferDto.toDto(user.getCreatedOffers()))
                .acceptedOffers(OfferDto.toDto(user.getAssignedOffers()))
                .bids(BidDto.toDto(user.getBids()))
                .personas(PersonaDto.toDto(user.getPersonas()))
                .status(user.getStatus())
                .build();
    }
}
