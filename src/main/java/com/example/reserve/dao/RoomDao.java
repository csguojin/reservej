package com.example.reserve.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.reserve.pojo.Room;

@Mapper
public interface RoomDao {
    Room getRoomById(@Param("id") Integer id);
}
