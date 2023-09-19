package com.example.reserve.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reserve.pojo.User;
import com.example.reserve.service.UserService;
import com.example.reserve.utils.JwtTokenProvider;

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
