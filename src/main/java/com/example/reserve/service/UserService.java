package com.example.reserve.service;

import java.util.List;

import com.example.reserve.pojo.User;

public interface UserService {
    List<User> getAllUsers();
    User getUserByID(Integer userID);
    User createUser(User newUser);
    User checkUser(String username, String password);
    User updateUser(User newUser);
    void deleteUser(Integer userID);
}
