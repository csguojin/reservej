package com.example.reserve.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.reserve.pojo.Room;
import com.example.reserve.service.RoomService;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("")
    public ResponseEntity<?> getAllRoom(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        List<Room> rooms = roomService.getAllRooms(page, pageSize);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("")
    public ResponseEntity<?> createRoom(@RequestBody Room newRoom) {
        Room room = roomService.createRoom(newRoom);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable Integer id) {
        Room room =  roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Integer id, @RequestBody Room newRoom) {
        newRoom.setId(id);
        Room room = roomService.updateRoom(newRoom);
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoomById(@PathVariable Integer id) {
        roomService.deleteRoomById(id);
        return ResponseEntity.ok("OK");
    }
}
