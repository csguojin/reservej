package com.example.reserve.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LuaScriptConfig {
    static private String createReservation;
    static private String cancelReservation;

    @Value("${file.lua.reservation.create}")
    public void setCreateReservation(String createReservation) {
        LuaScriptConfig.createReservation = createReservation;
    }
    static public String getCreateReservation() {
        return LuaScriptConfig.createReservation;
    }

    @Value("${file.lua.reservation.cancel}")
    public void setCancelReservation(String cancelReservation) {
        LuaScriptConfig.cancelReservation = cancelReservation;
    }
    static public String getCancelReservation() {
        return LuaScriptConfig.cancelReservation;
    }
}
