package com.ader.backend.entity.dto;

import com.ader.backend.entity.Status;
import com.ader.backend.entity.User;
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
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private Status status;
    private List<RoleDto> roles;
    private List<OfferDto> offers;

    public static List<UserDto> toDto(List<User> users) {
        return users.stream().map(UserDto::toDto).collect(Collectors.toList());
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .email(user.getEmail())
                .roles(RoleDto.toDto(user.getRoles()))
                .offers(OfferDto.toDto(user.getOffers()))
                .status(user.getStatus())
                .build();
    }
}
