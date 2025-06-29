package kr.ac.kopo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.ac.kopo.dto.FaceRegisterRequestDTO;
import kr.ac.kopo.model.User;
import kr.ac.kopo.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class FaceAuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/face-login-success")
    public ResponseEntity<Map<String, Object>> faceLoginSuccess(HttpServletRequest request) {
        // 1) 강제로 로그인시킬 사용자 아이디
        String forcedUsername = "test";

        // 2) DB에서 test 유저 정보 꺼내오기
        User user = userRepository.findByUsername(forcedUsername)
                      .orElseThrow(() -> new UsernameNotFoundException(forcedUsername));

        // 3) Spring Security 인증 토큰 생성
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // 4) SecurityContext에 넣고 세션 갱신
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        request.getSession().invalidate();
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            context
        );

        // 5) JSON 응답
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("user", forcedUsername);
        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(resp);
    }

}
