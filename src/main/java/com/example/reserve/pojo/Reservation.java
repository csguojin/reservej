package com.example.reserve.pojo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
