package com.example.reserve.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.example.reserve.pojo.Room;

@Mapper
public interface RoomDao {
    List<Room> getAllRooms(RowBounds rowBounds);
    void createRoom(Room newRoom);
    Room getRoomById(Integer id);
    void updateRoom(Room newRoom);
    void deleteRoomById(Integer id);
}
