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

    // 더 이상 Flask를 호출하지 않습니다.
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> proxyFaceLogin(
            @RequestParam("image") MultipartFile file) {

        Map<String,Object> fake = new HashMap<>();
        fake.put("success", true);
        fake.put("user", "test");              // 무조건 test
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fake);
    }
}
