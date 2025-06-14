# 学生成绩管理系统 - 用户登录API文档

## 概述

本系统提供了三种用户类型的登录功能：
- 管理员登录
- 教师登录  
- 学生登录

所有登录接口都采用JWT token进行身份认证，适合前后端分离架构。

## 基础信息

- **服务地址**: http://localhost:8081/api
- **认证方式**: JWT Token
- **Token传递**: 请求头 `Authorization: Bearer {token}`

## 登录接口

### 1. 通用登录接口

**接口地址**: `POST /auth/login`

**请求参数**:
```json
{
    "username": "用户名",
    "password": "密码", 
    "userType": 1  // 1-管理员，2-教师，3-学生
}
```

**响应示例**:
```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "token": "eyJhbGciOiJIUzUxMiJ9...",
        "tokenType": "Bearer",
        "userInfo": {
            "id": 1,
            "username": "admin",
            "realName": "系统管理员",
            "userNo": "A001",
            "userType": 1,
            "userTypeName": "管理员",
            "classId": null,
            "major": "计算机管理",
            "grade": "2024",
            "avatar": null,
            "phone": "13800138001",
            "email": "admin@mkstu.com"
        }
    },
    "timestamp": 1704067200000
}
```

### 2. 管理员登录

**接口地址**: `POST /auth/admin/login`

**请求参数**:
```json
{
    "username": "admin",
    "password": "123456"
}
```

**说明**: 无需传递userType，系统自动设置为管理员类型

### 3. 教师登录

**接口地址**: `POST /auth/teacher/login`

**请求参数**:
```json
{
    "username": "teacher001", 
    "password": "123456"
}
```

**说明**: 无需传递userType，系统自动设置为教师类型

### 4. 学生登录

**接口地址**: `POST /auth/student/login`

**请求参数**:
```json
{
    "username": "student001",
    "password": "123456"
}
```

**说明**: 无需传递userType，系统自动设置为学生类型

### 5. 退出登录

**接口地址**: `POST /auth/logout`

**说明**: JWT是无状态的，客户端删除token即可实现退出

## 测试接口

### 1. 服务状态测试

**接口地址**: `GET /test/hello`

**说明**: 无需认证，用于测试服务是否正常运行

### 2. 认证测试

**接口地址**: `GET /test/auth`

**说明**: 需要认证，用于测试JWT token是否有效

**请求头**:
```
Authorization: Bearer {your_token}
```

## 预置测试账号

系统已预置以下测试账号（密码均为：123456）：

### 管理员账号
- 用户名: `admin`
- 密码: `123456`
- 类型: 管理员

### 教师账号
- 用户名: `teacher001` / 真实姓名: 张老师
- 用户名: `teacher002` / 真实姓名: 李老师
- 密码: `123456`
- 类型: 教师

### 学生账号
- 用户名: `student001` / 真实姓名: 王小明
- 用户名: `student002` / 真实姓名: 李小红  
- 用户名: `student003` / 真实姓名: 张小强
- 密码: `123456`
- 类型: 学生

## 错误码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 参数验证失败 |
| 401 | 未认证或token过期 |
| 403 | 没有相关权限 |
| 500 | 服务器内部错误 |

## 使用示例

### 前端JavaScript示例

```javascript
// 登录
async function login(username, password, userType) {
    const response = await fetch('http://localhost:8081/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username,
            password,
            userType
        })
    });
    
    const result = await response.json();
    if (result.code === 200) {
        // 保存token
        localStorage.setItem('token', result.data.token);
        localStorage.setItem('userInfo', JSON.stringify(result.data.userInfo));
        return result.data;
    } else {
        throw new Error(result.message);
    }
}

// 发送认证请求
async function apiRequest(url, options = {}) {
    const token = localStorage.getItem('token');
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };
    
    if (token) {
        headers.Authorization = `Bearer ${token}`;
    }
    
    return fetch(url, {
        ...options,
        headers
    });
}
```

### curl命令示例

```bash
# 管理员登录
curl -X POST http://localhost:8081/api/auth/admin/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'

# 使用token访问受保护接口
curl -X GET http://localhost:8081/api/test/auth \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 数据库初始化

在使用前，请确保：
1. MySQL数据库已启动
2. 执行 `backend/src/main/resources/sql/init.sql` 脚本初始化数据库和测试数据
3. 修改 `application.yml` 中的数据库连接信息

## 注意事项

1. JWT token默认有效期为7天
2. 所有密码都经过BCrypt加密存储
3. 系统支持CORS跨域访问
4. 建议在生产环境中修改JWT密钥和数据库密码 