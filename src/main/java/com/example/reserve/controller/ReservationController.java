package com.example.reserve.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.reserve.exception.ReservationException;
import com.example.reserve.pojo.Reservation;
import com.example.reserve.service.ReservationService;

@RestController
@RequestMapping("/api/v1/users/{userID}/resvs")
public class ReservationController {
    @Autowired
    private ReservationService resvService;

    @GetMapping("")
    public List<Reservation> getAllResvsByUser(@PathVariable Integer userID) {
        return resvService.getResvsByUser(userID);
    }

    @PostMapping("")
    public ResponseEntity<?> createResv(
            @PathVariable Integer userID,
            @RequestBody Reservation newResv
    ) {
        try {
            newResv.setUserID(userID);
            Reservation result = resvService.createResv(newResv);
            return ResponseEntity.ok(result);
        } catch (ReservationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{ResvID}")
    public Reservation getResv(
            @PathVariable Integer userID,
            @PathVariable Integer resvID
    ) {
        // todo check user
        return resvService.getResvByID(resvID);
    }

    @PostMapping("/{resvID}/signin")
    public ResponseEntity<?> signinResv(
            @PathVariable Integer userID,
            @PathVariable Integer resvID
    ) {
        try {
            Reservation result = resvService.signinResv(resvID, userID);
            return ResponseEntity.ok(result);
        } catch (ReservationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{resvID}/signout")
    public ResponseEntity<?> signoutResv(
            @PathVariable Integer userID,
            @PathVariable Integer resvID
    ) {
        try {
            Reservation result = resvService.signoutResv(resvID, userID);
            return ResponseEntity.ok(result);
        } catch (ReservationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{resvID}/cancel")
    public ResponseEntity<?> cancelResv(
            @PathVariable Integer userID,
            @PathVariable Integer resvID
    ) {
        try {
            Reservation result = resvService.cancelResv(resvID, userID);
            return ResponseEntity.ok(result);
        } catch (ReservationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

