package com.mkstu.score.vo;

import lombok.Data;

/**
 * 登录响应VO
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Data
public class LoginResponse {

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 令牌类型
     */
    private String tokenType = "Bearer";

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    @Data
    public static class UserInfo {
        /**
         * 用户ID
         */
        private Long id;

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
         * 班级ID（学生专用）
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
         * 头像地址
         */
        private String avatar;

        /**
         * 手机号
         */
        private String phone;

        /**
         * 邮箱
         */
        private String email;
    }
} 