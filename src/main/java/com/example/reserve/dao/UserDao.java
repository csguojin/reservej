package com.example.reserve.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.example.reserve.pojo.User;

@Mapper
public interface UserDao {
    List<User> getAllUsers(RowBounds rowBounds);
    void createUser(User newUser);
    User getUserById(Integer id);
    User getUserByName(String username);
    void updateUser(User newUser);
    void deleteUserById(Integer id);
}
