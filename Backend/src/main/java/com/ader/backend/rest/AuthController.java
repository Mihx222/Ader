package com.ader.backend.rest;

import com.ader.backend.entity.User;
import com.ader.backend.rest.dto.UserDto;
import com.ader.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

  private UserService userService;

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("register")
  public ResponseEntity<UserDto> register(@RequestBody User user, @RequestParam String role) {
    log.info("New registration request with email: [{}]", user.getEmail());
    return ResponseEntity.ok(UserDto.toDto(userService.register(user, role)));
  }
}
