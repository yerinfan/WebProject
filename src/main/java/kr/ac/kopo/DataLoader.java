package kr.ac.kopo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.ac.kopo.model.Role;
import kr.ac.kopo.model.User;
import kr.ac.kopo.repository.UserRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadAdmin(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("password123"));
                admin.setRole(Role.ADMIN);
                userRepo.save(admin);
                System.out.println("==> 기본 관리자 계정 생성: admin / password123");
            }
        };
    }
}
