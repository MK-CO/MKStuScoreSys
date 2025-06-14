package com.mkstu.score.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mkstu.score.common.Result;
import com.mkstu.score.dto.CourseRequest;
import com.mkstu.score.entity.Course;
import com.mkstu.score.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程管理控制器
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 分页查询课程列表
     */
    @GetMapping("/page")
    public Result<IPage<Course>> pageCourses(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "20") Long size,
            @RequestParam(required = false) String keyword) {
        try {
            IPage<Course> page = courseService.pageCourses(current, size, keyword);
            return Result.success("查询成功", page);
        } catch (Exception e) {
            log.error("分页查询课程失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询课程详情
     */
    @GetMapping("/{id}")
    public Result<Course> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.getById(id);
            if (course == null) {
                return Result.error("课程不存在");
            }
            return Result.success("查询成功", course);
        } catch (Exception e) {
            log.error("查询课程详情失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据课程编号查询课程
     */
    @GetMapping("/no/{courseNo}")
    public Result<Course> getCourseByCourseNo(@PathVariable String courseNo) {
        try {
            Course course = courseService.getByCourseNo(courseNo);
            if (course == null) {
                return Result.error("课程不存在");
            }
            return Result.success("查询成功", course);
        } catch (Exception e) {
            log.error("根据课程编号查询课程失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建课程
     */
    @PostMapping
    public Result<String> createCourse(@Validated @RequestBody CourseRequest courseRequest) {
        try {
            // 检查课程编号是否已存在
            if (courseService.existsByCourseNo(courseRequest.getCourseNo())) {
                return Result.error("课程编号已存在");
            }

            Course course = new Course();
            BeanUtils.copyProperties(courseRequest, course);
            
            boolean success = courseService.createCourse(course);
            if (success) {
                return Result.success("创建成功");
            } else {
                return Result.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建课程失败", e);
            return Result.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新课程信息
     */
    @PutMapping("/{id}")
    public Result<String> updateCourse(@PathVariable Long id, 
                                     @Validated @RequestBody CourseRequest courseRequest) {
        try {
            Course course = new Course();
            BeanUtils.copyProperties(courseRequest, course);
            course.setId(id);
            
            boolean success = courseService.updateCourse(course);
            if (success) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败，课程不存在或课程编号已被使用");
            }
        } catch (Exception e) {
            log.error("更新课程失败", e);
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteCourse(@PathVariable Long id) {
        try {
            boolean success = courseService.deleteCourse(id);
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败，课程不存在");
            }
        } catch (Exception e) {
            log.error("删除课程失败", e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除课程
     */
    @DeleteMapping("/batch")
    public Result<String> deleteCourses(@RequestBody List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return Result.validateFailed("请选择要删除的课程");
            }
            
            boolean success = courseService.deleteCourses(ids);
            if (success) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除课程失败", e);
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 检查课程编号是否存在
     */
    @GetMapping("/check-no/{courseNo}")
    public Result<Boolean> checkCourseNo(@PathVariable String courseNo) {
        try {
            boolean exists = courseService.existsByCourseNo(courseNo);
            return Result.success("检查完成", exists);
        } catch (Exception e) {
            log.error("检查课程编号失败", e);
            return Result.error("检查失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有课程列表（不分页）
     */
    @GetMapping("/all")
    public Result<List<Course>> getAllCourses() {
        try {
            List<Course> courses = courseService.list();
            return Result.success("查询成功", courses);
        } catch (Exception e) {
            log.error("查询所有课程失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
} 