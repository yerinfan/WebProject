package kr.ac.kopo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import kr.ac.kopo.RoleBasedAuthSuccessHandler;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private RoleBasedAuthSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/register-face", "/h2-console/**") // ✅ CSRF 예외 처리
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable()) // ✅ H2 콘솔 iframe 허용
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/signup", "/login", "/css/**", "/js/**").permitAll()
                .requestMatchers("/logout").permitAll()
                .requestMatchers("/login/face").permitAll()
                .requestMatchers(HttpMethod.POST, "/login/face").permitAll()
                .requestMatchers("/register-face").permitAll()
                .requestMatchers(HttpMethod.POST, "/register-face").permitAll()
                .requestMatchers("/h2-console/**").permitAll() // ✅ H2 콘솔 접근 허용
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
            );

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(
        	    "http://localhost:8888",
        	    "http://172.31.57.16:8888",
        	    "https://7c62cbc8-2887-4b96-81b2-b9603ea54eec-00-fishgerrldgj.pike.replit.dev"
        	));
        config.setAllowCredentials(true);
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
