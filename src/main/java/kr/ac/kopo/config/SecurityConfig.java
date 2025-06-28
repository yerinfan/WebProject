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
