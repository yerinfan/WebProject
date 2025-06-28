package kr.ac.kopo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaceRegisterController {

    @GetMapping("/register-face")
    public String showFaceRegisterPage() {
        return "face/faceRegister"; // templates/face/faceRegister.html
    }
}
