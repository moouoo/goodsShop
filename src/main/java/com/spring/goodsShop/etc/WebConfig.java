package com.spring.goodsShop.etc;

import com.spring.goodsShop.interceptor.Level1_intercetor;
import com.spring.goodsShop.interceptor.Level2_interceptor;
import com.spring.goodsShop.interceptor.Level3_interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    Level3_interceptor level3Interceptor;

    @Autowired
    Level2_interceptor level2Interceptor;

    @Autowired
    Level1_intercetor level1Intercetor;

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

    // 참조파일설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /img/**로 시작하는 URL 요청에 대해 파일 시스템의 경로를 매핑
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:/Users/moo/IdeaProjects/goodsShop/src/main/resources/static/img/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(level3Interceptor)
                .addPathPatterns("/admin/**");
        registry.addInterceptor(level2Interceptor)
                .addPathPatterns("/member/**");
        registry.addInterceptor(level1Intercetor)
                .addPathPatterns("/member/*");
    }

    // 인턴셉터 조건 설정 및 위치설정해야함.

}
