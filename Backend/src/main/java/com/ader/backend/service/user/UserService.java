package com.ader.backend.service.user;

import com.ader.backend.entity.user.User;
import com.ader.backend.entity.user.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    ResponseEntity<List<UserDto>> getAllUsers();

    ResponseEntity<Object> getUser(String email);

    ResponseEntity<Object> getUser(Long id);

    ResponseEntity<Object> register(User user);

    ResponseEntity<Object> updateUser(String email, User user);

    ResponseEntity<Object> deleteUser(String email);

    boolean isAuthenticated(String email, Authentication authentication);

    Authentication getAuthentication();

    User getAuthenticatedUser();
}
