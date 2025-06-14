package com.mkstu.score.common;

/**
 * 枚举了一些常用API操作码
 *
 * @author MKStu
 * @date 2024-01-01
 */
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    
    // 用户相关
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_LOGIN_FAILED(1002, "用户名或密码错误"),
    USER_ACCOUNT_FORBIDDEN(1003, "账号已被禁用"),
    USER_ACCOUNT_NOT_ACTIVATED(1004, "账号未激活"),
    USER_HAS_EXISTED(1005, "用户已存在"),
    
    // 课程相关
    COURSE_NOT_FOUND(2001, "课程不存在"),
    COURSE_HAS_EXISTED(2002, "课程已存在"),
    
    // 成绩相关
    SCORE_NOT_FOUND(3001, "成绩不存在"),
    SCORE_HAS_EXISTED(3002, "成绩已存在"),
    
    // 班级相关
    CLASS_NOT_FOUND(4001, "班级不存在"),
    CLASS_HAS_EXISTED(4002, "班级已存在");

    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 