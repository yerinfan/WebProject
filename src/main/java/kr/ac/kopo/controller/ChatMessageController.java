package kr.ac.kopo.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import kr.ac.kopo.dto.ChatMessageDTO;
import kr.ac.kopo.dto.ChatMessageResponseDTO;
import kr.ac.kopo.model.ChatRoom;
import kr.ac.kopo.model.Message;
import kr.ac.kopo.model.User;
import kr.ac.kopo.repository.ChatRoomRepository;
import kr.ac.kopo.repository.MessageRepository;
import kr.ac.kopo.repository.UserRepository;

@Controller
public class ChatMessageController {
	private final ChatRoomRepository roomRepo;
	private final SimpMessagingTemplate template;
	private final MessageRepository messageRepo;
	private final UserRepository userRepo;

	public ChatMessageController(SimpMessagingTemplate template, MessageRepository messageRepo, UserRepository userRepo,
			ChatRoomRepository roomRepo) {
		this.template = template;
		this.messageRepo = messageRepo;
		this.userRepo = userRepo;
		this.roomRepo = roomRepo;
	}

    @MessageMapping("/chat/{roomId}")
    public void processMessage(
        @DestinationVariable("roomId") Long roomId,
        @Payload ChatMessageDTO incoming,
        Principal principal) {

        // 1) 보낸 사람 & 방 조회
        User sender = userRepo.findByUsername(principal.getName())
                              .orElseThrow();
        ChatRoom room = roomRepo.findById(roomId)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID: " + roomId));

        // 2) 엔티티 생성 & 저장
        Message msg = new Message();
        msg.setRoom(room);
        msg.setSender(sender);
        msg.setContent(incoming.getContent());
        msg.setType(incoming.getType());
        Message saved = messageRepo.save(msg);

        // 3) DTO 변환 후 브로드캐스트
        ChatMessageResponseDTO response = new ChatMessageResponseDTO(
            saved.getId(),
            saved.getRoom().getId(),
            saved.getSender().getUsername(),
            saved.getContent(),
            saved.getType(),
            saved.getTimestamp()
        );
        template.convertAndSend("/topic/rooms/" + roomId, response);
    }
}
