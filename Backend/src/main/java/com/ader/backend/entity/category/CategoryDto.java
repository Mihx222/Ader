package com.ader.backend.entity.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class CategoryDto {

    private String name;

    public static List<CategoryDto> toDto(List<Category> categories) {
        return categories.stream().map(CategoryDto::toDto).collect(Collectors.toList());
    }

    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .name(category.getName())
                .build();
    }
}
