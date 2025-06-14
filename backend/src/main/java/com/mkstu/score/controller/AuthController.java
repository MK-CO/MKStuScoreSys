package com.mkstu.score.controller;

import com.mkstu.score.common.Result;
import com.mkstu.score.dto.LoginRequest;
import com.mkstu.score.service.UserService;
import com.mkstu.score.vo.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author MKStu
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = userService.login(loginRequest);
            if (loginResponse == null) {
                return Result.failed("用户名或密码错误");
            }
            return Result.ok("登录成功", loginResponse);
        } catch (Exception e) {
            LOGGER.error("用户登录失败", e);
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.ok("登出成功");
    }

    /**
     * 测试接口
     */
    @GetMapping("/test")
    public Result<String> test() {
        return Result.ok("后端服务正常运行");
    }
} 