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
        return personaService.getAllPersonas();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<Object> getPersona(@PathVariable Long id) {
        log.info("User: [{}] requested persona with id: [{}]",
                userService.getAuthenticatedUser().getEmail(), id);
        return personaService.getPersonaById(id);
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @GetMapping("user/{userEmail}")
    public ResponseEntity<List<PersonaDto>> getAllByUser(@PathVariable String userEmail) {
        return personaService.getAllPersonasByUser(userEmail);
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PostMapping("add")
    public ResponseEntity<Object> createPersona(@RequestBody Persona persona) {
        return personaService.createPersona(persona);
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @PutMapping("{id}")
    public ResponseEntity<Object> updatePersona(@PathVariable Long id, @RequestBody Persona persona) {
        return personaService.updatePersona(id, persona);
    }

    @PreAuthorize("isAuthenticated() and hasRole('USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletePersona(@PathVariable Long id) {
        return personaService.deletePersona(id);
    }
}
