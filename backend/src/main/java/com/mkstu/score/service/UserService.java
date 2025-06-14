package com.mkstu.score.service;

import com.mkstu.score.dto.LoginRequest;
import com.mkstu.score.entity.User;
import com.mkstu.score.vo.LoginResponse;

/**
 * 用户Service接口
 *
 * @author MKStu
 * @date 2024-01-01
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求参数
     * @return 登录结果
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * 根据用户名和用户类型获取用户信息
     *
     * @param username 用户名
     * @param userType 用户类型
     * @return 用户信息
     */
    User getUserByUsernameAndType(String username, Integer userType);

    /**
     * 根据用户ID查询用户
     *
     * @param userId 用户ID
     * @return User
     */
    User getUserById(Long userId);

    /**
     * 更新最后登录时间
     *
     * @param userId 用户ID
     */
    void updateLastLoginTime(Long userId);
} 