package com.example.reserve.service;

import java.util.Date;
import java.util.List;

import com.example.reserve.exception.ReservationException;
import com.example.reserve.pojo.Reservation;

public interface ReservationService {
    Reservation createResv(Reservation newResv);
    Reservation getResvByID(Integer id);
    List<Reservation> getResvsByUser(Integer userID);
    List<Reservation> getResvsByUserDate(Integer userID, Date date);
    List<Reservation> getResvsBySeat(Integer seatID);
    List<Reservation> getResvsBySeatDate(Integer seatID, Date date);
    Reservation signinResv(Integer resvID, Integer userID) throws ReservationException;
    Reservation signoutResv(Integer resvID, Integer userID) throws ReservationException;
    Reservation cancelResv(Integer resvID, Integer userID) throws ReservationException;
}
