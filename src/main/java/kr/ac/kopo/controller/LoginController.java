package kr.ac.kopo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import kr.ac.kopo.model.FaceAuthResult;
import kr.ac.kopo.service.FaceAuthService;

@Controller
public class LoginController {

    @Autowired
    private FaceAuthService faceAuthService;

    @Autowired
    public LoginController(FaceAuthService faceAuthService) {
        this.faceAuthService = faceAuthService;
    }

    /**
     * 1) 기본 로그인 폼
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        return "login";        // templates/login.html
    }

    @GetMapping("/login/face")
    public String faceLoginPage() {
        return "faceLogin"; // templates/faceLogin.html
    }

    @PostMapping("/loign/face")
    @ResponseBody
    public Map<String, Object> faceLogin(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String base64Image = payload.get("image");

        FaceAuthResult result = faceAuthService.verifyFace(base64Image);
        Map<String, Object> res = new HashMap<>();
        res.put("success", result.isSuccess());
        res.put("message", result.getMessage());

        // 인증 성공 시 세션에 사용자 정보 저장
        if (result.isSuccess()) {
            request.getSession().setAttribute("username", result.getUsername()); // 예: "yeun"
        }

        return res;
    }


    
}
