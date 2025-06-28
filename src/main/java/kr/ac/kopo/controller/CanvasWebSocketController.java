package kr.ac.kopo.controller;

import kr.ac.kopo.model.CanvasNote;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class CanvasWebSocketController {
    @MessageMapping("/canvas.update")
    @SendTo("/topic/canvas")
    public CanvasNote updateCanvas(CanvasNote note) {
        return note;
    }
}