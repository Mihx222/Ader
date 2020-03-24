package com.ader.backend.service;

import com.ader.backend.entity.User;
import com.ader.backend.entity.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    ResponseEntity<UserDto> getUserByUsername(String username);

    void register(User user);

    UserDto updateUser(String username, User user);

    void deleteUser(String username);

    boolean isAuthenticated(String username);

    Authentication getAuthentication();
}
