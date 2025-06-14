package com.mkstu.score.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mkstu.score.dto.LoginRequest;
import com.mkstu.score.entity.User;
import com.mkstu.score.mapper.UserMapper;
import com.mkstu.score.service.UserService;
import com.mkstu.score.utils.JwtUtils;
import com.mkstu.score.vo.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户Service实现类
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        log.info("用户登录尝试: username={}, userType={}", loginRequest.getUsername(), loginRequest.getUserType());
        
        // 根据用户名和用户类型查询用户
        User user = getUserByUsernameAndType(loginRequest.getUsername(), loginRequest.getUserType());
        if (user == null) {
            log.error("用户不存在: username={}, userType={}", loginRequest.getUsername(), loginRequest.getUserType());
            throw new RuntimeException("用户不存在");
        }

        log.info("找到用户: userId={}, username={}, realName={}", user.getId(), user.getUsername(), user.getRealName());

        // 验证账号状态
        if (user.getStatus() == 0) {
            log.error("账号已被禁用: userId={}", user.getId());
            throw new RuntimeException("账号已被禁用");
        }

        // 验证密码
        log.info("开始验证密码: 输入密码={}, 数据库密码={}", loginRequest.getPassword(), user.getPassword());
        log.info("输入密码长度: {}, 数据库密码长度: {}", loginRequest.getPassword().length(), user.getPassword().length());
        log.info("输入密码字节: {}", java.util.Arrays.toString(loginRequest.getPassword().getBytes()));
        log.info("数据库密码字节: {}", java.util.Arrays.toString(user.getPassword().getBytes()));
        
        // 测试一个新生成的密码
        String testEncoded = passwordEncoder.encode("123456");
        log.info("新生成的123456加密结果: {}", testEncoded);
        boolean testMatches = passwordEncoder.matches("123456", testEncoded);
        log.info("新生成密码验证结果: {}", testMatches);
        
        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        log.info("密码验证结果: {}", passwordMatches);
        
        // 尝试直接验证已知的密码
        boolean directTest = passwordEncoder.matches("123456", "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKTx.QorwvRLDXXpLkxDqWgWnMu6");
        log.info("直接验证123456结果: {}", directTest);
        
        if (!passwordMatches) {
            log.error("密码验证失败: username={}, userType={}", loginRequest.getUsername(), loginRequest.getUserType());
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成JWT token
        String token = jwtUtils.generateToken(user);
        log.info("生成JWT token成功: token长度={}", token.length());

        // 更新最后登录时间
        updateLastLoginTime(user.getId());

        // 构建响应结果
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        
        // 构建用户信息
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        BeanUtils.copyProperties(user, userInfo);
        userInfo.setUserTypeName(getUserTypeName(user.getUserType()));
        response.setUserInfo(userInfo);

        log.info("用户登录成功: userId={}, username={}", user.getId(), user.getUsername());
        return response;
    }

    @Override
    public User getUserByUsernameAndType(String username, Integer userType) {
        return userMapper.selectByUsernameAndType(username, userType);
    }

    @Override
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public void updateLastLoginTime(Long userId) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userId)
                    .set("last_login_time", LocalDateTime.now());
        userMapper.update(null, updateWrapper);
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