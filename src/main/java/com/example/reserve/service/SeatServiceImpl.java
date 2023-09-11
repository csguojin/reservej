package com.example.reserve.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reserve.dao.SeatDao;
import com.example.reserve.pojo.Seat;

@Service
public class SeatServiceImpl implements SeatService {
    @Autowired
    private SeatDao seatDao;

    public List<Seat> getAllSeatOfRoom(Integer roomID, Integer page, Integer pageSize) {
        return seatDao.getAllSeatOfRoom(roomID, new RowBounds((page - 1) * pageSize, pageSize));
    }

    public Seat createSeat(Seat newSeat) {
        seatDao.createSeat(newSeat);
        return seatDao.getSeatById(newSeat.getId());
    }

    public Seat getSeatById(Integer id) {
        return seatDao.getSeatById(id);
    }

    public Seat updateSeat(Seat newSeat) {
        seatDao.updateSeat(newSeat);
        return seatDao.getSeatById(newSeat.getId());
    }

    public void deleteSeatById(Integer id) {
        seatDao.deleteSeatById(id);
    }
}
