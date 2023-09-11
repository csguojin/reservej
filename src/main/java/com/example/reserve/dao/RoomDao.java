package com.example.reserve.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.reserve.pojo.Room;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface RoomDao {
    List<Room> getAllRooms(RowBounds rowBounds);
    void createRoom(Room newRoom);
    Room getRoomById(@Param("id") Integer id);
    void updateRoom(Room newRoom);
    void deleteRoomById(@Param("id") Integer id);
}
