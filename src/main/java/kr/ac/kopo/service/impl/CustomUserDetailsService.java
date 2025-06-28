package kr.ac.kopo.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.ac.kopo.model.User;
import kr.ac.kopo.repository.UserRepository;

@Service  // Bean으로 등록
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 사용자 조회
        User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + username));

        // Spring Security의 User 객체로 변환
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())        // 이미 BCrypt 등으로 암호화돼 있어야 함
            .roles(user.getRole().name())        // 예: Role.ADMIN → "ADMIN"
            .build();
    }
}
