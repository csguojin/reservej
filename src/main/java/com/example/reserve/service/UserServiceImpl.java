package com.example.reserve.service;

import java.util.List;
import java.util.Objects;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reserve.dao.UserDao;
import com.example.reserve.pojo.User;
import com.example.reserve.utils.JwtTokenProvider;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public List<User> getAllUsers() {
        return userDao.getAllUsers(new RowBounds(0, 10));
    }

    public User getUserByID(Integer userID) {
        return userDao.getUserById(userID);
    }

    public User createUser(User newUser) {
        userDao.createUser(newUser);
        return userDao.getUserById(newUser.getId());
    }

    public User checkUser(String username, String password) {
        User user = userDao.getUserByName(username);
        if(!Objects.equals(user.getPassword(), password)) {
            return null;
        }
        return user;
    }

    public User updateUser(User newUser) {
        userDao.updateUser(newUser);
        return userDao.getUserById(newUser.getId());
    }

    public void deleteUser(Integer userID) {
        userDao.deleteUserById(userID);
    }
}
