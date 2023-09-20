package com.example.reserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private Integer id;
    private String name;
    private Integer capacity;
    private String openTime;
    private String closeTime;
    private String location;
    private String description;
    private Integer status;

    public String buildRedisKey() {
        return "room:"+this.getId().toString();
    }

    public static String buildRedisKey(Integer roomID) {
        return "room:"+roomID.toString();
    }
}