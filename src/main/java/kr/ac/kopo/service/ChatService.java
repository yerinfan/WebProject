// 8. Service Layer
package kr.ac.kopo.service;

import kr.ac.kopo.model.ChatRoom;
import kr.ac.kopo.model.Message;
import kr.ac.kopo.model.CanvasNote;
import kr.ac.kopo.model.User;

import java.util.List;

public interface ChatService {
    User registerUser(User user);
    User findByUsername(String username);

    ChatRoom createRoom(String name);
    List<ChatRoom> listRooms();
    ChatRoom findRoom(Long roomId);
    ChatRoom joinByInviteCode(String code);

    Message saveMessage(Message message);
    List<Message> getMessages(Long roomId);

    CanvasNote saveCanvas(CanvasNote note);
    CanvasNote getCanvas(Long roomId);
}
