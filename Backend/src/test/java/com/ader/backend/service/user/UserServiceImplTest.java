package com.ader.backend.service.user;

import com.ader.backend.entity.Role;
import com.ader.backend.entity.Roles;
import com.ader.backend.entity.User;
import com.ader.backend.repository.RoleRepository;
import com.ader.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

  @Mock private UserRepository userRepository;
  @Mock private RoleRepository roleRepository;
  @Mock private BCryptPasswordEncoder passwordEncoder;

  @InjectMocks
  private UserServiceImpl userService;

  private User testInfluencer;
  private User testAdvertiser;
  private List<User> users;

  @BeforeEach
  void setUp() {
    userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder);

    testInfluencer = new User();
    testInfluencer.setId(1L);
    testInfluencer.setEmail("user@user.com");
    testInfluencer.setPassword("test");

    testAdvertiser = new User();
    testAdvertiser.setId(2L);
    testAdvertiser.setEmail("ad@ad.com");
    testAdvertiser.setPassword("test");
    testAdvertiser.setBrandName("somebrand");
    testAdvertiser.setBrandWebsite("somewebsite");

    users = new ArrayList<>();
  }

  @Test
  void loadByUsername_whenInvoked_returnUserDetails() {
    when(userRepository.findByEmail(any(String.class))).thenReturn(java.util.Optional.ofNullable(testInfluencer));

    UserDetails userDetails = userService.loadUserByUsername(testInfluencer.getEmail());

    assertThat(userDetails).isNotNull();
    assertThat(userDetails.getUsername()).isEqualTo(testInfluencer.getEmail());
  }

  @Test
  void loadByUsername_withWrongUser_throwUsernameNotFoundException() {
    when(userRepository.findByEmail(testAdvertiser.getEmail())).thenThrow(UsernameNotFoundException.class);
    assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(testAdvertiser.getEmail()));
  }

  @Test
  void getAllUsers_whenInvoked_returnAllUsers() {
    when(userRepository.findAll()).thenReturn(users);

    List<User> allUsers = userService.getAllUsers();
    assertThat(allUsers).isEmpty();
  }

  @Test
  void getUser_whenInvoked_returnUser() {
    users.add(testInfluencer);

    when(userRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(users.get(0)));

    User influencer = userService.getUser(testInfluencer.getId());
    assertThat(influencer).isNotNull();
    assertThat(influencer.getId()).isEqualTo(testInfluencer.getId());
  }

  @Test
  void getUser_withWrongId_throwResponseStatusException() {
    users.add(testAdvertiser);
    Long id = testInfluencer.getId();

    when(userRepository.findById(id)).thenReturn(users.stream().filter(user -> user.getId().equals(id)).findAny());

    assertThrows(ResponseStatusException.class, () -> userService.getUser(id));
  }

  @Test
  void register_whenInvoked_addAndReturnUser() {
    Role role = new Role();
    role.setName(Roles.ROLE_USER.name());

    when(userRepository.save(argThat(arg -> arg.equals(testInfluencer)))).thenAnswer(answer -> {
      users.add(testInfluencer);
      return users.stream().filter(user -> user.equals(testInfluencer)).findAny().orElse(null);
    });
    when(roleRepository.findByName(any(String.class))).thenReturn(Optional.of(role));

    User newUser = userService.register(testInfluencer, role.getName());

    assertThat(newUser).isNotNull();
    assertThat(newUser).isEqualTo(testInfluencer);
  }
}
