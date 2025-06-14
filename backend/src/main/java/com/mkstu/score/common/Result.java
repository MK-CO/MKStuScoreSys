package com.mkstu.score.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回结果
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> ok() {
        return new Result<T>((int)ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> ok(T data) {
        return new Result<T>((int)ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> ok(String message, T data) {
        return new Result<T>((int)ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed() {
        return new Result<T>((int)ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed(String message) {
        return new Result<T>((int)ResultCode.FAILED.getCode(), message);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed(ResultCode resultCode) {
        return new Result<T>((int)resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed() {
        return new Result<T>((int)ResultCode.VALIDATE_FAILED.getCode(), ResultCode.VALIDATE_FAILED.getMessage());
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result<T>((int)ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized(T data) {
        return new Result<T>((int)ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> Result<T> forbidden(T data) {
        return new Result<T>((int)ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }
} 