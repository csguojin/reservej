package com.example.reserve.service;

import java.util.List;

import com.example.reserve.pojo.Room;

public interface RoomService {
    List<Room> getAllRooms(Integer page, Integer pageSize);
    Room createRoom(Room newRoom);
    Room getRoomById(Integer id);
    Room updateRoom(Room newRoom);
    void deleteRoomById(Integer id);
}
