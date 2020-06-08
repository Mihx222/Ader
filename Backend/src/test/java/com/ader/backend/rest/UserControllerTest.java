package com.ader.backend.rest;

import com.ader.backend.entity.User;
import com.ader.backend.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  private MockMvc mockMvc;
  private User testInfluencer;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

    testInfluencer = new User();
    testInfluencer.setId(1L);
    testInfluencer.setEmail("user@user.com");
    testInfluencer.setPassword("test");
  }

  @Test
  void getUsers_whenInvoked_return200() throws Exception {
    mockMvc.perform(get("/rest/user")).andExpect(status().isOk());
  }

  @Test
  void getUser_whenInvoked_return200() throws Exception {
    when(userService.getUser(any(String.class))).thenReturn(testInfluencer);

    mockMvc.perform(get("/rest/user/{email}", "haha"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void updateUser_whenInvoked_return200() throws Exception {
    when(userService.updateUser(any(String.class), eq(testInfluencer))).thenReturn(testInfluencer);

    mockMvc.perform(put("/rest/user/{email}", "haha").content(String.valueOf(testInfluencer)))
            .andExpect(status().isOk());
  }

  @Test
  void deleteUser_whenInvoked_returns200() throws Exception {
    when(userService.deleteUser(any(String.class))).thenReturn("Success");

    mockMvc.perform(delete("/rest/user/{email}", testInfluencer.getEmail()))
            .andExpect(status().isOk());
    verify(userService, times(1)).deleteUser(any(String.class));
  }
}
