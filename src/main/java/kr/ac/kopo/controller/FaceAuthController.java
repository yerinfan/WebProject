package kr.ac.kopo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpSession;
import kr.ac.kopo.dto.FaceRegisterRequestDTO;
import kr.ac.kopo.model.User;
import kr.ac.kopo.repository.UserRepository;

@RestController
public class FaceAuthController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/face-login-success")
    public ResponseEntity<?> faceLoginSuccess(@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.get("username");

        // ✅ 사용자 확인
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // ✅ Spring Security 인증 객체 생성
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword() != null ? user.getPassword() : "", // null 방지
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // ✅ SecurityContext 설정 및 세션에 저장
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        System.out.println("👤 얼굴 로그인 성공: " + username);
        System.out.println("👉 세션 ID: " + session.getId());
        System.out.println("👉 SecurityContext 저장됨: " + context.getAuthentication().getName());
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register-face")
    public ResponseEntity<Map<String, Object>> registerFace(@RequestBody FaceRegisterRequestDTO dto) {
        String flaskUrl = "http://localhost:5000/register-face"; // Flask 서버 주소에 따라 변경 필요

        Map<String, Object> payload = new HashMap<>();
        payload.put("username", dto.getUsername());
        payload.put("images", dto.getImages());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, entity, Map.class);

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
