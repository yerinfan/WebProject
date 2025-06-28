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

    public FaceAuthResult verifyFace(String base64Image) {
        Map<String, String> request = new HashMap<>();
        request.put("image", base64Image);

        try {
            ResponseEntity<FaceAuthResult> response = restTemplate.postForEntity(
                "http://localhost:5000/verify-face", request, FaceAuthResult.class);
            return response.getBody();
        } catch (Exception e) {
            return new FaceAuthResult(false, "서버 오류: " + e.getMessage(), null);
        }
    }
    
    public FaceAuthResult registerFace(String username, List<String> images) {
        Map<String, Object> request = new HashMap<>();
        request.put("username", username);
        request.put("images", images);  // ★ 반드시 List로 보내야 함

        try {
            ResponseEntity<FaceAuthResult> response = restTemplate.postForEntity(
                "http://localhost:5000/register-face",
                request,
                FaceAuthResult.class
            );
            return response.getBody();
        } catch (Exception e) {
            return new FaceAuthResult(false, "서버 오류: " + e.getMessage(), null);
        }
    }


    // ✅ 회원 얼굴 여러 장 등록용 메서드 추가
    public FaceAuthResult registerMultipleImages(String username, List<String> images) {
        Map<String, Object> request = new HashMap<>();
        request.put("username", username);
        request.put("images", images);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<FaceAuthResult> response = restTemplate.postForEntity(
                "http://localhost:5000/register-face", entity, FaceAuthResult.class);
            return response.getBody();
        } catch (Exception e) {
            return new FaceAuthResult(false, "등록 실패: " + e.getMessage(), null);
        }
    }
}
