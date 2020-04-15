package com.ader.backend.service.persona;

import com.ader.backend.entity.persona.Persona;
import com.ader.backend.entity.persona.PersonaDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PersonaService {

    ResponseEntity<List<PersonaDto>> getAllPersonas();

    ResponseEntity<List<PersonaDto>> getAllPersonasByUser(String userEmail);

    ResponseEntity<Object> getPersonaById(Long id);

    ResponseEntity<Object> createPersona(Persona persona);

    ResponseEntity<Object> updatePersona(Long id, Persona persona);

    ResponseEntity<Object> deletePersona(Long id);
}
