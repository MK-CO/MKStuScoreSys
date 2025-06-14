package com.mkstu.score;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学生成绩管理系统启动类
 * 
 * @author MKStu
 * @date 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.mkstu.score.mapper")
public class ScoreSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoreSystemApplication.class, args);
        System.out.println("===== 学生成绩管理系统启动成功 =====");
    }
} 