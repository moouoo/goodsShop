package com.spring.goodsShop.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class Level99_interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession();
        int level = session.getAttribute("sLevel") == null ? 0 : Integer.parseInt(session.getAttribute("sLevel").toString());

        // 관리자만 이용 가능.
        if(level != 99){
            response.sendRedirect(request.getContextPath() + "/message/adminNo");
            return false;
        }
        return true;
    }
}
