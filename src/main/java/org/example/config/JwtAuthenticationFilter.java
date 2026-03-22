package org.example.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器 - 从请求中提取并验证 JWT 令牌
 * 继承 OncePerRequestFilter 确保每个请求只被过滤一次
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * 构造方法 - 注入 JwtUtil
     * @param jwtUtil JWT 工具类
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 核心过滤方法
     * @param request HTTP请求
     * @param response HTTP响应
     * @param filterChain 过滤器链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取 Authorization 头
        String header = request.getHeader("Authorization");

        // 检查是否存在 Bearer 令牌
        if (header != null && header.startsWith("Bearer ")) {
            // 提取令牌（去掉 "Bearer " 前缀）
            String token = header.substring(7);

            // 验证令牌的有效性
            if (jwtUtil.validateToken(token)) {
                // 从令牌中提取用户名
                String username = jwtUtil.getUsernameFromToken(token);

                // 创建 UserDetails 对象
                UserDetails userDetails = new User(username, "", Collections.emptyList());
                // 创建认证令牌
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 将认证信息设置到 Security 上下文中
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 继续执行过滤器链
        filterChain.doFilter(request, response);
    }
}
