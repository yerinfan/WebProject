package kr.ac.kopo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.ac.kopo.dto.ChatMessageResponseDTO;
import kr.ac.kopo.dto.NoteDTO;
import kr.ac.kopo.model.CanvasNote;
import kr.ac.kopo.model.ChatRoom;
import kr.ac.kopo.model.Message;
import kr.ac.kopo.model.Role;
import kr.ac.kopo.model.User;
import kr.ac.kopo.repository.CanvasNoteRepository;
import kr.ac.kopo.repository.ChatRoomRepository;
import kr.ac.kopo.repository.MessageRepository;
import kr.ac.kopo.repository.UserRepository;
import kr.ac.kopo.service.FaceUserService;

@Controller
@RequestMapping("/rooms")
public class ChatRoomController {
    private final ChatRoomRepository roomRepo;
    private final MessageRepository messageRepo;
    private final CanvasNoteRepository canvasRepo;
    private final UserRepository userRepo;
    private final SimpMessagingTemplate template;

    @Autowired
    private FaceUserService faceUserService;
    
    public ChatRoomController(ChatRoomRepository roomRepo, MessageRepository messageRepo,
                               CanvasNoteRepository canvasRepo, UserRepository userRepo, SimpMessagingTemplate template) {
        this.roomRepo = roomRepo;
        this.messageRepo = messageRepo;
        this.canvasRepo = canvasRepo;
        this.userRepo = userRepo;
        this.template = template;
    }

    @GetMapping
    public String listMyRooms(Model model, Principal principal) {
        User me = userRepo.findByUsername(principal.getName()).orElseThrow();

        // ✅ 세션/인증 확인 로그
        System.out.println("🔐 현재 로그인 사용자: " + principal.getName());
        System.out.println("👉 사용자 DB 정보: " + me.getUsername() + ", Role: " + me.getRole());

        List<ChatRoom> rooms;
        if (me.getRole() == Role.ADMIN) {
            rooms = roomRepo.findAll();
        } else {
            rooms = roomRepo.findByParticipants_Id(me.getId());
        }
        model.addAttribute("rooms", rooms);

        // ✅ 얼굴 등록 여부 확인 추가
        boolean faceRegistered = faceUserService.hasRegisteredFace(me.getUsername());
        model.addAttribute("faceRegistered", faceRegistered);

        return "rooms";
    }

    @GetMapping("/all")
    public String listAllRooms(Model model) {
        List<ChatRoom> rooms = roomRepo.findAll();
        model.addAttribute("rooms", rooms);
        return "rooms";
    }

    @GetMapping("/create")
    public String showCreateForm() {
        return "rooms/create";
    }

    @PostMapping("/create")
    public String createRoom(@RequestParam("name") String name, Principal principal) {
        ChatRoom room = new ChatRoom();
        room.setName(name);
        room.setInviteCode(UUID.randomUUID().toString().substring(0, 8));
        room = roomRepo.saveAndFlush(room);

        User me = userRepo.findByUsername(principal.getName()).orElseThrow();
        room.getParticipants().add(me);
        room.setOwner(me);
        roomRepo.save(room);

        return "redirect:/rooms";
    }

    @PostMapping("/join")
    public String joinByCode(@RequestParam("code") String code, Principal principal) {
        ChatRoom room = roomRepo.findByInviteCode(code).orElseThrow(() -> new IllegalArgumentException("잘못된 초대코드입니다."));
        User me = userRepo.findByUsername(principal.getName()).orElseThrow();
        if (!room.getParticipants().contains(me)) {
            room.getParticipants().add(me);
            roomRepo.save(room);
        }
        return "redirect:/rooms/" + room.getId();
    }

    @GetMapping("/{id}")
    public String roomPage(@PathVariable("id") Long id, Model model) {
        ChatRoom room = roomRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid room ID: " + id));
        List<Message> messages = messageRepo.findByRoomIdOrderByTimestampAsc(id);
        List<CanvasNote> notes = canvasRepo.findByRoomId(id);

        List<NoteDTO> noteDtos = notes.stream()
            .map(n -> new NoteDTO(n.getId(), n.getNote(), n.getUpdatedAt()))
            .collect(Collectors.toList());

        model.addAttribute("room", room);
        model.addAttribute("messages", messages);
        model.addAttribute("notes", noteDtos);

        return "chatroom";
    }

    @PostMapping("/{roomId}/images")
    public String uploadImage(@PathVariable("roomId") Long roomId, @RequestParam("file") MultipartFile file,
                               Principal principal) throws IOException {
        ChatRoom room = roomRepo.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID: " + roomId));
        User sender = userRepo.findByUsername(principal.getName()).orElseThrow();

        Path uploadDir = Paths.get("uploads", "rooms", roomId.toString()).toAbsolutePath();
        Files.createDirectories(uploadDir);

        String original = file.getOriginalFilename();
        String ext = Optional.ofNullable(original).filter(n -> n.contains("."))
                .map(n -> n.substring(n.lastIndexOf("."))).orElse("");
        String filename = UUID.randomUUID().toString() + ext;

        Path target = uploadDir.resolve(filename);

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        Message msg = new Message();
        msg.setRoom(room);
        msg.setSender(sender);
        msg.setContent("/uploads/rooms/" + roomId + "/" + filename);
        msg.setType("IMAGE");
        msg.setTimestamp(LocalDateTime.now());
        Message saved = messageRepo.save(msg);

        ChatMessageResponseDTO dto = new ChatMessageResponseDTO(saved.getId(), roomId, sender.getUsername(),
                saved.getContent(), saved.getType(), saved.getTimestamp());
        template.convertAndSend("/topic/rooms/" + roomId, dto);

        return "redirect:/rooms/" + roomId;
    }

    // 1) 메모 수정
    @PutMapping(path = "/{roomId}/notes/{noteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public NoteDTO updateNote(
            @PathVariable Long roomId,
            @PathVariable Long noteId,
            @RequestParam("note") String newText,
            Principal principal) {

        // 방 존재 여부 확인
        roomRepo.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room: " + roomId));

        // 메모 조회
        CanvasNote note = canvasRepo.findByIdAndRoomId(noteId, roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid note: " + noteId));

        // 수정 후 저장
        note.setNote(newText);
        note.setUpdatedAt(LocalDateTime.now());
        CanvasNote saved = canvasRepo.save(note);

        // DTO로 변환하여 반환
        return new NoteDTO(saved.getId(), saved.getNote(), saved.getUpdatedAt());
    }

    // 2) 메모 생성
    @PostMapping(path = "/{roomId}/notes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public NoteDTO createNote(
            @PathVariable Long roomId,
            @RequestParam("note") String content,
            Principal principal) {

        // 방 존재 여부 확인
        ChatRoom room = roomRepo.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room: " + roomId));

        // 새 메모 생성
        CanvasNote note = new CanvasNote();
        note.setRoom(room);
        note.setNote(content);
        note.setUpdatedAt(LocalDateTime.now());

        CanvasNote saved = canvasRepo.save(note);
        return new NoteDTO(saved.getId(), saved.getNote(), saved.getUpdatedAt());
    }

    @PostMapping("/{roomId}/delete")
    public String deleteRoom(@PathVariable("roomId") Long roomId, Principal principal) {
        ChatRoom room = roomRepo.findById(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));

        // 현재 로그인한 사용자가 방장인지 확인
        String currentUsername = principal.getName();
        if (!room.getOwner().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("방장만 방을 삭제할 수 있습니다.");
        }

        roomRepo.delete(room);
        return "redirect:/rooms";
    }
}
