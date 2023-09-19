package com.example.reserve.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reserve.cache.Redis;
import com.example.reserve.dao.ReservationDao;
import com.example.reserve.exception.ReservationException;
import com.example.reserve.pojo.Reservation;
import com.example.reserve.utils.RandomStringGenerator;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationDao resvDao;

    @Autowired
    private Redis redis;

    public Reservation createResv(Reservation newResv) throws ReservationException {
        newResv.check();

        // user
        String lockUserKey = newResv.getRedisKeyLockUser();
        String lockUserValue = RandomStringGenerator.geneRandStr(16);
        boolean ok = redis.lock(lockUserKey, lockUserValue, 60);
        if (!ok) {
            throw new ReservationException("user cannot make multiple appointments at the same time");
        }

        // check user todo

        // seat
        String lockSeatKey = newResv.getRedisKeyLockSeat();
        String lockSeatValue = RandomStringGenerator.geneRandStr(16);
        ok = redis.lock(lockSeatKey, lockSeatValue, 60);
        if (!ok) {
            throw new ReservationException("seat cannot handle multiple appointments at the same time");
        }

        // check seat todo

        resvDao.createResv(newResv);

        Reservation resv = resvDao.getResvByID(newResv.getId());

        // update cache todo
        redis.unlock(lockSeatKey, lockSeatValue);
        redis.unlock(lockUserKey, lockUserValue);
        return resv;
    }

    public Reservation getResvByID(Integer id) {
        return resvDao.getResvByID(id);
    }

    public List<Reservation> getResvsByUser(Integer userID) {
        return resvDao.getResvsByUser(userID, new RowBounds(0, 10));
    }

    public List<Reservation> getResvsByUserDate(Integer userID, Date date) {
        return resvDao.getResvsByUserDate(userID, date);
    }

    public List<Reservation> getResvsBySeat(Integer seatID) {
        return resvDao.getResvsBySeat(seatID, new RowBounds(0, 10));
    }

    public List<Reservation> getResvsBySeatDate(Integer seatID, Date date) {
        return resvDao.getResvsBySeatDate(seatID, date);
    }

    public Reservation signinResv(Integer resvID, Integer userID) throws ReservationException {
        Reservation resv = resvDao.getResvByID(resvID);
        if (resv == null) {
            throw new ReservationException("not found reservation");
        }

        if (resv.getStatus() != 0) {
            throw new ReservationException("reservation has been cancel");
        }

        if (resv.getSigninTime() != null) {
            throw new ReservationException("repeat signin");
        }

        Date now = new Date();

        Calendar nowPlus15Minutes = Calendar.getInstance();
        nowPlus15Minutes.setTime(now);
        nowPlus15Minutes.add(Calendar.MINUTE, 15);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(resv.getStartTime());

        if (nowPlus15Minutes.before(startCalendar)) {
            throw new ReservationException("sign in too early");
        }

        Calendar startPlus15Minutes = Calendar.getInstance();
        startPlus15Minutes.setTime(resv.getStartTime());
        startPlus15Minutes.add(Calendar.MINUTE, 15);

        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(now);

        if (startPlus15Minutes.before(nowCalendar)) {
            throw new ReservationException("sign in too late");
        }

        resv.setSigninTime(now);

        resvDao.updateResv(resv);

        return resvDao.getResvByID(resvID);
    }

    public Reservation signoutResv(Integer resvID, Integer userID) throws ReservationException {
        Reservation resv = resvDao.getResvByID(resvID);
        if (resv == null) {
            throw new ReservationException("not found reservation");
        }

        if (resv.getStatus() != 0) {
            throw new ReservationException("reservation has been cancel");
        }

        if (resv.getSignoutTime() != null) {
            throw new ReservationException("repeat signout");
        }

        Date now = new Date();
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTime(now);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(resv.getEndTime());

        if (nowCalendar.after(endCalendar)) {
            throw new ReservationException("the time is up, it ends automatically");
        }

        resv.setSignoutTime(now);

        resvDao.updateResv(resv);

        return resvDao.getResvByID(resvID);
    }

    public Reservation cancelResv(Integer resvID, Integer userID) throws ReservationException {
        Reservation resv = resvDao.getResvByID(resvID);
        if (resv == null) {
            throw new ReservationException("not found reservation");
        }

        if (resv.getStatus() != 0) {
            throw new ReservationException("reservation has been cancel");
        }

        resv.setStatus(1);
        resvDao.updateResv(resv);

        return resvDao.getResvByID(resvID);
    }
}
