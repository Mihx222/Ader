package com.ader.backend.rest;

import com.ader.backend.entity.Persona;
import com.ader.backend.entity.User;
import com.ader.backend.rest.dto.PersonaDto;
import com.ader.backend.service.persona.PersonaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PersonaControllerTest {

  @Mock
  private PersonaService personaService;

  @InjectMocks
  private PersonaController personaController;

  private MockMvc mockMvc;
  private Persona persona1;
  private Persona persona2;
  private List<Persona> personas;
  private User testInfluencer;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(personaController).build();

    persona1 = new Persona();
    persona1.setId(1L);
    persona2 = new Persona();
    persona2.setId(2L);

    personas = new ArrayList<>();
    personas.addAll(Arrays.asList(persona1, persona2));

    testInfluencer = new User();
    testInfluencer.setId(1L);
    testInfluencer.setEmail("user@user.com");
    testInfluencer.setPassword("test");
  }

  @Test
  void getAll_whenInvoked_return200() throws Exception {
    mockMvc.perform(get("/rest/persona")).andExpect(status().isOk());
  }

  @Test
  void getPersona_whenInvoked_return200() throws Exception {
    when(personaService.getPersonaById(any(Long.class))).thenReturn(persona1);

    mockMvc.perform(get("/rest/persona/{id}", persona1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void getAllByUser_whenInvoked_return200() throws Exception {
    when(personaService.getAllPersonasByUser(any(String.class))).thenReturn(personas);

    mockMvc.perform(get("/rest/persona/user/{userEmail}", testInfluencer.getEmail()))
            .andExpect(status().isOk());
  }

  @Test
  void createPersona_whenInvoked_return200() throws Exception {
    when(personaService.createPersona(any(Persona.class))).thenReturn(persona1);

    mockMvc.perform(post("/rest/persona/add").content(String.valueOf(persona1)))
            .andExpect(status().isOk());
  }

  @Test
  void updatePersona_whenInvoked_return200() throws Exception {
    when(personaService.updatePersona(any(Long.class), eq(persona1))).thenReturn(persona1);

    mockMvc.perform(put("/rest/persona/{id}", persona1.getId()).content(String.valueOf(PersonaDto.toDto(persona1))))
            .andExpect(status().isOk());
  }

  @Test
  void deletePersona_whenInvoked_return200() throws Exception {
    when(personaService.deletePersona(any(Long.class))).thenReturn("Success");

    mockMvc.perform(delete("/rest/persona/{id}", persona1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.TEXT_PLAIN + ";charset=ISO-8859-1"));
  }
}
