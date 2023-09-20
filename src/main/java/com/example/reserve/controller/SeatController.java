package com.example.reserve.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.reserve.pojo.Seat;
import com.example.reserve.service.SeatService;

@RestController
@RequestMapping("/api/v1/rooms/{roomID}/seats")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @GetMapping("")
    public ResponseEntity<?> getAllSeatOfRoom(
            @PathVariable Integer roomID,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        List<Seat> seats = seatService.getAllSeatOfRoom(roomID, page, pageSize);
        return ResponseEntity.ok(seats);
    }

    @PostMapping("")
    public ResponseEntity<?> createSeat(@PathVariable Integer roomID, @RequestBody Seat newSeat) {
        newSeat.setRoomID(roomID);
        Seat seat = seatService.createSeat(newSeat);
        return ResponseEntity.ok(seat);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSeatById(@PathVariable Integer id) {
        Seat seat = seatService.getSeatById(id);
        return ResponseEntity.ok(seat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSeat(@PathVariable Integer roomID, @PathVariable Integer id, @RequestBody Seat newSeat) {
        newSeat.setRoomID(roomID);
        newSeat.setId(id);
        Seat seat = seatService.updateSeat(newSeat);
        return ResponseEntity.ok(seat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeatById(@PathVariable Integer id) {
        seatService.deleteSeatById(id);
        return ResponseEntity.ok("OK");
    }
}
