package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserDao;
import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.dto.RegisterRequest;
import org.example.entity.User;
import org.example.service.AuthService;
import org.example.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现类 - 处理登录和注册业务逻辑
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;  // 数据访问层，操作用户数据
    private final PasswordEncoder passwordEncoder;  // 密码编码器
    private final JwtUtil jwtUtil;  // JWT 工具类，用于生成令牌

    /**
     * 用户注册
     * @param request 注册请求
     */
    @Override
    public void register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userDao.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        // 密码加密
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        // 保存用户到数据库
        userDao.save(user);
    }

    /**
     * 用户登录
     * @param request 登录请求
     * @return 登录响应（包含 JWT 令牌）
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        // 根据用户名查询用户
        User user = userDao.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成 JWT 令牌
        String token = jwtUtil.generateToken(user.getUsername());
        // 返回登录响应（包含令牌）
        return LoginResponse.success(user.getUsername(), user.getEmail(), token);
    }
}
