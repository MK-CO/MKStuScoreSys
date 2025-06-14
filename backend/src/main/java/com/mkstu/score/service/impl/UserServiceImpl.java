package com.mkstu.score.service.impl;

import com.mkstu.score.dto.LoginRequest;
import com.mkstu.score.entity.User;
import com.mkstu.score.mapper.UserMapper;
import com.mkstu.score.service.UserService;
import com.mkstu.score.utils.JwtTokenUtil;
import com.mkstu.score.vo.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse loginResponse = null;
        User user = getUserByUsernameAndUserType(loginRequest.getUsername(), loginRequest.getUserType());
        
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            if (user.getStatus() == 0) {
                throw new RuntimeException("账号已被禁用，请联系管理员");
            }
            
            // 生成JWT token
            String token = jwtTokenUtil.generateToken(user.getId(), user.getUsername(), user.getUserType());
            
            // 更新最后登录时间
            updateLastLoginTime(user.getId());
            
            // 构建返回结果
            loginResponse = new LoginResponse();
            loginResponse.setUserId(user.getId());
            loginResponse.setUsername(user.getUsername());
            loginResponse.setRealName(user.getRealName());
            loginResponse.setUserNo(user.getUserNo());
            loginResponse.setUserType(user.getUserType());
            loginResponse.setUserTypeName(getUserTypeName(user.getUserType()));
            loginResponse.setClassId(user.getClassId());
            loginResponse.setMajor(user.getMajor());
            loginResponse.setGrade(user.getGrade());
            loginResponse.setAvatar(user.getAvatar());
            loginResponse.setToken(token);
            loginResponse.setTokenHead(tokenHead);
        }
        
        return loginResponse;
    }

    @Override
    public User getUserByUsernameAndUserType(String username, Integer userType) {
        return userMapper.selectByUsernameAndUserType(username, userType);
    }

    @Override
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public void updateLastLoginTime(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);
    }

    /**
     * 获取用户类型名称
     */
    private String getUserTypeName(Integer userType) {
        switch (userType) {
            case 1:
                return "管理员";
            case 2:
                return "教师";
            case 3:
                return "学生";
            default:
                return "未知";
        }
    }
} 