package com.ader.backend.rest.dto;

import com.ader.backend.entity.AdvertisementFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class AdvertisementFormatDto {

    private String name;

    public static List<AdvertisementFormatDto> toDto(@NotNull List<AdvertisementFormat> advertisementFormats) {
        return advertisementFormats.stream().map(AdvertisementFormatDto::toDto).collect(Collectors.toList());
    }

    public static AdvertisementFormatDto toDto(@NotNull AdvertisementFormat advertisementFormat) {
        return new AdvertisementFormatDto(advertisementFormat.getName());
    }
}
