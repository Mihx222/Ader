package com.ader.backend.entity.user;

import com.ader.backend.entity.Status;
import com.ader.backend.entity.offer.OfferDto;
import com.ader.backend.entity.role.RoleDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class UserDto {

    private Long id;
    private String brandName;
    private String brandWebsite;
    private String email;
    private Status status;
    private List<RoleDto> roles;
    private List<OfferDto> createdOffers;

    public static List<UserDto> toDto(List<User> users) {
        return users.stream().map(UserDto::toDto).collect(Collectors.toList());
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .brandName(user.getBrandName())
                .brandWebsite(user.getBrandWebsite())
                .email(user.getEmail())
                .roles(RoleDto.toDto(user.getRoles()))
                .createdOffers(OfferDto.toDto(user.getCreatedOffers()))
                .status(user.getStatus())
                .build();
    }
}
