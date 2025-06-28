package kr.ac.kopo.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root(Principal principal) {
        if (principal == null) {
            // 로그인 안 된 사용자면 로그인 페이지로
            return "redirect:/login";
        }
        // 로그인 된 사용자면 rooms 목록으로
        return "redirect:/rooms";
    }
}
