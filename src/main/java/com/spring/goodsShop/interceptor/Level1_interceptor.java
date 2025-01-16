package com.spring.goodsShop.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
@Component
public class Level1_interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession();
        int level = session.getAttribute("sLevel") == null ? 0 : Integer.parseInt(session.getAttribute("sLevel").toString());

        // 로그인해야지 이용가능
        if(level >= 1){
            response.sendRedirect(request.getContextPath() + "/message/level1No");
            return false;
        }
        return true;
    }
}
