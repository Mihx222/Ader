package com.ader.backend.rest.dto;

import com.ader.backend.entity.File;
import lombok.Builder;
import lombok.Data;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class FileDto {

    private UUID uuid;
    private String name;
    private String type;
    private byte[] bytes;

    public static List<FileDto> toDto(List<File> files) {
        return files.stream().map(FileDto::toDto).collect(Collectors.toList());
    }

    public static FileDto toDto(File file) {
        return FileDto.builder()
                .uuid(file.getUuid())
                .name(new String(Base64.getDecoder().decode(
                        Objects.requireNonNull(file.getName()))
                ))
                .type(file.getType())
                .bytes(file.getBytes())
                .build();
    }
}
