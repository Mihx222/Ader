package com.ader.backend.service.persona;

import com.ader.backend.entity.Persona;
import com.ader.backend.entity.User;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.PersonaRepository;
import com.ader.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final UserService userService;

    @Override
    public List<Persona> getAllPersonas() {
        return personaRepository.findAll();
    }

    @Override
    public List<Persona> getAllPersonasByUser(String userEmail) {
        User authenticatedUser = userService.getAuthenticatedUser();

        if (!authenticatedUser.getEmail().equals(userEmail)) {
            String errorMessage = "You do not have the rights to retrieve the personas for user: [" +
                    userEmail + "]";
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        return personaRepository.findAllByUserEmail(userEmail);
    }

    @Override
    public Persona getPersonaById(Long id) {
        Persona persona = personaRepository.findById(id).orElse(null);

        if (persona == null) {
            String errorMessage = "Persona with id: [" + id + "] not found!";
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        return persona;
    }

    @Override
    public Persona createPersona(Persona persona) {
        String errorMessage;

        try {
            personaRepository.save(persona);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        log.info("Successfully created persona: [{}]", persona);
        return persona;
    }

    @Override
    public Persona updatePersona(Long id, Persona persona) {
        String errorMessage;
        Persona personaToUpdate = personaRepository.findById(id).orElse(null);

        if (personaToUpdate == null) {
            errorMessage = "Persona with id: [" + id + "] not found!";
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
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
                throw new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        errorMessage
                );
            }
        }

        log.info("Successfully updated persona. New persona: [{}]", personaToUpdate);
        return personaToUpdate;
    }

    @Override
    public String deletePersona(Long id) {
        String errorMessage;
        String successMessage;

        try {
            personaRepository.deleteById(id);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        successMessage = "Successfully deleted persona with id: [" + id + "]";
        log.info(successMessage);
        return successMessage;
    }
}
