package com.mkstu.score.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mkstu.score.entity.Course;
import com.mkstu.score.mapper.CourseMapper;
import com.mkstu.score.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 课程服务实现类
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Override
    public IPage<Course> pageCourses(Long current, Long size, String keyword) {
        Page<Course> page = new Page<>(current, size);
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            queryWrapper.and(wrapper -> wrapper
                .like(Course::getCourseName, keyword)
                .or()
                .like(Course::getCourseNo, keyword)
                .or()
                .like(Course::getCourseType, keyword)
            );
        }
        
        queryWrapper.orderByDesc(Course::getCreateTime);
        return page(page, queryWrapper);
    }

    @Override
    public Course getByCourseNo(String courseNo) {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getCourseNo, courseNo);
        return getOne(queryWrapper);
    }

    @Override
    public boolean createCourse(Course course) {
        // 检查课程编号是否已存在
        if (existsByCourseNo(course.getCourseNo())) {
            return false;
        }
        
        // 设置默认值
        if (course.getStatus() == null) {
            course.setStatus(1);
        }
        
        return save(course);
    }

    @Override
    public boolean updateCourse(Course course) {
        // 检查课程是否存在
        Course existingCourse = getById(course.getId());
        if (existingCourse == null) {
            return false;
        }
        
        // 如果修改了课程编号，需要检查新编号是否已存在
        if (!existingCourse.getCourseNo().equals(course.getCourseNo())) {
            if (existsByCourseNo(course.getCourseNo())) {
                return false;
            }
        }
        
        return updateById(course);
    }

    @Override
    public boolean deleteCourse(Long id) {
        Course course = getById(id);
        if (course == null) {
            return false;
        }
        
        return removeById(id);
    }

    @Override
    public boolean deleteCourses(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        return removeByIds(ids);
    }

    @Override
    public boolean existsByCourseNo(String courseNo) {
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Course::getCourseNo, courseNo);
        return count(queryWrapper) > 0;
    }
} 