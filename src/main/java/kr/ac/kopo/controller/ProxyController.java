package kr.ac.kopo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import kr.ac.kopo.resource.MultipartInputStreamFileResource;

@RestController
@RequestMapping("/api/face")
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskBaseUrl = "https://7c62cbc8-2887-4b96-81b2-b9603ea54eec-00-fishgerrldgj.pike.replit.dev/face";

    @PostMapping("/api/face/login")
    public ResponseEntity<?> proxyFaceLogin(@RequestParam("image") MultipartFile imageFile) {
        try {
            // 파일 null 또는 비어있는지 확인
            if (imageFile == null || imageFile.isEmpty()) {
                System.err.println("❌ 업로드된 이미지가 없습니다.");
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "이미지 파일이 비어 있습니다.");
                return ResponseEntity.badRequest().body(error);
            }

            // Content-Type: multipart/form-data 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 이미지 데이터를 Flask로 전달할 body 구성
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new MultipartInputStreamFileResource(
                    imageFile.getInputStream(), imageFile.getOriginalFilename())  // ← 길이 인자 제거됨
            );

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 🛰 Flask 서버 주소
            String flaskUrl = "https://7c62cbc8-2887-4b96-81b2-b9603ea54eec-00-fishgerrldgj.pike.replit.dev/face/login";

            // 요청 전송
            ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, requestEntity, String.class);

            // JSON인지 확인 (필요시 검증 추가 가능)
            HttpHeaders respHeaders = response.getHeaders();
            String contentType = respHeaders.getContentType() != null ? respHeaders.getContentType().toString() : "";
            if (!contentType.contains("application/json")) {
                System.err.println("⚠️ JSON 응답 아님: " + response.getBody());
            }

            return ResponseEntity
                    .status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getBody());

        } catch (Exception e) {
            // 예외 발생 시 JSON 형식으로 응답
            System.err.println("❌ 프록시 오류: " + e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "프록시 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
