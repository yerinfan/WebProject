package kr.ac.kopo.service.impl;

import kr.ac.kopo.model.*;
import kr.ac.kopo.repository.*;
import kr.ac.kopo.service.ChatService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {
    private final UserRepository userRepo;
    private final ChatRoomRepository roomRepo;
    private final MessageRepository msgRepo;
    private final CanvasNoteRepository canvasRepo;

    public ChatServiceImpl(UserRepository userRepo, ChatRoomRepository roomRepo,
                           MessageRepository msgRepo, CanvasNoteRepository canvasRepo) {
        this.userRepo = userRepo;
        this.roomRepo = roomRepo;
        this.msgRepo = msgRepo;
        this.canvasRepo = canvasRepo;
    }

    @Override
    public User registerUser(User user) {
        // encrypt password before saving
        return userRepo.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }

    @Override
    public ChatRoom createRoom(String name) {
        ChatRoom room = new ChatRoom();
        room.setName(name);
        room.setInviteCode(UUID.randomUUID().toString().substring(0,8));
        return roomRepo.save(room);
    }

    @Override
    public List<ChatRoom> listRooms() {
        return roomRepo.findAll();
    }

    @Override
    public ChatRoom findRoom(Long roomId) {
        return roomRepo.findById(roomId).orElseThrow();
    }

    @Override
    public ChatRoom joinByInviteCode(String code) {
        return roomRepo.findByInviteCode(code).orElseThrow();
    }

    @Override
    public Message saveMessage(Message message) {
        return msgRepo.save(message);
    }

    @Override
    public List<Message> getMessages(Long roomId) {
        return msgRepo.findAll(); // add filter by room
    }

    @Override
    public CanvasNote saveCanvas(CanvasNote note) {
        return canvasRepo.save(note);
    }

    @Override
    public CanvasNote getCanvas(Long roomId) {
        return canvasRepo.findById(roomId).orElse(new CanvasNote());
    }
}