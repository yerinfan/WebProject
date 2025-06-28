package kr.ac.kopo.controller;

import kr.ac.kopo.model.User;
import kr.ac.kopo.repository.UserRepository;
import kr.ac.kopo.service.FaceUserService;
import kr.ac.kopo.model.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserRepository userRepo;

    @Autowired
    private FaceUserService faceUserService;

    public AdminUserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // ✅ 사용자 목록 + 얼굴 등록 사용자 리스트 동시 조회
    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userRepo.findAll();
        List<String> faceUsers = faceUserService.getRegisteredFaceUsers();

        model.addAttribute("users", users);
        model.addAttribute("faceUsers", faceUsers);
        return "admin/users";  // 경로 수정
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        userRepo.deleteById(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/role")
    public String changeUserRole(@PathVariable("id") Long id,
                                 @RequestParam("role") Role role) {
        User u = userRepo.findById(id)
                         .orElseThrow(() -> new IllegalArgumentException("Unknown user: " + id));
        u.setRole(role);
        userRepo.save(u);
        return "redirect:/admin/users";
    }
    
    @PostMapping("/face-delete")
    public String deleteFaceEncoding(@RequestParam("username") String username) {
        faceUserService.deleteFaceEncoding(username);
        return "redirect:/admin/users";
    }
}
