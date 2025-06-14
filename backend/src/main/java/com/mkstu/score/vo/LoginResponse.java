package com.mkstu.score.vo;

import lombok.Data;

/**
 * 登录响应结果
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Data
public class LoginResponse {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 学号/工号
     */
    private String userNo;

    /**
     * 用户类型：1-管理员，2-教师，3-学生
     */
    private Integer userType;

    /**
     * 用户类型名称
     */
    private String userTypeName;

    /**
     * 班级ID
     */
    private Long classId;

    /**
     * 专业
     */
    private String major;

    /**
     * 年级
     */
    private String grade;

    /**
     * 头像
     */
    private String avatar;

    /**
     * JWT令牌
     */
    private String token;

    /**
     * token前缀
     */
    private String tokenHead;
} 