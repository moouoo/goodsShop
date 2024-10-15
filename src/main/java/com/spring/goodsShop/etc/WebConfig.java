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

//    이거부터 할 차례, 현재 문제는 등록 후에 등록된 상품 확인시 이미지를 못 찾는다는 문제 발견. 단, 서버를 재가동시키면 이미지를 찾는다.
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // /img/**로 시작하는 URL 요청에 대해 파일 시스템의 경로를 매핑
//        registry.addResourceHandler("/img/product_img/**")
//                .addResourceLocations("file:/path/to/your/images/");
//    }
}
