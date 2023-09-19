package com.example.reserve.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reserve.cache.Redis;
import com.example.reserve.dao.RoomDao;
import com.example.reserve.pojo.Room;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomDao roomDao;

    @Autowired
    private Redis redis;

    public List<Room> getAllRooms(Integer page, Integer pageSize) {
        return roomDao.getAllRooms(new RowBounds((page - 1) * pageSize, pageSize));
    }

    public Room createRoom(Room newRoom) {
        roomDao.createRoom(newRoom);
        String redisKey = newRoom.getRedisKey();
        Room room = roomDao.getRoomById(newRoom.getId());
        redis.set(redisKey, room);
        return room;
    }

    public Room getRoomById(Integer id) {
        String redisKey = Room.getRedisKey(id);
        Room room = (Room) redis.getObj(redisKey);
        if (room != null) {
            return room;
        }
        room = roomDao.getRoomById(id);
        redis.set(redisKey, room);
        return room;
    }

    public Room updateRoom(Room newRoom) {
        String redisKey = newRoom.getRedisKey();
        redis.deleteKey(redisKey);
        roomDao.updateRoom(newRoom);
        Room room = roomDao.getRoomById(newRoom.getId());
        redis.set(redisKey, room);
        return room;
    }

    public void deleteRoomById(Integer id) {
        String redisKey = Room.getRedisKey(id);
        redis.deleteKey(redisKey);
        roomDao.deleteRoomById(id);
    }
}
