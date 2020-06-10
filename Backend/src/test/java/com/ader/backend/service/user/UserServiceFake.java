package com.ader.backend.service.user;

import com.ader.backend.entity.User;
import org.assertj.core.util.Lists;
import org.springframework.security.core.Authentication;

import java.util.List;

public class UserServiceFake implements UserService {

  @Override
  public List<User> getAllUsers() {
    return Lists.emptyList();
  }

  @Override
  public User getUser(String email) {
    return new User();
  }

  @Override
  public User getUser(Long id) {
    return new User();
  }

  @Override
  public User register(User user, String role) {
    return user;
  }

  @Override
  public User updateUser(String email, User user) {
    return user;
  }

  @Override
  public String deleteUser(String email) {
    return "Deleted";
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
