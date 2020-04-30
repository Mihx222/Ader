package com.ader.backend.service.persona;

import com.ader.backend.entity.Persona;

import java.util.List;

public interface PersonaService {

    List<Persona> getAllPersonas();

    List<Persona> getAllPersonasByUser(String userEmail);

    Persona getPersonaById(Long id);

    Persona createPersona(Persona persona);

    Persona updatePersona(Long id, Persona persona);

    String deletePersona(Long id);
}
