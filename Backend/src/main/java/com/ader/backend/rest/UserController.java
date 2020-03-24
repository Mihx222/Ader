package com.ader.backend.rest;

import com.ader.backend.entity.User;
import com.ader.backend.entity.dto.UserDto;
import com.ader.backend.service.UserService;
import org.springframework.http.ResponseEntity;
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
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PutMapping("{username}")
    public UserDto updateUser(@PathVariable String username, @RequestBody User user) {
        return userService.updateUser(username, user);
    }

    @DeleteMapping("{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }
}
