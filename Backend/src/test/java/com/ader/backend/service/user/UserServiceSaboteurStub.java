package com.ader.backend.service.user;

import com.ader.backend.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class UserServiceSaboteurStub implements UserService {

  @Override
  public List<User> getAllUsers() {
    return null;
  }

  @Override
  public User getUser(String email) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
  }

  @Override
  public User getUser(Long id) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
  }

  @Override
  public User register(User user, String role) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
  }

  @Override
  public User updateUser(String email, User user) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
  }

  @Override
  public String deleteUser(String email) {
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
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
