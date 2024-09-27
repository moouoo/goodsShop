package com.spring.goodsShop.etc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // 모든 요청에 대해 CORS 설정
//                .allowedOrigins("http://localhost:8080") // 허용할 출처
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
//                .allowedHeaders("*") // 허용할 헤더
//                .allowCredentials(true); // 자격 증명 허용
//    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/static/") // 정적 리소스 경로 설정
//                .setCachePeriod(3600); // 캐시 기간 설정
//    }
}
