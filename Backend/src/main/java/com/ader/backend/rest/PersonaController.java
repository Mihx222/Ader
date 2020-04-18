package com.ader.backend.rest;

import com.ader.backend.entity.persona.Persona;
import com.ader.backend.entity.persona.PersonaDto;
import com.ader.backend.service.persona.PersonaService;
import com.ader.backend.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/persona")
@Slf4j
public class PersonaController {

    private final PersonaService personaService;
    private final UserService userService;

    public PersonaController(PersonaService personaService, UserService userService) {
        this.personaService = personaService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PersonaDto>> getAll() {
        log.info("All personas requested");
        return personaService.getAllPersonas();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<Object> getPersona(@PathVariable Long id) {
        log.info("Requested persona with id: [{}]", id);
        return personaService.getPersonaById(id);
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @GetMapping("user/{userEmail}")
    public ResponseEntity<List<PersonaDto>> getAllByUser(@PathVariable String userEmail) {
        log.info("Requested persona from user with email: [{}]", userEmail);
        return personaService.getAllPersonasByUser(userEmail);
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PostMapping("add")
    public ResponseEntity<Object> createPersona(@RequestBody Persona persona) {
        log.info("Requested creation of new persona with payload: [{}]", persona);
        return personaService.createPersona(persona);
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<Object> updatePersona(@PathVariable Long id, @RequestBody Persona persona) {
        log.info("Requested updating persona with id: [{}], with new persona: [{}]", id, persona);
        return personaService.updatePersona(id, persona);
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletePersona(@PathVariable Long id) {
        log.info("Requested deleting persona with id: [{}]", id);
        return personaService.deletePersona(id);
    }
}
