package com.spring.goodsShop.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
@Component
public class Level2_interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
//        String section = request.getParameter("section");
        HttpSession session = request.getSession();
        int level = session.getAttribute("sLevel") == null ? 0 : Integer.parseInt(session.getAttribute("sLevel").toString());

        // 개인페이지에서 계좌등록한 상품판매유저만.
        if(level < 2){
            response.sendRedirect(request.getContextPath() + "/message/level2No");
            return false;
        }
        return true;
    }
}
