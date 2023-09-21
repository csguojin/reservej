package com.example.reserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;

    public String buildRedisKey() {
        return "user:"+this.getId().toString();
    }

    public static String buildRedisKey(Integer userID) {
        return "user:"+userID.toString();
    }
}
