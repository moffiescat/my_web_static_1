package org.example.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类 - 负责生成、解析和验证 JWT 令牌
 */
@Component
public class JwtUtil {

    // 密钥 - 生产环境应使用环境变量或配置文件管理
    private static final String SECRET = "your-256-bit-secret-key-for-jwt-signing-must-be-at-least-32-characters-long";
    // 令牌过期时间（24小时）
    private static final long EXPIRATION = 86400000;

    // 基于密钥生成 HMAC-SHA256 签名密钥
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * 生成 JWT 令牌
     * @param username 用户名
     * @return JWT 令牌字符串
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .subject(username)  // 设置用户名作为主题
                .issuedAt(now)      // 签发时间
                .expiration(expiryDate)  // 过期时间
                .signWith(key)      // 使用密钥签名
                .compact();         // 生成最终的 JWT 字符串
    }

    /**
     * 从令牌中提取用户名
     * @param token JWT 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)     // 使用密钥验证签名
                .build()
                .parseSignedClaims(token)  // 解析令牌
                .getPayload();       // 获取载荷部分
        return claims.getSubject();  // 返回主题（用户名）
    }

    /**
     * 验证令牌的有效性
     * @param token JWT 令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)     // 验证签名
                    .build()
                    .parseSignedClaims(token);  // 解析令牌
            return true;  // 验证通过
        } catch (JwtException | IllegalArgumentException e) {
            // 验证失败（签名错误或令牌过期）
            return false;
        }
    }
}
