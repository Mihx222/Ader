package com.ader.backend.rest;

import com.ader.backend.entity.User;
import com.ader.backend.rest.dto.UserDto;
import com.ader.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<UserDto> register(@RequestBody User user) {
        log.info("New registration request with email: [{}]", user.getEmail());
        return ResponseEntity.ok(UserDto.toDto(userService.register(user)));
    }
}
