package kr.ac.kopo.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FaceUserService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String ENCODING_DIR = "D:/faceLogin/encodings"; // 경로는 실제와 맞춰주세요

    public boolean hasRegisteredFace(String username) {
        Path path = Paths.get(ENCODING_DIR, username, username + ".npy");
        return Files.exists(path);
    }
    
    
    public List<String> getRegisteredFaceUsers() {
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:5000/admin/face-users", List.class);
            return response.getBody();
        } catch (Exception e) {
            return List.of(); // 실패 시 빈 리스트 반환
        }
    }
    
    public boolean deleteFaceEncoding(String username) {
        try {
            restTemplate.delete("http://localhost:5000/admin/delete-face/" + username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
