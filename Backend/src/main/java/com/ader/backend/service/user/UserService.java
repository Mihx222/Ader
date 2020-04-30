package com.ader.backend.service.user;

import com.ader.backend.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUser(String email);

    User getUser(Long id);

    User register(User user);

    User updateUser(String email, User user);

    String deleteUser(String email);

    boolean isAuthenticated(String email, Authentication authentication);

    Authentication getAuthentication();

    User getAuthenticatedUser();
}
