package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.service.VisitService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class VisitInterceptor implements HandlerInterceptor {

    private final VisitService visitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只统计GET请求和根路径的访问
        if ("GET".equals(request.getMethod()) && "/".equals(request.getRequestURI())) {
            visitService.incrementVisitCount();
        }
        return true;
    }
}
