package com.mkstu.score.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkstu.score.entity.Course;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程数据访问层
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {
} 