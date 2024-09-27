package com.spring.goodsShop.etc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf((csrf) -> csrf.disable()); 코드로 아무것도 언급 안하면 디폴트가 켜지는거라서 csrf켜지는 중..
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll() // 모든 요청 허용
                )
                .headers(headers -> headers
//                        .frameOptions().sameOrigin() // 동일 출처에서의 프레임 허용
                        .contentSecurityPolicy("frame-ancestors 'self'") // 동일 출처에서의 프레임 허용
                );

        return http.build();
    }




    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
