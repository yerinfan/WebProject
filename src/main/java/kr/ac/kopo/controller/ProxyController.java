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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.ac.kopo.resource.MultipartInputStreamFileResource;

@RestController
@RequestMapping("/api/face")
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskBaseUrl = "https://7c62cbc8-2887-4b96-81b2-b9603ea54eec-00-fishgerrldgj.pike.replit.dev/face";

    @PostMapping("/api/face/login")
    public ResponseEntity<?> proxyFaceLogin(@RequestParam("image") MultipartFile imageFile) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new MultipartInputStreamFileResource(
                    imageFile.getInputStream(), imageFile.getOriginalFilename()
            ));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String flaskUrl = "https://7c62cbc8-2887-4b96-81b2-b9603ea54eec-00-fishgerrldgj.pike.replit.dev/face/login";
            ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, requestEntity, String.class);

            // JSON 문자열 → Map 변환
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> json = mapper.readValue(response.getBody(), new TypeReference<>() {});

            return ResponseEntity.status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "프록시 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
