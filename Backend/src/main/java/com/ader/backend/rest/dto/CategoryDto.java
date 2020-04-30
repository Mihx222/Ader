package com.ader.backend.rest.dto;

import com.ader.backend.entity.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class CategoryDto {

    private String name;

    public static List<CategoryDto> toDto(List<Category> categories) {
        return categories.stream().map(CategoryDto::toDto).collect(Collectors.toList());
    }

    public static CategoryDto toDto(Category category) {
        return new CategoryDto(category.getName());
    }
}
