package com.ader.backend.rest.dto;

import com.ader.backend.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class RoleDto {

    private String name;

    public static List<RoleDto> toDto(List<Role> roles) {
        return roles.stream().map(RoleDto::toDto).collect(Collectors.toList());
    }

    public static RoleDto toDto(Role role) {
        return new RoleDto(role.getName());
    }
}
