package kr.ac.kopo.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FaceController {
    @GetMapping("/face-login")
    public String faceLoginPage() {
        return "face-login"; // `templates/face-login.html`
    }

    @PostMapping("/face-login")
    @ResponseBody
    public Map<String, Object> faceLogin(@RequestParam("image") MultipartFile image) throws IOException {
        String flaskUrl = "{실제_리플릿_URL}/face/login";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", image.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate rest = new RestTemplate();
        ResponseEntity<Map> resp = rest.postForEntity(flaskUrl, requestEntity, Map.class);

        return resp.getBody();
    }
}
