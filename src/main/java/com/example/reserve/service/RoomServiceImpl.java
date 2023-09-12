package com.example.reserve.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reserve.dao.RoomDao;
import com.example.reserve.pojo.Room;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomDao roomDao;

    public List<Room> getAllRooms(Integer page, Integer pageSize) {
        return roomDao.getAllRooms(new RowBounds((page - 1) * pageSize, pageSize));
    }

    public Room createRoom(Room newRoom) {
        roomDao.createRoom(newRoom);
        return roomDao.getRoomById(newRoom.getId());
    }

    public Room getRoomById(Integer id) {
        return roomDao.getRoomById(id);
    }

    public Room updateRoom(Room newRoom) {
        roomDao.updateRoom(newRoom);
        return roomDao.getRoomById(newRoom.getId());
    }

    public void deleteRoomById(Integer id) {
        roomDao.deleteRoomById(id);
    }
}
