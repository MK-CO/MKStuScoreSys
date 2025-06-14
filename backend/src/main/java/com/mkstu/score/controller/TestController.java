package com.mkstu.score.controller;

import com.mkstu.score.common.Result;
import com.mkstu.score.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 测试接口
     */
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello, 学生成绩管理系统!");
    }

    /**
     * 测试认证接口
     */
    @GetMapping("/auth")
    public Result<Object> testAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return Result.success("认证成功", user);
        }
        return Result.unauthorized();
    }

    @PostMapping("/bcrypt")
    public Result<Map<String, Object>> testBCrypt(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        String hash = request.get("hash");
        
        log.info("测试BCrypt: password={}, hash={}", password, hash);
        
        Map<String, Object> result = new HashMap<>();
        
        if (password != null && hash != null) {
            boolean matches = passwordEncoder.matches(password, hash);
            result.put("matches", matches);
            log.info("BCrypt验证结果: {}", matches);
        }
        
        if (password != null) {
            String newHash = passwordEncoder.encode(password);
            result.put("newHash", newHash);
            boolean newMatches = passwordEncoder.matches(password, newHash);
            result.put("newMatches", newMatches);
            log.info("新生成hash: {}, 验证结果: {}", newHash, newMatches);
        }
        
        // 测试已知的密码
        String knownPassword = "123456";
        String knownHash = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKTx.QorwvRLDXXpLkxDqWgWnMu6";
        boolean knownMatches = passwordEncoder.matches(knownPassword, knownHash);
        result.put("knownTest", knownMatches);
        log.info("已知密码测试: password={}, hash={}, matches={}", knownPassword, knownHash, knownMatches);
        
        return Result.success("BCrypt测试完成", result);
    }
} 