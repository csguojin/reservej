package com.example.reserve.pojo;

import java.util.Calendar;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.reserve.exception.ReservationException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Integer id;
    private Integer userID;
    private Integer seatID;
    private Date createTime;
    private Date startTime;
    private Date endTime;
    private Date signinTime;
    private Date signoutTime;
    private Integer status;

    public String getRedisKeyLockUser() throws ReservationException {
        if (this.getUserID() <= 0) {
            throw new ReservationException("invalid user");
        }
        return "lock:resv:user:" + this.getUserID().toString();
    }
    public String getRedisKeyLockSeat() throws ReservationException {
        if (this.getSeatID() <= 0) {
            throw new ReservationException("invalid seat");
        }
        return "lock:resv:user:" + this.getSeatID().toString();
    }

    public String getRedisKey() {
        return "resv:"+this.getId().toString();
    }

    public Integer[] getRedisTimeBits() {
        if (!this.getEndTime().after(this.getStartTime())) {
            return new Integer[]{-1, -1};
        }

        long startTimeMillis = this.getStartTime().getTime();
        long endTimeMillis = this.getEndTime().getTime();

        int startHour = (int) (startTimeMillis / (60 * 60 * 1000)) % 24;
        int startMin = (int) ((startTimeMillis / (60 * 1000)) % 60);
        int startBit = (startHour * 60 + startMin) / 5;

        int endHour = (int) (endTimeMillis / (60 * 60 * 1000)) % 24;
        int endMin = (int) ((endTimeMillis / (60 * 1000)) % 60);
        int endSec = (int) ((endTimeMillis / 1000) % 60);
        int endMinutes = endHour * 60 + endMin;
        if (endMin % 5 == 0 && endSec == 0) {
            endMinutes--;
        }
        int endBit = endMinutes / 5;

        return new Integer[]{startBit, endBit};
    }


    public boolean check() throws ReservationException {
        if (this.getUserID() <= 0) {
            throw new ReservationException("invalid user");
        }
        if (this.getSeatID() <= 0) {
            throw new ReservationException("invalid seat");
        }
        if (!this.getEndTime().after(this.getStartTime())) {
            throw new ReservationException("start time must be before end time");
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(this.getStartTime());
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(this.getEndTime());
        if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)
                || cal1.get(Calendar.DAY_OF_YEAR) != cal2.get(Calendar.DAY_OF_YEAR)) {
            throw new ReservationException("start time and end time must be on the same day");
        }

        return true;
    }
}


