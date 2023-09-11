package com.example.reserve.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.reserve.service.RoomService;
import com.example.reserve.pojo.Room;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("")
    public List<Room> getAllRoom(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        if (page <= 0) {
            page = 1;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
        return roomService.getAllRooms(page, pageSize);
    }

    @PostMapping("")
    public Room createRoom(@RequestBody Room newRoom) {
        return roomService.createRoom(newRoom);
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Integer id) {
        return roomService.getRoomById(id);
    }

    @PutMapping("/{id}")
    public Room updateRoom(@PathVariable Integer id, @RequestBody Room newRoom) {
        newRoom.setId(id);
        return roomService.updateRoom(newRoom);
    }

    @DeleteMapping("/{id}")
    public void deleteRoomById(@PathVariable Integer id) {
        roomService.deleteRoomById(id);
    }
}
