package com.example.reserve.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.example.reserve.pojo.Reservation;

@Mapper
public interface ReservationDao {
    Reservation createResv(Reservation newResv);
    Reservation getResvByID(Integer id);
    List<Reservation> getResvsByUser(Integer userID, RowBounds rowBounds);
    List<Reservation> getResvsByUserDate(Integer userID, Date date);
    List<Reservation> getResvsBySeat(Integer seatID, RowBounds rowBounds);
    List<Reservation> getResvsBySeatDate(Integer seatID, Date date);
    void updateResv(Reservation newResv);
}
