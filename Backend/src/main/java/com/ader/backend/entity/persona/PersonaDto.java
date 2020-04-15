package com.ader.backend.entity.persona;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class PersonaDto {

    private Long id;
    private String activity;
    private String audience;
    private String sellingOrientation;
    private String values;

    public static List<PersonaDto> toDto(List<Persona> personas) {
        return personas.stream().map(PersonaDto::toDto).collect(Collectors.toList());
    }

    public static PersonaDto toDto(Persona persona) {
        return PersonaDto.builder()
                .id(persona.getId())
                .activity(persona.getActivity())
                .audience(persona.getAudience())
                .sellingOrientation(persona.getSellingOrientation())
                .values(persona.getValues())
                .build();
    }
}
