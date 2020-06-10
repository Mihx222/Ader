package com.ader.backend.service.user;

import com.ader.backend.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public class UserServiceMock implements UserService {

  private List<User> fakeRepository;
  private int numberOfInvocations;

  public int getNumberOfInvocations() {
    return numberOfInvocations;
  }

  public UserServiceMock(List<User> fakeRepository, int numberOfInvocations) {
    this.fakeRepository = fakeRepository;
    this.numberOfInvocations = numberOfInvocations;
  }

  @Override
  public List<User> getAllUsers() {
    return fakeRepository;
  }

  @Override
  public User getUser(String email) {
    return fakeRepository.stream().filter(user -> user.getEmail().equals(email)).findFirst().get();
  }

  @Override
  public User getUser(Long id) {
    return fakeRepository.stream().filter(user -> user.getId().equals(id)).findFirst().get();
  }

  @Override
  public User register(User user, String role) {
    fakeRepository.add(user);
    numberOfInvocations++;
    return user;
  }

  @Override
  public User updateUser(String email, User user) {
    fakeRepository.set(fakeRepository.indexOf(user), user);
    return user;
  }

  @Override
  public String deleteUser(String email) {
    fakeRepository.removeIf(user -> user.getEmail().equals(email));
    return "Dummy";
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
