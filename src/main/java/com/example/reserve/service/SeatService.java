package com.example.reserve.service;

import java.util.List;

import com.example.reserve.pojo.Seat;

public interface SeatService {
    List<Seat> getAllSeatOfRoom(Integer roomID, Integer page, Integer pageSize);
    Seat createSeat(Seat newSeat);
    Seat getSeatById(Integer id);
    Seat updateSeat(Seat newSeat);
    void deleteSeatById(Integer id);
}
