package com.ader.backend.rest;

import com.ader.backend.entity.user.User;
import com.ader.backend.entity.user.UserDto;
import com.ader.backend.service.user.UserService;
import liquibase.exception.DatabaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<UserDto> register(@RequestBody User user) throws DatabaseException {
        return userService.register(user);
    }
}
