package com.example.reserve.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.reserve.pojo.User;
import com.example.reserve.service.UserService;
import com.example.reserve.utils.JwtTokenProvider;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user = userService.createUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody User user) {
        User checkedUser = userService.checkUser(user.getUsername(), user.getPassword());
        if (checkedUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username or password error");
        }
        return ResponseEntity.ok(new HashMap<>(){{
            put("user", checkedUser);
            put("token", JwtTokenProvider.generateJwtToken(checkedUser.getId().toString()));
        }});
    }

    @PutMapping("/users/{userID}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userID, @RequestBody User newUser) {
        newUser.setId(userID);
        User user = userService.updateUser(newUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{userID}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userID) {
        userService.deleteUser(userID);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/users/{userID}")
    public ResponseEntity<?> getUserByID(@PathVariable Integer userID) {
        User user = userService.getUserByID(userID);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
