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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

  @Mock
  private Authentication authentication;
  @Mock
  private SecurityContext securityContext;
  @Mock
  private UserRepository userRepository;
  @Mock
  private RoleRepository roleRepository;
  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  @InjectMocks
  private UserServiceImpl userService;

  private User testInfluencer;
  private User testAdvertiser;
  private List<User> users;
  private Role role;
  private Authentication mockAuth;

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

    role = new Role();
    role.setName(Roles.ROLE_USER.name());

    mockAuth = new OAuth2Authentication(
            new OAuth2Request(
                    Collections.emptyMap(),
                    "mockClient",
                    Collections.emptyList(),
                    true,
                    new HashSet<>(Arrays.asList("read", "write")),
                    Collections.emptySet(),
                    null,
                    Collections.emptySet(),
                    Collections.emptyMap()),
            new UsernamePasswordAuthenticationToken(
                    testInfluencer.getEmail(),
                    "N/A",
                    Collections.singletonList(new SimpleGrantedAuthority(role.getName())))
    );
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
  void register_whenInvoked_callSaveOnce() {
    when(roleRepository.findByName(any(String.class))).thenReturn(Optional.of(role));

    userService.register(testInfluencer, role.getName());

    verify(userRepository, times(1)).save(testInfluencer);
  }

  @Test
  void updateUser_whenInvoked_doesNotThrowAnyException() {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(mockAuth);
    when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(testInfluencer));
    when(roleRepository.findByName(any(String.class))).thenReturn(Optional.ofNullable(role));

    assertThatCode(() -> userService.updateUser(testInfluencer.getEmail(), testInfluencer)).doesNotThrowAnyException();
  }
}
