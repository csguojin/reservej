package com.example.reserve.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.reserve.service.UserService;
import com.example.reserve.utils.JwtTokenProvider;
import com.example.reserve.pojo.User;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(
            @RequestBody User user
    ) {
        return userService.createUser(user);
    }

    @PostMapping("/signin")
    public String signin(
            @RequestBody User user
    ) {
        User checkedUser = userService.checkUser(user.getUsername(), user.getPassword());
        if (checkedUser == null) {
            return "username or password error";
        }
        return JwtTokenProvider.generateJwtToken(checkedUser.getId().toString());
    }
}
