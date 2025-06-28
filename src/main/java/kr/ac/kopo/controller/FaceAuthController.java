package kr.ac.kopo.controller;

import kr.ac.kopo.dto.FaceRegisterRequestDTO;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FaceAuthController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/register-face")
    public ResponseEntity<Map<String, Object>> registerFace(@RequestBody FaceRegisterRequestDTO dto) {
        String flaskUrl = "http://localhost:5000/register-face";

        // Flask 서버로 전달할 요청 JSON 구성
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", dto.getUsername());
        payload.put("images", dto.getImages());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskUrl, entity, Map.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Flask 서버 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
