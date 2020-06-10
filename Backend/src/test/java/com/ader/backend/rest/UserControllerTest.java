package com.ader.backend.rest;

import com.ader.backend.entity.User;
import com.ader.backend.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

  @MockBean
  private UserService userService;
  @Autowired
  private MockMvc mockMvc;

  private User testInfluencer;

  @BeforeEach
  void setUp() {
    testInfluencer = new User();
    testInfluencer.setId(1L);
    testInfluencer.setEmail("user@user.com");
    testInfluencer.setPassword("test");
  }

  @Test
  @WithMockUser(username = "test", roles = "ADMIN")
  void getUsers_whenInvoked_return200() throws Exception {
    mockMvc.perform(get("/rest/user")).andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "test")
  void getUser_whenInvoked_return200() throws Exception {
    when(userService.getUser(testInfluencer.getEmail())).thenReturn(testInfluencer);

    mockMvc.perform(get("/rest/user/{email}", testInfluencer.getEmail()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @WithMockUser(username = "test")
  void updateUser_whenInvoked_return200() throws Exception {
    when(userService.updateUser(any(String.class), eq(testInfluencer))).thenReturn(testInfluencer);

    mockMvc.perform(put("/rest/user/{email}", "haha").content(String.valueOf(testInfluencer)))
            .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "test", roles = "ADMIN")
  void deleteUser_whenInvoked_returns200() throws Exception {
    when(userService.deleteUser(any(String.class))).thenReturn("Success");

    mockMvc.perform(delete("/rest/user/{email}", testInfluencer.getEmail()))
            .andExpect(status().isOk());
    verify(userService, times(1)).deleteUser(any(String.class));
  }
}
