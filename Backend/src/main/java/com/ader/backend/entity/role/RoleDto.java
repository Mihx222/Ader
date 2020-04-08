package com.ader.backend.entity.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class RoleDto {

    private String name;

    public static List<RoleDto> toDto(List<Role> roles) {
        return roles.stream().map(RoleDto::toDto).collect(Collectors.toList());
    }

    public static RoleDto toDto(Role role) {
        return RoleDto.builder()
                .name(role.getName())
                .build();
    }
}
