package kr.ac.kopo.controller;

import kr.ac.kopo.dto.FaceRegisterRequestDTO;
import kr.ac.kopo.model.User;
import kr.ac.kopo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FaceAuthController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/face-login-success")
    public ResponseEntity<?> faceLoginSuccess(@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.get("username");

        // 사용자 DB 조회
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // ✅ 세션에 사용자 인증 정보 저장
        session.setAttribute("user", user);  // 또는 Spring Security의 Authentication으로 처리할 수도 있음

        return ResponseEntity.ok().build();
    }


    @PostMapping("/register-face")
    public ResponseEntity<Map<String, Object>> registerFace(@RequestBody FaceRegisterRequestDTO dto) {
        String flaskUrl = "http://localhost:5000/register-face"; // 실제 Flask 서버 주소로 교체 필요

        // Flask 서버로 전달할 요청 JSON 구성
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", dto.getUsername());
        payload.put("images", dto.getImages());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, entity, Map.class);

            // Flask에서 등록 성공시 DB에 얼굴 등록 여부 true로 반영
            if (response.getBody() != null && Boolean.TRUE.equals(response.getBody().get("success"))) {
                User user = userRepository.findByUsername(dto.getUsername()).orElse(null);
                if (user != null) {
                    user.setFaceRegistered(true);
                    userRepository.save(user);
                }
            }

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Flask 서버 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
