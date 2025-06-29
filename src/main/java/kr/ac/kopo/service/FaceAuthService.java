package kr.ac.kopo.service;

import kr.ac.kopo.model.FaceAuthResult;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FaceAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String FLASK_SERVER_URL = "https://7c62cbc8-2887-4b96-81b2-b9603ea54eec-00-fishgerrldgj.pike.replit.dev";

    public FaceAuthResult verifyFace(String base64Image) {
        Map<String, String> request = new HashMap<>();
        request.put("image", base64Image);

        try {
            ResponseEntity<FaceAuthResult> response = restTemplate.postForEntity(
                FLASK_SERVER_URL + "/verify-face", request, FaceAuthResult.class);
            return response.getBody();
        } catch (Exception e) {
            return new FaceAuthResult(false, "서버 오류: " + e.getMessage(), null);
        }
    }

    public FaceAuthResult registerFace(String username, List<String> images) {
        Map<String, Object> request = new HashMap<>();
        request.put("username", username);
        request.put("images", images); // Base64 문자열 리스트

        try {
            ResponseEntity<FaceAuthResult> response = restTemplate.postForEntity(
                FLASK_SERVER_URL + "/register-face",
                request,
                FaceAuthResult.class
            );
            return response.getBody();
        } catch (Exception e) {
            return new FaceAuthResult(false, "서버 오류: " + e.getMessage(), null);
        }
    }

    // ✅ 회원 얼굴 여러 장 등록용 메서드
    public FaceAuthResult registerMultipleImages(String username, List<String> images) {
        Map<String, Object> request = new HashMap<>();
        request.put("username", username);
        request.put("images", images);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<FaceAuthResult> response = restTemplate.postForEntity(
                FLASK_SERVER_URL + "/register-face", entity, FaceAuthResult.class);
            return response.getBody();
        } catch (Exception e) {
            return new FaceAuthResult(false, "등록 실패: " + e.getMessage(), null);
        }
    }
}
