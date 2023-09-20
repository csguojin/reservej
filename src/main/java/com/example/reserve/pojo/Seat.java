package com.example.reserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    private Integer id;
    private Integer roomID;
    private String name;
    private String description;
    private Integer status;

    public String buildRedisKey() {
        return "seat:"+this.getId().toString();
    }

    public static String buildRedisKey(Integer seatID) {
        return "seat:"+seatID.toString();
    }
}
