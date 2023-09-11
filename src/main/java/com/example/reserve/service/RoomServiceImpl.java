package com.example.reserve.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reserve.dao.RoomDao;
import com.example.reserve.pojo.Room;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomDao roomDao;

    public Room getRoomById(Integer id) {
        return  roomDao.getRoomById(id);

    }
}
