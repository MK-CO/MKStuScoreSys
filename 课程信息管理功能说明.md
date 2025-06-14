# 课程信息管理功能说明

## 功能概述

课程信息管理功能提供了完整的课程CRUD操作，包括课程的查询、创建、更新和删除功能。支持分页查询、搜索过滤、批量操作等特性。

## 技术架构

### 后端技术栈
- **Spring Boot 2.7.0** - 主框架
- **MyBatis Plus 3.5.2** - ORM框架
- **MySQL** - 数据库
- **Spring Validation** - 数据验证
- **Lombok** - 代码简化

### 前端技术栈
- **Vue 3** - 前端框架
- **Element Plus** - UI组件库
- **Axios** - HTTP客户端
- **Pinia** - 状态管理

## 已创建的文件

### 后端文件
1. `backend/src/main/java/com/mkstu/score/entity/Course.java` - 课程实体类
2. `backend/src/main/java/com/mkstu/score/mapper/CourseMapper.java` - 数据访问层
3. `backend/src/main/java/com/mkstu/score/service/CourseService.java` - 服务接口
4. `backend/src/main/java/com/mkstu/score/service/impl/CourseServiceImpl.java` - 服务实现
5. `backend/src/main/java/com/mkstu/score/controller/CourseController.java` - 控制器
6. `backend/src/main/java/com/mkstu/score/dto/CourseRequest.java` - 请求DTO

### 前端文件
1. `frontend/src/api/course.js` - API调用文件
2. `frontend/src/views/admin/CourseInfoManagement.vue` - 课程管理页面（已更新）

### 数据库文件
1. `mysql-deploy/local-version/init.sql` - 数据库初始化脚本（已更新）

## 主要功能

1. **分页查询** - 支持分页浏览课程列表，可按关键字搜索
2. **添加课程** - 表单验证，支持所有课程字段录入
3. **编辑课程** - 修改现有课程信息
4. **删除课程** - 单个删除和批量删除
5. **课程详情** - 查看课程完整信息
6. **数据验证** - 前后端双重验证，确保数据完整性

## 使用说明

### 1. 启动后端服务
```bash
cd backend
mvn spring-boot:run
```

### 2. 启动前端服务
```bash
cd frontend
npm run dev
```

### 3. 初始化数据库
执行 `mysql-deploy/local-version/init.sql` 脚本

### 4. 访问页面
登录系统后，在管理员界面中找到"课程信息管理"菜单

## API接口

### 基础路径
```
http://localhost:8081/api/courses
```

### 主要接口
- `GET /page` - 分页查询课程列表
- `POST /` - 创建课程
- `PUT /{id}` - 更新课程
- `DELETE /{id}` - 删除课程
- `DELETE /batch` - 批量删除

## 数据验证

- 课程编码：必填，最大20字符，唯一
- 课程名称：必填，最大100字符
- 学分：必填，0.0-10.0范围
- 课程类型：必填，1-必修课，2-选修课，3-实践课
- 开课院系：必填，最大50字符
- 授课教师：必填，最大50字符
- 课程容量：必填，1-500范围

## 注意事项

1. 课程编码具有唯一性约束
2. 采用软删除机制
3. 支持模糊搜索（课程名称、编码、教师、院系）
4. 新课程默认已选人数为0
5. 需要管理员权限访问 