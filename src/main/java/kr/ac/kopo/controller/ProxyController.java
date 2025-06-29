package kr.ac.kopo.controller;

import kr.ac.kopo.resource.MultipartInputStreamFileResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/face")
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String flaskBaseUrl = "https://7c62cbc8-2887-4b96-81b2-b9603ea54eec-00-fishgerrldgj.pike.replit.dev/face";

    @PostMapping("/login")
    public ResponseEntity<String> proxyFaceLogin(@RequestParam("image") MultipartFile file) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 이미지 파일 FormData 구성
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new MultipartInputStreamFileResource(
                    file.getInputStream(),
                    file.getOriginalFilename(),
                    file.getSize()
            ));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Flask 서버로 전송
            ResponseEntity<String> response = restTemplate.postForEntity(
                    flaskBaseUrl + "/login",
                    requestEntity,
                    String.class
            );

            // 클라이언트로 JSON 명시하여 응답
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(response.getBody(), responseHeaders, response.getStatusCode());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"success\":false,\"message\":\"프록시 오류: " + e.getMessage() + "\"}");
        }
    }
}
