package com.ader.backend.rest;

import com.ader.backend.entity.user.User;
import com.ader.backend.entity.user.UserDto;
import com.ader.backend.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/user")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        log.info("Requested all users");
        return userService.getAllUsers();
    }

    @GetMapping("{email}")
    public ResponseEntity<Object> getUser(@PathVariable String email) {
        log.info("Requested user with email: [{}]", email);
        return userService.getUser(email);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("{email}")
    public ResponseEntity<Object> updateUser(@PathVariable String email, @RequestBody User user) {
        log.info("Requested updating user with email: [{}]", email);
        return userService.updateUser(email, user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{email}")
    public ResponseEntity<Object> deleteUser(@PathVariable String email) {
        log.info("Requested deleting user with email: [{}]", email);
        return userService.deleteUser(email);
    }
}
