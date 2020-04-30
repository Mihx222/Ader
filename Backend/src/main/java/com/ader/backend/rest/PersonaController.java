package com.ader.backend.rest;

import com.ader.backend.entity.Persona;
import com.ader.backend.rest.dto.PersonaDto;
import com.ader.backend.service.persona.PersonaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/persona")
@Slf4j
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PersonaDto>> getAll() {
        log.info("All personas requested");
        return ResponseEntity.ok(PersonaDto.toDto(personaService.getAllPersonas()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<PersonaDto> getPersona(@PathVariable Long id) {
        log.info("Requested persona with id: [{}]", id);
        return ResponseEntity.ok(PersonaDto.toDto(personaService.getPersonaById(id)));
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @GetMapping("user/{userEmail}")
    public ResponseEntity<List<PersonaDto>> getAllByUser(@PathVariable String userEmail) {
        log.info("Requested persona from user with email: [{}]", userEmail);
        return ResponseEntity.ok(PersonaDto.toDto(personaService.getAllPersonasByUser(userEmail)));
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PostMapping("add")
    public ResponseEntity<PersonaDto> createPersona(@RequestBody Persona persona) {
        log.info("Requested creation of new persona with payload: [{}]", persona);
        return ResponseEntity.ok(PersonaDto.toDto(personaService.createPersona(persona)));
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<PersonaDto> updatePersona(@PathVariable Long id, @RequestBody Persona persona) {
        log.info("Requested updating persona with id: [{}], with new persona: [{}]", id, persona);
        return ResponseEntity.ok(PersonaDto.toDto(personaService.updatePersona(id, persona)));
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePersona(@PathVariable Long id) {
        log.info("Requested deleting persona with id: [{}]", id);
        return ResponseEntity.ok(personaService.deletePersona(id));
    }
}
