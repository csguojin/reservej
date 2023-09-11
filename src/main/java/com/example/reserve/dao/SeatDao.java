package com.example.reserve.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.example.reserve.pojo.Seat;

import java.util.List;

@Mapper
public interface SeatDao {
    List<Seat> getAllSeatOfRoom(Integer roomID, RowBounds rowBounds);
    void createSeat(Seat newSeat);
    Seat getSeatById(Integer id);
    void updateSeat(Seat newSeat);
    void deleteSeatById(Integer id);
}
