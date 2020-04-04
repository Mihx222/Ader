package com.ader.backend.service.impl;

import com.ader.backend.entity.Role;
import com.ader.backend.entity.Roles;
import com.ader.backend.entity.Status;
import com.ader.backend.entity.User;
import com.ader.backend.entity.dto.UserDto;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.RoleRepository;
import com.ader.backend.repository.UserRepository;
import com.ader.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            log.error("Invalid username!");
            throw new UsernameNotFoundException("Invalid username!");
        }

        List<SimpleGrantedAuthority> grantedAuthorities = getAuthorities(user);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
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
    public List<UserDto> getAllUsers() {
        return UserDto.toDto(userRepository.findAll());
    }

    @Override
    public ResponseEntity<UserDto> getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        if (isAuthenticated(username)) {
            return ResponseEntity.ok(Objects.requireNonNull(UserDto.toDto(user)));
        } else {
            User authenticatedUser = userRepository.findByUsername(getAuthentication().getName());

            if (authenticatedUser.getRoles().contains(roleRepository.findByName(Roles.ROLE_ADMIN.toString()))) {
                return ResponseEntity.ok(Objects.requireNonNull(UserDto.toDto(user)));
            } else {
                throw new AccessDeniedException(
                        "The user you are trying to access is not the one currently authenticated!");
            }
        }
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            log.error("User with id: [{}] not found!", id);
            return null;
        } else {
            return user;
        }
    }

    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles().isEmpty()) {
            user.getRoles().add(roleRepository.findByName(Roles.ROLE_USER.toString()));
        }

        return userRepository.save(user);
    }

    @Override
    public UserDto updateUser(String username, User user) {
        User authenticatedUser = userRepository.findByUsername(getAuthentication().getName());

        if (isAuthenticated(username) ||
                authenticatedUser.getRoles().contains(roleRepository.findByName(Roles.ROLE_ADMIN.toString()))) {
            User updatedUser = null;
            try {
                updatedUser = userRepository.findByUsername(username);
            } catch (UsernameNotFoundException ex) {
                ex.printStackTrace();
            }

            assert updatedUser != null;
            BeanUtils.copyProperties(user, updatedUser, BeanHelper.getNullPropertyNames(user, true));

            return UserDto.toDto(updatedUser);
        } else {
            throw new AccessDeniedException(
                    "The user you are trying to update is not the one currently authenticated!");
        }
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username);

        if (isAuthenticated(username)) {
            throw new RequestRejectedException(
                    "You cannot delete the account you're currently logged in with!"
            );
        } else if (user != null) {
            user.setStatus(Status.DELETED);
        } else {
            throw new UsernameNotFoundException(
                    "Could not find the user with username: " + username
            );
        }
    }

    @Override
    public boolean isAuthenticated(String username) {
        return getAuthentication().getName().equals(username);
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User getAuthenticatedUser() {
        return userRepository.findByUsername(getAuthentication().getName());
    }
}
