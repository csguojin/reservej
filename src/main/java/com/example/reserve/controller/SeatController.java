package com.example.reserve.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.reserve.pojo.Seat;
import com.example.reserve.service.SeatService;

@RestController
@RequestMapping("/api/v1/rooms/{roomID}/seats")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @GetMapping("")
    public List<Seat> getAllSeatOfRoom(
            @PathVariable Integer roomID,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize
    ) {
        if (page <= 0) {
            page = 1;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        return seatService.getAllSeatOfRoom(roomID, page, pageSize);
    }

    @PostMapping("")
    public Seat createSeat(
            @PathVariable Integer roomID,
            @RequestBody Seat newSeat) {
        newSeat.setRoomID(roomID);
        return seatService.createSeat(newSeat);
    }

    @GetMapping("/{id}")
    public Seat getSeatById(@PathVariable Integer id) {
        return seatService.getSeatById(id);
    }

    @PutMapping("/{id}")
    public Seat updateSeat(
            @PathVariable Integer roomID,
            @PathVariable Integer id, @RequestBody Seat newSeat
    ) {
        newSeat.setRoomID(roomID);
        newSeat.setId(id);
        return seatService.updateSeat(newSeat);
    }

    @DeleteMapping("/{id}")
    public void deleteSeatById(@PathVariable Integer id) {
        seatService.deleteSeatById(id);
    }
}
