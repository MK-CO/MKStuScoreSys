package com.mkstu.score.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程实体类
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程编号
     */
    @TableField("course_no")
    private String courseNo;

    /**
     * 课程名称
     */
    @TableField("course_name")
    private String courseName;

    /**
     * 学分
     */
    @TableField("credit")
    private BigDecimal credit;

    /**
     * 课程类型
     */
    @TableField("course_type")
    private String courseType;

    /**
     * 任课教师ID
     */
    @TableField("teacher_id")
    private Long teacherId;

    /**
     * 学期
     */
    @TableField("semester")
    private String semester;

    /**
     * 课程描述
     */
    @TableField("description")
    private String description;

    /**
     * 状态：0-禁用，1-正常
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
} 