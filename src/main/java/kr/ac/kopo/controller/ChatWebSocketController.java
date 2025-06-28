// 6. WebSocket Controllers
package kr.ac.kopo.controller;

import kr.ac.kopo.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(Message chatMessage) {
        return chatMessage;
    }
}