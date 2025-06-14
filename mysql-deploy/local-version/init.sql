-- 初始化数据库脚本
-- 创建学生成绩管理系统的基础表结构

CREATE DATABASE IF NOT EXISTS studb DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE studb;

-- 创建学生表
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) UNIQUE NOT NULL COMMENT '学号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender ENUM('男', '女') NOT NULL COMMENT '性别',
    birth_date DATE COMMENT '出生日期',
    class_name VARCHAR(50) COMMENT '班级',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '学生信息表';

-- 创建课程表
CREATE TABLE IF NOT EXISTS courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL COMMENT '课程代码',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    credits DECIMAL(3,1) DEFAULT 0.0 COMMENT '学分',
    teacher VARCHAR(50) COMMENT '任课教师',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '课程信息表';

-- 创建成绩表
CREATE TABLE IF NOT EXISTS scores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL COMMENT '学号',
    course_code VARCHAR(20) NOT NULL COMMENT '课程代码',
    score DECIMAL(5,2) COMMENT '成绩',
    semester VARCHAR(20) COMMENT '学期',
    exam_type ENUM('期中', '期末', '平时') DEFAULT '期末' COMMENT '考试类型',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_student_course_semester (student_id, course_code, semester, exam_type),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (course_code) REFERENCES courses(course_code) ON DELETE CASCADE
) COMMENT '成绩信息表';

-- 插入示例数据
INSERT INTO students (student_id, name, gender, birth_date, class_name) VALUES
('2023001', '张三', '男', '2005-03-15', '计算机科学与技术1班'),
('2023002', '李四', '女', '2005-07-22', '计算机科学与技术1班'),
('2023003', '王五', '男', '2005-01-10', '计算机科学与技术2班');

INSERT INTO courses (course_code, course_name, credits, teacher) VALUES
('CS101', '程序设计基础', 3.0, '王老师'),
('MATH101', '高等数学', 4.0, '李老师'),
('ENG101', '大学英语', 2.0, '赵老师');

INSERT INTO scores (student_id, course_code, score, semester, exam_type) VALUES
('2023001', 'CS101', 85.5, '2023-1', '期末'),
('2023001', 'MATH101', 92.0, '2023-1', '期末'),
('2023002', 'CS101', 78.0, '2023-1', '期末'),
('2023002', 'ENG101', 88.5, '2023-1', '期末'); 