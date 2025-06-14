package com.mkstu.score.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 课程请求DTO
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Data
public class CourseRequest {

    /**
     * 课程ID（编辑时使用）
     */
    private Long id;

    /**
     * 课程编号
     */
    @NotBlank(message = "课程编号不能为空")
    @Size(max = 20, message = "课程编号长度不能超过20个字符")
    private String courseNo;

    /**
     * 课程名称
     */
    @NotBlank(message = "课程名称不能为空")
    @Size(max = 100, message = "课程名称长度不能超过100个字符")
    private String courseName;

    /**
     * 学分
     */
    @NotNull(message = "学分不能为空")
    @DecimalMin(value = "0.0", message = "学分不能小于0")
    @DecimalMax(value = "10.0", message = "学分不能大于10")
    private BigDecimal credit;

    /**
     * 课程类型
     */
    @NotBlank(message = "课程类型不能为空")
    @Size(max = 50, message = "课程类型长度不能超过50个字符")
    private String courseType;

    /**
     * 任课教师ID
     */
    @NotNull(message = "任课教师ID不能为空")
    private Long teacherId;

    /**
     * 学期
     */
    @NotBlank(message = "学期不能为空")
    @Size(max = 20, message = "学期长度不能超过20个字符")
    private String semester;

    /**
     * 课程描述
     */
    @Size(max = 500, message = "课程描述长度不能超过500个字符")
    private String description;

    /**
     * 状态：0-禁用，1-正常
     */
    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值无效")
    @Max(value = 1, message = "状态值无效")
    private Integer status;
} 