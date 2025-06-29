package kr.ac.kopo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import kr.ac.kopo.dto.FaceRegisterRequestDTO;
import kr.ac.kopo.model.User;
import kr.ac.kopo.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
public class FaceAuthController {

    private static final Logger log = LoggerFactory.getLogger(FaceAuthController.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/face-login-success")
    public ResponseEntity<?> faceLoginSuccess(@RequestBody Map<String, String> body,
                                              HttpServletRequest request) {
        String username = body.get("username");

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword() != null ? user.getPassword() : "",
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);

        // ✅ 기존 세션 무효화 후 새 세션 발급
        request.getSession().invalidate();
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        log.info("👤 얼굴 로그인 성공: {}", username);
        log.info("👉 세션 ID: {}", newSession.getId());

        return ResponseEntity.ok().build();
    }


    @PostMapping("/register-face")
    public ResponseEntity<Map<String, Object>> registerFace(@RequestBody FaceRegisterRequestDTO dto) {
        String flaskUrl = "http://localhost:5000/register-face"; // 실제 Flask 서버 주소로 교체 필요

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
                    log.info("✅ 얼굴 등록 완료: {}", dto.getUsername());
                }
            }

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            log.error("❌ Flask 서버 오류: {}", e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Flask 서버 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
