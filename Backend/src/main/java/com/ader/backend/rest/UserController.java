package com.ader.backend.rest;

import com.ader.backend.entity.User;
import com.ader.backend.rest.dto.UserDto;
import com.ader.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        log.info("Requested all users");
        return ResponseEntity.ok(UserDto.toDto(userService.getAllUsers()));
    }

    @GetMapping("{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable String email) {
        log.info("Requested user with email: [{}]", email);
        return ResponseEntity.ok(UserDto.toDto(userService.getUser(email)));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("{email}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String email, @RequestBody User user) {
        log.info("Requested updating user with email: [{}]", email);
        return ResponseEntity.ok(UserDto.toDto(userService.updateUser(email, user)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        log.info("Requested deleting user with email: [{}]", email);
        return ResponseEntity.ok(userService.deleteUser(email));
    }
}
