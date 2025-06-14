package com.mkstu.score.utils;

import com.mkstu.score.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 根据用户信息生成token
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("userType", user.getUserType());
        claims.put("realName", user.getRealName());
        claims.put("userNo", user.getUserNo());
        return generateToken(claims);
    }

    /**
     * 从token中获取JWT中的载荷
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.info("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从token中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Long userId;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = Long.valueOf(claims.get("id").toString());
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }

    /**
     * 从token中获取用户类型
     */
    public Integer getUserTypeFromToken(String token) {
        Integer userType;
        try {
            Claims claims = getClaimsFromToken(token);
            userType = Integer.valueOf(claims.get("userType").toString());
        } catch (Exception e) {
            userType = null;
        }
        return userType;
    }

    /**
     * 验证token是否还有效
     */
    public Boolean validateToken(String token, String username) {
        String usernameFromToken = getUserNameFromToken(token);
        return (usernameFromToken.equals(username) && !isTokenExpired(token));
    }

    /**
     * 判断token是否已经失效
     */
    private Boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject((String) claims.get("username"))
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 当原来的token没过期时是可以刷新的
     */
    public String refreshHeadToken(String oldToken) {
        if (oldToken.startsWith(tokenHead)) {
            String token = oldToken.substring(tokenHead.length());
            if (!isTokenExpired(token)) {
                return generateToken(getClaimsFromToken(token));
            }
        }
        return null;
    }
} 