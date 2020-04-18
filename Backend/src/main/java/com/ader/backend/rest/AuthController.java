package com.ader.backend.rest;

import com.ader.backend.entity.user.User;
import com.ader.backend.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/auth")
@Slf4j
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        log.info("New registration request with email: [{}]", user.getEmail());
        return userService.register(user);
    }
}
