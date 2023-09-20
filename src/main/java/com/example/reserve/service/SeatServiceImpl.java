package com.example.reserve.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reserve.cache.Redis;
import com.example.reserve.dao.SeatDao;
import com.example.reserve.pojo.Seat;

@Service
public class SeatServiceImpl implements SeatService {
    @Autowired
    private SeatDao seatDao;
    @Autowired
    private Redis redis;

    public Seat createSeat(Seat newSeat) {
        seatDao.createSeat(newSeat);
        String redisKey = newSeat.buildRedisKey();
        Seat seat = seatDao.getSeatById(newSeat.getId());
        redis.set(redisKey, seat);
        return seat;
    }

    public Seat getSeatById(Integer id) {
        String redisKey = Seat.buildRedisKey(id);
        Seat seat = redis.get(redisKey, Seat.class);
        if (seat != null) {
            return seat;
        }
        seat = seatDao.getSeatById(id);
        redis.set(redisKey, seat);
        return seat;
    }

    public Seat updateSeat(Seat newSeat) {
        String redisKey = newSeat.buildRedisKey();
        redis.del(redisKey);
        seatDao.updateSeat(newSeat);
        Seat seat = seatDao.getSeatById(newSeat.getId());
        redis.set(redisKey, seat);
        return seat;
    }

    public void deleteSeatById(Integer id) {
        String redisKey = Seat.buildRedisKey(id);
        redis.del(redisKey);
        seatDao.deleteSeatById(id);
    }

    public List<Seat> getAllSeatOfRoom(Integer roomID, Integer page, Integer pageSize) {
        return seatDao.getAllSeatOfRoom(roomID, new RowBounds((page - 1) * pageSize, pageSize));
    }
}
