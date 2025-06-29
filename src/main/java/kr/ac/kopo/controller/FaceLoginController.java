package kr.ac.kopo.controller;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

public class FaceLoginController {
	@PostMapping("/login/face")
	public ResponseEntity<Map<String, Object>> loginByFace(@RequestBody Map<String, String> payload, HttpServletRequest request) {
	    String imageData = payload.get("image");

	    try {
	        // Flask 서버에 요청
	        RestTemplate restTemplate = new RestTemplate();
	        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
	        formData.add("image", new ByteArrayResource(Base64.getDecoder().decode(imageData.split(",")[1])) {
	            @Override
	            public String getFilename() {
	                return "face.jpg";
	            }
	        });

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

	        String flaskUrl = "https://<YOUR-FLASK>/face/login";
	        ResponseEntity<Map> flaskResponse = restTemplate.postForEntity(flaskUrl, requestEntity, Map.class);

	        Map<String, Object> responseBody = flaskResponse.getBody();
	        if (responseBody != null && Boolean.TRUE.equals(responseBody.get("success"))) {
	            String username = (String) responseBody.get("user");

	            // Spring Security로 로그인 처리
	            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
	            SecurityContextHolder.getContext().setAuthentication(auth);

	            return ResponseEntity.ok(Map.of("success", true));
	        } else {
	            return ResponseEntity.ok(Map.of("success", false, "message", "Face not recognized"));
	        }

	    } catch (Exception e) {
	        return ResponseEntity.status(500).body(Map.of("success", false, "message", e.getMessage()));
	    }
	}
}
