package com.ader.backend.service.user;

import com.ader.backend.entity.Status;
import com.ader.backend.entity.role.Role;
import com.ader.backend.entity.role.Roles;
import com.ader.backend.entity.user.User;
import com.ader.backend.entity.user.UserDto;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.RoleRepository;
import com.ader.backend.repository.UserRepository;
import liquibase.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            log.error("Invalid email!");
            throw new UsernameNotFoundException("Invalid email!");
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
    public ResponseEntity<UserDto> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            String errorMessage = "User with email: [" + email + "] not found!";
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        } else {
            return ResponseEntity.ok(UserDto.toDto(user));
        }
    }

    @Override
    public ResponseEntity<User> getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            String errorMessage = "User with id: [" + id + "] not found!";
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @Override
    public ResponseEntity<UserDto> register(User user) throws DatabaseException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles().isEmpty()) {
            user.getRoles().add(roleRepository.findByName(Roles.ROLE_USER.toString()));
        }

        try {
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e);
        }

        log.info("User [" + user.toString() + "] created successfully!");
        return ResponseEntity.ok(UserDto.toDto(user));
    }

    @Override
    public ResponseEntity<UserDto> updateUser(String email, User user) {
        Authentication authentication = getAuthentication();
        User authenticatedUser = userRepository.findByEmail(authentication.getName()).orElse(null);
        String errorMessage;

        if (authenticatedUser == null) {
            errorMessage = "User [" + authentication.getName() + "] does not exist!";
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        } else if (authenticatedUser.getRoles().contains(roleRepository.findByName(Roles.ROLE_ADMIN.toString()))) {
            User updatedUser = userRepository.findByEmail(email).orElse(null);

            if (updatedUser == null) {
                errorMessage = "User with email [" + email + "] does not exist!";
                log.error(errorMessage);
                throw new UsernameNotFoundException(errorMessage);
            } else {
                BeanUtils.copyProperties(user, updatedUser, BeanHelper.getNullPropertyNames(user, true));
                return ResponseEntity.ok(UserDto.toDto(updatedUser));
            }
        } else {
            errorMessage = "You do not have the rights to update user with email [" + email + "]!";
            log.error(errorMessage);
            throw new UnauthorizedUserException(errorMessage);
        }
    }

    @Override
    public ResponseEntity<String> deleteUser(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        String errorMessage;

        if (user == null) {
            errorMessage = "Could not find user with email [" + email + "]!";
            log.error(errorMessage);
            throw new UsernameNotFoundException(errorMessage);
        } else if (isAuthenticated(email, null)) {
            errorMessage = "You cannot delete the account you're currently logged in with!";
            log.error(errorMessage);
            throw new UserDeniedAuthorizationException(errorMessage);
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
            log.error("Authenticated user [" + authentication.getName() + "] does not exist in the database! This should not be possible!");
            return null;
        }

        return authenticatedUser;
    }
}
