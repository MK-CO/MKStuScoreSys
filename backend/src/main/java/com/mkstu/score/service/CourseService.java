package com.mkstu.score.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mkstu.score.entity.Course;

import java.util.List;

/**
 * 课程服务接口
 *
 * @author MKStu
 * @date 2024-01-01
 */
public interface CourseService extends IService<Course> {

    /**
     * 分页查询课程列表
     *
     * @param current   当前页
     * @param size      每页大小
     * @param keyword   搜索关键字
     * @return 分页结果
     */
    IPage<Course> pageCourses(Long current, Long size, String keyword);

    /**
     * 根据课程编号查询课程
     *
     * @param courseNo 课程编号
     * @return 课程信息
     */
    Course getByCourseNo(String courseNo);

    /**
     * 创建课程
     *
     * @param course 课程信息
     * @return 是否成功
     */
    boolean createCourse(Course course);

    /**
     * 更新课程信息
     *
     * @param course 课程信息
     * @return 是否成功
     */
    boolean updateCourse(Course course);

    /**
     * 删除课程
     *
     * @param id 课程ID
     * @return 是否成功
     */
    boolean deleteCourse(Long id);

    /**
     * 批量删除课程
     *
     * @param ids 课程ID列表
     * @return 是否成功
     */
    boolean deleteCourses(List<Long> ids);

    /**
     * 检查课程编号是否存在
     *
     * @param courseNo 课程编号
     * @return 是否存在
     */
    boolean existsByCourseNo(String courseNo);
} 