package com.ader.backend.rest;

import com.ader.backend.entity.user.User;
import com.ader.backend.entity.user.UserDto;
import com.ader.backend.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/user")
@CrossOrigin("http://localhost:4200")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{email}")
    public ResponseEntity<Object> getUser(@PathVariable String email) {
        return userService.getUser(email);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("{email}")
    public ResponseEntity<Object> updateUser(@PathVariable String email, @RequestBody User user) {
        return userService.updateUser(email, user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{email}")
    public ResponseEntity<Object> deleteUser(@PathVariable String email) {
        return userService.deleteUser(email);
    }
}
