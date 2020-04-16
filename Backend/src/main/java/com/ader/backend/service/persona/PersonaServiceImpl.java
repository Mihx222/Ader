package com.ader.backend.service.persona;

import com.ader.backend.entity.persona.Persona;
import com.ader.backend.entity.persona.PersonaDto;
import com.ader.backend.entity.user.User;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.PersonaRepository;
import com.ader.backend.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final UserService userService;

    public PersonaServiceImpl(PersonaRepository personaRepository, UserService userService) {
        this.personaRepository = personaRepository;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<PersonaDto>> getAllPersonas() {
        return ResponseEntity.ok(PersonaDto.toDto(personaRepository.findAll()));
    }

    @Override
    public ResponseEntity<List<PersonaDto>> getAllPersonasByUser(String userEmail) {
        User authenticatedUser = userService.getAuthenticatedUser();

        if (!authenticatedUser.getEmail().equals(userEmail)) {
            String errorMessage = "You do not have the rights to retrieve the personas for user: [" +
                    userEmail + "]";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(PersonaDto.toDto(personaRepository.findAllByUserEmail(userEmail)));
    }

    @Override
    public ResponseEntity<Object> getPersonaById(Long id) {
        Persona persona = personaRepository.findById(id).orElse(null);

        if (persona == null) {
            String errorMessage = "Persona with id: [" + id + "] not found!";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        return ResponseEntity.ok(PersonaDto.toDto(persona));
    }

    @Override
    public ResponseEntity<Object> createPersona(Persona persona) {
        String errorMessage;

        try {
            personaRepository.save(persona);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        log.info("Successfully created persona: [{}]", persona);
        return ResponseEntity.ok(PersonaDto.toDto(persona));
    }

    @Override
    public ResponseEntity<Object> updatePersona(Long id, Persona persona) {
        String errorMessage;
        Persona personaToUpdate = personaRepository.findById(id).orElse(null);

        if (personaToUpdate == null) {
            errorMessage = "Persona with id: [" + id + "] not found!";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } else {
            try {
                BeanUtils.copyProperties(
                        persona,
                        personaToUpdate,
                        BeanHelper.getNullPropertyNames(persona, true)
                );
            } catch (Exception e) {
                errorMessage = e.getMessage();
                log.error(errorMessage);
                return ResponseEntity.badRequest().body(errorMessage);
            }
        }

        log.info("Successfully updated persona. New persona: [{}]", personaToUpdate);
        return ResponseEntity.ok(PersonaDto.toDto(personaToUpdate));
    }

    @Override
    public ResponseEntity<Object> deletePersona(Long id) {
        String errorMessage;
        String successMessage;

        try {
            personaRepository.deleteById(id);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        successMessage = "Successfully deleted persona with id: [" + id + "]";
        log.info(successMessage);
        return ResponseEntity.ok(successMessage);
    }
}
