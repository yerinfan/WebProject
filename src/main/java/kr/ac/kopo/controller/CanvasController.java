package kr.ac.kopo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import kr.ac.kopo.model.CanvasNote;
import kr.ac.kopo.model.ChatRoom;
import kr.ac.kopo.repository.CanvasNoteRepository;
import kr.ac.kopo.repository.ChatRoomRepository;

@Controller
@RequestMapping("/rooms/{roomId}/canvas")
public class CanvasController {

    private final CanvasNoteRepository noteRepo;
    private final ChatRoomRepository roomRepo;

    public CanvasController(CanvasNoteRepository noteRepo, ChatRoomRepository roomRepo) {
        this.noteRepo = noteRepo;
        this.roomRepo = roomRepo;
    }

    // 캔버스 페이지 조회
    @GetMapping
    public String showCanvas(@PathVariable Long roomId, Model model) {
        ChatRoom room = roomRepo.findById(roomId).orElseThrow();
        List<CanvasNote> notes = noteRepo.findByRoomId(roomId);
        model.addAttribute("room", room);
        model.addAttribute("notes", notes);
        return "canvas";
    }

    // 메모 저장
    @PostMapping("/add")
    public String addNote(
          @PathVariable Long roomId,
          @RequestParam String note
    ) {
        CanvasNote cn = new CanvasNote();
        cn.setId(roomId);
        cn.setNote(note);
        noteRepo.save(cn);
        return "redirect:/rooms/" + roomId + "/canvas";
    }
}
