-- 学生成绩管理系统数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS score_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE score_system;

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `user_no` varchar(50) NOT NULL COMMENT '学号/工号',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别：0-女，1-男',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `user_type` tinyint(1) NOT NULL COMMENT '用户类型：1-管理员，2-教师，3-学生',
  `class_id` bigint(20) DEFAULT NULL COMMENT '班级ID（学生专用）',
  `major` varchar(100) DEFAULT NULL COMMENT '专业',
  `grade` varchar(20) DEFAULT NULL COMMENT '年级',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '账号状态：0-禁用，1-正常',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username_type` (`username`,`user_type`),
  UNIQUE KEY `uk_user_no_type` (`user_no`,`user_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 班级表
DROP TABLE IF EXISTS `sys_class`;
CREATE TABLE `sys_class` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `class_name` varchar(100) NOT NULL COMMENT '班级名称',
  `class_no` varchar(50) NOT NULL COMMENT '班级编号',
  `major` varchar(100) NOT NULL COMMENT '专业',
  `grade` varchar(20) NOT NULL COMMENT '年级',
  `teacher_id` bigint(20) DEFAULT NULL COMMENT '班主任ID',
  `student_count` int(11) DEFAULT '0' COMMENT '学生人数',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_class_no` (`class_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 课程表
DROP TABLE IF EXISTS `sys_course`;
CREATE TABLE `sys_course` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_name` varchar(100) NOT NULL COMMENT '课程名称',
  `course_no` varchar(50) NOT NULL COMMENT '课程编号',
  `course_type` varchar(50) DEFAULT NULL COMMENT '课程类型',
  `credit` decimal(3,1) DEFAULT NULL COMMENT '学分',
  `teacher_id` bigint(20) DEFAULT NULL COMMENT '任课教师ID',
  `semester` varchar(20) DEFAULT NULL COMMENT '学期',
  `description` text COMMENT '课程描述',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_course_no` (`course_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 成绩表
DROP TABLE IF EXISTS `sys_score`;
CREATE TABLE `sys_score` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` bigint(20) NOT NULL COMMENT '学生ID',
  `course_id` bigint(20) NOT NULL COMMENT '课程ID',
  `teacher_id` bigint(20) NOT NULL COMMENT '任课教师ID',
  `score` decimal(5,2) DEFAULT NULL COMMENT '成绩',
  `grade_point` decimal(3,2) DEFAULT NULL COMMENT '绩点',
  `exam_type` varchar(20) DEFAULT NULL COMMENT '考试类型：期中、期末、平时',
  `semester` varchar(20) NOT NULL COMMENT '学期',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_course_semester` (`student_id`,`course_id`,`semester`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

-- 课程表（时间表）
DROP TABLE IF EXISTS `sys_schedule`;
CREATE TABLE `sys_schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `course_id` bigint(20) NOT NULL COMMENT '课程ID',
  `class_id` bigint(20) NOT NULL COMMENT '班级ID',
  `teacher_id` bigint(20) NOT NULL COMMENT '教师ID',
  `week_day` tinyint(1) NOT NULL COMMENT '星期几：1-7',
  `start_time` varchar(10) NOT NULL COMMENT '开始时间',
  `end_time` varchar(10) NOT NULL COMMENT '结束时间',
  `classroom` varchar(50) DEFAULT NULL COMMENT '教室',
  `semester` varchar(20) NOT NULL COMMENT '学期',
  `weeks` varchar(100) DEFAULT NULL COMMENT '上课周数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 插入初始数据
-- 管理员用户（密码：123456）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `user_no`, `gender`, `phone`, `email`, `user_type`, `major`, `grade`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKTx.QorwvRLDXXpLkxDqWgWnMu6', '系统管理员', 'A001', 1, '13800138001', 'admin@mkstu.com', 1, '计算机管理', '2024', 1);

-- 教师用户（密码：123456）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `user_no`, `gender`, `phone`, `email`, `user_type`, `major`, `grade`, `status`) VALUES
('teacher001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKTx.QorwvRLDXXpLkxDqWgWnMu6', '张老师', 'T001', 1, '13800138002', 'teacher001@mkstu.com', 2, '计算机科学与技术', '2024', 1),
('teacher002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKTx.QorwvRLDXXpLkxDqWgWnMu6', '李老师', 'T002', 0, '13800138003', 'teacher002@mkstu.com', 2, '软件工程', '2024', 1);

-- 班级数据
INSERT INTO `sys_class` (`class_name`, `class_no`, `major`, `grade`, `teacher_id`, `student_count`) VALUES
('计算机科学与技术2021级1班', 'CS2021-1', '计算机科学与技术', '2021', 2, 30),
('软件工程2021级1班', 'SE2021-1', '软件工程', '2021', 3, 28);

-- 学生用户（密码：123456）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `user_no`, `gender`, `phone`, `email`, `user_type`, `class_id`, `major`, `grade`, `status`) VALUES
('student001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKTx.QorwvRLDXXpLkxDqWgWnMu6', '王小明', '2021001', 1, '13800138004', 'student001@mkstu.com', 3, 1, '计算机科学与技术', '2021', 1),
('student002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKTx.QorwvRLDXXpLkxDqWgWnMu6', '李小红', '2021002', 0, '13800138005', 'student002@mkstu.com', 3, 1, '计算机科学与技术', '2021', 1),
('student003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKTx.QorwvRLDXXpLkxDqWgWnMu6', '张小强', '2021003', 1, '13800138006', 'student003@mkstu.com', 3, 2, '软件工程', '2021', 1);

-- 课程数据
INSERT INTO `sys_course` (`course_name`, `course_no`, `course_type`, `credit`, `teacher_id`, `semester`, `description`) VALUES
('Java程序设计', 'CS001', '专业必修', 4.0, 2, '2024-1', 'Java编程语言基础与应用开发'),
('数据库原理', 'CS002', '专业必修', 3.5, 2, '2024-1', '数据库系统原理与SQL应用'),
('软件工程', 'SE001', '专业必修', 3.0, 3, '2024-1', '软件开发生命周期与项目管理'),
('计算机网络', 'CS003', '专业必修', 3.5, 2, '2024-1', '计算机网络协议与应用');

-- 成绩数据
INSERT INTO `sys_score` (`student_id`, `course_id`, `teacher_id`, `score`, `grade_point`, `exam_type`, `semester`) VALUES
(4, 1, 2, 92.00, 4.2, '期末', '2024-1'),
(4, 2, 2, 88.50, 3.8, '期末', '2024-1'),
(5, 1, 2, 85.00, 3.5, '期末', '2024-1'),
(5, 2, 2, 90.00, 4.0, '期末', '2024-1'),
(6, 3, 3, 87.50, 3.7, '期末', '2024-1'); 