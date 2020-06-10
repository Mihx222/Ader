package com.ader.backend.service.user;

import com.ader.backend.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public class UserServiceDummy implements UserService {

  @Override
  public List<User> getAllUsers() {
    return null;
  }

  @Override
  public User getUser(String email) {
    return null;
  }

  @Override
  public User getUser(Long id) {
    return null;
  }

  @Override
  public User register(User user, String role) {
    return null;
  }

  @Override
  public User updateUser(String email, User user) {
    return null;
  }

  @Override
  public String deleteUser(String email) {
    return null;
  }

  @Override
  public boolean isAuthenticated(String email, Authentication authentication) {
    return false;
  }

  @Override
  public Authentication getAuthentication() {
    return null;
  }

  @Override
  public User getAuthenticatedUser() {
    return null;
  }
}
