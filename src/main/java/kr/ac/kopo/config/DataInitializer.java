package kr.ac.kopo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.ac.kopo.model.User;
import kr.ac.kopo.model.Role;
import kr.ac.kopo.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadAdminUser(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            // admin 계정이 없으면 생성
            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("1234"));
                admin.setRole(Role.ADMIN);          // ← enum으로 설정
                admin.setFaceRegistered(false);
                userRepo.save(admin);
                System.out.println("✅ Admin 계정 생성됨: admin / 1234");
            }

            // user 계정도 없으면 생성
            if (userRepo.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(encoder.encode("1234"));
                user.setRole(Role.USER);           // ← enum으로 설정
                user.setFaceRegistered(false);
                userRepo.save(user);
                System.out.println("✅ User 계정 생성됨: user / 1234");
            }
        };
    }
}
