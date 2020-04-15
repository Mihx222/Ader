package com.ader.backend.service.user;

import com.ader.backend.entity.Status;
import com.ader.backend.entity.role.Role;
import com.ader.backend.entity.role.Roles;
import com.ader.backend.entity.user.User;
import com.ader.backend.entity.user.UserDto;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.RoleRepository;
import com.ader.backend.repository.UserRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String NO_ROLE_WITH_NAME_MESSAGE = "No role with name: [";
    private static final String WAS_FOUND_MESSAGE = "] was found";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            String errorMessage = "Invalid email: [" + email + "]";
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        }

        List<SimpleGrantedAuthority> grantedAuthorities = getAuthorities(user);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities
        );
    }

    private List<SimpleGrantedAuthority> getAuthorities(User user) {
        List<Role> userRoles = user.getRoles();

        return userRoles.stream().map(
                role -> new SimpleGrantedAuthority(role.getName())
        ).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(UserDto.toDto(userRepository.findAll()));
    }

    @Override
    public ResponseEntity<Object> getUser(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            String errorMessage = "User with email: [" + email + "] not found!";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } else {
            return ResponseEntity.ok(UserDto.toDto(user));
        }
    }

    @Override
    public ResponseEntity<Object> getUser(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            String errorMessage = "User with id: [" + id + "] not found!";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @Override
    public ResponseEntity<Object> register(User user) {
        String errorMessage;

        // Handle password and email
        Pair<User, String> handledUserByPassAndEmail = handlePasswordAndEmail(user);
        if (handledUserByPassAndEmail.getRight() == null) {
            user = handledUserByPassAndEmail.getLeft();
        } else {
            log.error(handledUserByPassAndEmail.getRight());
            return ResponseEntity.badRequest().body(handledUserByPassAndEmail.getRight());
        }

        // Handle roles
        Pair<User, String> handledUserByRoles = handleRoles(user);
        if (handledUserByRoles.getRight() == null) {
            user = handledUserByRoles.getLeft();
        } else {
            log.error(handledUserByRoles.getRight());
            return ResponseEntity.badRequest().body(handledUserByRoles.getRight());
        }

        // Save new user
        try {
            userRepository.save(user);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            return ResponseEntity.unprocessableEntity().body(errorMessage);
        }

        UserDto newUser = UserDto.toDto(user);
        log.info("User [{}] created successfully!", newUser);
        return ResponseEntity.ok(newUser);
    }

    @Override
    public ResponseEntity<Object> updateUser(String email, User user) {
        Authentication currentAuthentication = getAuthentication();
        User authenticatedUser = userRepository.findByEmail(currentAuthentication.getName()).orElse(null);
        User userToUpdate = userRepository.findByEmail(email).orElse(null);
        Role roleAdmin = roleRepository.findByName(Roles.ROLE_ADMIN.toString()).orElse(null);
        String errorMessage;

        if (userToUpdate == null) {
            errorMessage = "User [" + email + "] does not exist!";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } else if (authenticatedUser == null) {
            errorMessage = "Authenticated user [" + currentAuthentication.getName() + "] was not found! " +
                    "This means that the user credentials have changed.";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } else if (authenticatedUser.getRoles().contains(roleAdmin) ||
                userToUpdate.getEmail().equals(authenticatedUser.getEmail())) {
            if (!authenticatedUser.getRoles().contains(roleAdmin) && !user.getRoles().isEmpty()) {
                errorMessage = "You do not have the rights to change roles of user: [" + userToUpdate.getEmail() + "]";
                log.error(errorMessage);
                return ResponseEntity.badRequest().body(errorMessage);
            } else {
                // Copy properties to new user
                BeanUtils.copyProperties(
                        user,
                        userToUpdate,
                        BeanHelper.getNullPropertyNames(user, true)
                );
            }
            return ResponseEntity.ok(UserDto.toDto(userToUpdate));
        } else {
            errorMessage = "You do not have the rights to update user with email [" + email + "]!";
            log.error(errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<Object> deleteUser(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        String errorMessage;

        if (user == null) {
            errorMessage = "Could not find user with email [" + email + "]!";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } else if (isAuthenticated(email, null)) {
            errorMessage = "You cannot delete the account you're currently logged in with!";
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } else {
            user.setStatus(Status.DELETED);
            return ResponseEntity.ok("Deleted user with email: [" + email + "]");
        }
    }

    @Override
    public boolean isAuthenticated(String email, Authentication authentication) {
        if (authentication == null) {
            return getAuthentication().getName().equals(email);
        } else {
            return authentication.getName().equals(email);
        }
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = getAuthentication();
        User authenticatedUser = userRepository.findByEmail(authentication.getName()).orElse(null);

        if (authenticatedUser == null) {
            log.error("Authenticated user [{}] does not exist in the database! " +
                            "This means that the user credentials have changed",
                    authentication.getName());
            return null;
        }

        return authenticatedUser;
    }

    protected boolean checkRoleNamesFromList(List<Role> roles) {
        for (Role role : roles) {
            if (role.getName() == null) return false;
        }
        return true;
    }

    protected boolean checkRoleIdsFromList(List<Role> roles) {
        for (Role role : roles) {
            if (role.getId() == null) return false;
        }
        return true;
    }

    protected Pair<User, String> handlePasswordAndEmail(@NonNull User user) {
        String errorMessage = null;

        if (user.getPassword() == null || user.getEmail() == null) {
            errorMessage = "Received user has no email or password set!";
            log.error(errorMessage);
            return Pair.of(null, errorMessage);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return Pair.of(user, errorMessage);
    }

    protected Pair<User, String> handleRoles(@NonNull User user) {
        String errorMessage = null;
        List<Role> userRoles = user.getRoles();
        boolean checkRoleNamesFromList = checkRoleNamesFromList(userRoles);
        boolean checkRoleIdsFromList = checkRoleIdsFromList(userRoles);

        if (userRoles.isEmpty()) {
            log.info("Role for new user is not set. Try auto-assigning role: [USER]");
            Role userRole = roleRepository.findByName(Roles.ROLE_USER.toString()).orElse(null);

            if (userRole == null) {
                errorMessage = NO_ROLE_WITH_NAME_MESSAGE + Roles.ROLE_USER.toString() + WAS_FOUND_MESSAGE;
                log.error(errorMessage);
                return Pair.of(null, errorMessage);
            }

            userRoles.add(userRole);
        } else {
            if (checkRoleNamesFromList) {
                for (int i = 0; i < userRoles.size(); i++) {
                    Role roleToAssign = roleRepository.findByName(userRoles.get(i).getName()).orElse(null);

                    if (roleToAssign == null) {
                        errorMessage = NO_ROLE_WITH_NAME_MESSAGE + userRoles.get(i).getName() + WAS_FOUND_MESSAGE;
                        log.error(errorMessage);
                        return Pair.of(null, errorMessage);
                    }

                    userRoles.set(i, roleToAssign);
                }
            } else if (checkRoleIdsFromList) {
                for (int i = 0; i < userRoles.size(); i++) {
                    Role roleToAssign = roleRepository.findById(userRoles.get(i).getId()).orElse(null);

                    if (roleToAssign == null) {
                        errorMessage = "No role with id: [" + userRoles.get(i).getId() + "] was found!";
                        log.error(errorMessage);
                        return Pair.of(null, errorMessage);
                    }

                    userRoles.set(i, roleToAssign);
                }
            } else {
                errorMessage = "No Id(s) or name(s) provided in new user roles!";
                log.error(errorMessage);
                return Pair.of(null, errorMessage);
            }
        }
        return Pair.of(user, errorMessage);
    }
}
