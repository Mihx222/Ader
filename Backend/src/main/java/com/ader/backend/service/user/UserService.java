package com.ader.backend.service.user;

import com.ader.backend.entity.user.User;
import com.ader.backend.entity.user.UserDto;
import liquibase.exception.DatabaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    ResponseEntity<List<UserDto>> getAllUsers();

    ResponseEntity<UserDto> getUserByEmail(String email);

    ResponseEntity<User> getUserById(Long id);

    ResponseEntity<UserDto> register(User user) throws DatabaseException;

    ResponseEntity<UserDto> updateUser(String email, User user);

    ResponseEntity<String> deleteUser(String email);

    boolean isAuthenticated(String email, Authentication authentication);

    Authentication getAuthentication();

    User getAuthenticatedUser();
}
