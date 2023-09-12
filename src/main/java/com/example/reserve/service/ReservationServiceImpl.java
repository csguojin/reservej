package com.example.reserve.service;

import java.util.Date;
import java.util.List;
import java.util.Calendar;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reserve.dao.ReservationDao;
import com.example.reserve.pojo.Reservation;
import com.example.reserve.exception.ReservationException;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationDao resvDao;

    public Reservation createResv(Reservation newResv) {
        // todo
        resvDao.createResv(newResv);
        return resvDao.getResvByID(newResv.getId());
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
