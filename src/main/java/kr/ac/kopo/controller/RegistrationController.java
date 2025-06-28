// 7. Registration Controller
package kr.ac.kopo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import kr.ac.kopo.model.Role;
import kr.ac.kopo.model.User;
import kr.ac.kopo.repository.UserRepository;

@Controller
public class RegistrationController {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public RegistrationController(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepo.save(user);
        return "redirect:/login?registered";
    }
}