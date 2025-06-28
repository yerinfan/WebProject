package kr.ac.kopo.controller;

// src/main/java/kr/ac/kopo/controller/AdminRoomController.java

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.ac.kopo.repository.ChatRoomRepository;

@Controller
@RequestMapping("/admin/rooms")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoomController {

    private final ChatRoomRepository roomRepo;

    public AdminRoomController(ChatRoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomRepo.findAll());
        return "admin/rooms";
    }

    @PostMapping("/{id}/delete")
    public String deleteRoom(@PathVariable Long id) {
        roomRepo.deleteById(id);
        return "redirect:/admin/rooms";
    }
}
