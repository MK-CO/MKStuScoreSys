package com.mkstu.score.controller;

import com.mkstu.score.common.Result;
import com.mkstu.score.dto.LoginRequest;
import com.mkstu.score.service.UserService;
import com.mkstu.score.vo.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 测试密码编码
     */
    @PostMapping("/test-password")
    public Result<String> testPassword(@RequestBody Map<String, String> request) {
        String rawPassword = request.get("password");
        String encodedPassword = request.get("encodedPassword");
        
        log.info("测试密码验证: rawPassword={}, encodedPassword={}", rawPassword, encodedPassword);
        
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        log.info("密码匹配结果: {}", matches);
        
        // 同时生成一个新的编码密码用于对比
        String newEncoded = passwordEncoder.encode(rawPassword);
        log.info("新生成的编码密码: {}", newEncoded);
        
        return Result.success("密码验证结果: " + matches + ", 新编码: " + newEncoded);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = userService.login(loginRequest);
            return Result.success("登录成功", loginResponse);
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 学生登录
     */
    @PostMapping("/student/login")
    public Result<LoginResponse> studentLogin(@Validated @RequestBody LoginRequest loginRequest) {
        try {
            // 强制设置用户类型为学生
            loginRequest.setUserType(3);
            LoginResponse loginResponse = userService.login(loginRequest);
            return Result.success("学生登录成功", loginResponse);
        } catch (Exception e) {
            log.error("学生登录失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 教师登录
     */
    @PostMapping("/teacher/login")
    public Result<LoginResponse> teacherLogin(@Validated @RequestBody LoginRequest loginRequest) {
        try {
            // 强制设置用户类型为教师
            loginRequest.setUserType(2);
            LoginResponse loginResponse = userService.login(loginRequest);
            return Result.success("教师登录成功", loginResponse);
        } catch (Exception e) {
            log.error("教师登录失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 管理员登录
     */
    @PostMapping("/admin/login")
    public Result<LoginResponse> adminLogin(@Validated @RequestBody LoginRequest loginRequest) {
        try {
            // 强制设置用户类型为管理员
            loginRequest.setUserType(1);
            LoginResponse loginResponse = userService.login(loginRequest);
            return Result.success("管理员登录成功", loginResponse);
        } catch (Exception e) {
            log.error("管理员登录失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        // JWT是无状态的，客户端删除token即可
        return Result.success("退出登录成功");
    }

    /**
     * 测试查询用户信息
     */
    @PostMapping("/test-user")
    public Result<Object> testUser(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        Integer userType = (Integer) request.get("userType");
        
        log.info("查询用户: username={}, userType={}", username, userType);
        
        try {
            com.mkstu.score.entity.User user = userService.getUserByUsernameAndType(username, userType);
            if (user == null) {
                return Result.error("用户不存在");
            }
            
            // 返回用户信息（隐藏敏感信息）
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("realName", user.getRealName());
            userInfo.put("userType", user.getUserType());
            userInfo.put("status", user.getStatus());
            userInfo.put("passwordLength", user.getPassword().length());
            userInfo.put("passwordPrefix", user.getPassword().substring(0, Math.min(10, user.getPassword().length())));
            
            return Result.success("查询成功", userInfo);
        } catch (Exception e) {
            log.error("查询用户失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
} 