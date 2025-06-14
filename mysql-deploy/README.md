# MySQL 快速部署指南

本文件夹提供了在 Windows 和 Mac 系统上快速部署 MySQL 数据库的两种方案：

1. **Docker 版本** - 使用 Docker 容器化部署（推荐）
2. **本地安装版本** - 传统的本地安装方式

## 🚀 快速开始

### 方案一：Docker 版本（推荐）✨ 

**优点：**
- 一键部署，无需复杂配置
- 环境隔离，不污染系统
- 跨平台一致性
- 易于卸载和重新部署
- **全自动化部署** - 自动检查和启动 Docker daemon
- **健壮性保障** - 内置健康检查和错误恢复
- **智能等待** - 自动等待 MySQL 服务完全启动

**前提条件：**
- 已安装 Docker Desktop（脚本会自动检查并尝试启动）

#### Mac/Linux 系统

```bash
cd mysql-deploy/docker-version
./start.sh    # 无需 chmod，脚本已具备执行权限
```

**新增功能：**
- 🔍 自动检测 Docker daemon 状态
- 🚀 自动启动 Docker Desktop（macOS）
- ⏱️ 智能等待服务健康检查通过
- 🏥 内置 MySQL 健康检查机制
- 🎨 彩色日志输出，状态一目了然
- 🛡️ 增强错误处理和恢复机制

#### Windows 系统

```cmd
cd mysql-deploy\docker-version
start.bat
```

**新增功能：**
- 🔍 自动检测 Docker daemon 状态
- 🚀 自动启动 Docker Desktop
- ⏱️ 智能等待服务健康检查通过
- 🎨 结构化日志输出
- 🛡️ 增强错误处理机制

#### 停止服务

**Mac/Linux:**
```bash
./stop.sh     # 优雅停止所有服务
```

**Windows:**
```cmd
stop.bat      # 优雅停止所有服务
```

### 方案二：本地安装版本

**优点：**
- 性能略优于 Docker
- 与系统集成度更高
- 可自定义配置

**缺点：**
- 安装配置相对复杂
- 可能与系统其他软件冲突

#### Mac 系统

```bash
cd mysql-deploy/local-version
chmod +x install-mac.sh
./install-mac.sh
```

#### Windows 系统

```cmd
cd mysql-deploy\local-version
install-windows.bat
```

## 📊 默认配置信息

### 数据库连接信息

| 配置项 | 值 |
|--------|-----|
| 主机 | localhost |
| 端口 | 3306 |
| 数据库名 | score_system |
| Root 用户密码 | root123456 |
| 管理员用户名 | admin |
| 管理员密码 | admin123456 |

### Docker 版本额外信息

| 服务 | 地址 | 说明 |
|------|------|------|
| MySQL | localhost:3306 | 数据库服务 |
| Adminer | http://localhost:8080 | Web 管理界面 |

## 🗄️ 数据库结构

系统预创建了学生成绩管理系统的基础表结构：

### 学生表 (students)
- `id` - 主键
- `student_id` - 学号（唯一）
- `name` - 姓名
- `gender` - 性别
- `birth_date` - 出生日期
- `class_name` - 班级

### 课程表 (courses)
- `id` - 主键
- `course_code` - 课程代码（唯一）
- `course_name` - 课程名称
- `credits` - 学分
- `teacher` - 任课教师

### 成绩表 (scores)
- `id` - 主键
- `student_id` - 学号（外键）
- `course_code` - 课程代码（外键）
- `score` - 成绩
- `semester` - 学期
- `exam_type` - 考试类型（期中/期末/平时）

## 🔧 管理命令

### Docker 版本管理 🐳

```bash
# 一键启动服务（推荐）
./start.sh

# 一键停止服务
./stop.sh

# 手动管理命令
docker compose up -d        # 启动服务
docker compose down         # 停止服务
docker compose down -v      # 停止并删除数据卷
docker compose logs -f      # 查看实时日志
docker compose restart     # 重启服务

# 进入 MySQL 容器
docker exec -it mysql-local mysql -u root -p

# 查看服务状态
docker ps --filter name=mysql-local --filter name=adminer-local
```

### 本地安装版本管理

#### Mac (使用 Homebrew)
```bash
# 启动 MySQL 服务
brew services start mysql

# 停止 MySQL 服务
brew services stop mysql

# 重启 MySQL 服务
brew services restart mysql

# 查看服务状态
brew services list | grep mysql
```

#### Windows
```cmd
# 启动 MySQL 服务
net start MySQL80

# 停止 MySQL 服务
net stop MySQL80

# 重启 MySQL 服务
net stop MySQL80 && net start MySQL80
```

## 🌐 推荐的管理工具

### 命令行工具
- **MySQL CLI** - 官方命令行客户端
- **mycli** - 增强的 MySQL 命令行工具

### 图形化工具
- **MySQL Workbench** - MySQL 官方图形化工具
- **phpMyAdmin** - 基于 Web 的 MySQL 管理工具
- **Adminer** - 轻量级 Web 数据库管理工具（Docker 版本已集成）
- **Navicat for MySQL** - 商业数据库管理工具
- **HeidiSQL** - Windows 下的免费 MySQL 客户端
- **Sequel Pro** - Mac 下的 MySQL 客户端

## 🔍 连接测试

### 使用命令行连接

```bash
# 使用 root 用户连接
mysql -h localhost -u root -p

# 使用 admin 用户连接
mysql -h localhost -u admin -p score_system
```

### 使用 Web 界面（Docker 版本）

1. 打开浏览器访问：http://localhost:8080
2. 选择数据库类型：MySQL
3. 输入连接信息：
   - 服务器：mysql-local（或 localhost）
   - 用户名：admin
   - 密码：admin123456
   - 数据库：score_system

## 🛠️ 新版本脚本特性

### ✨ 智能启动流程

1. **环境检查**
   - 自动检测 Docker 安装状态
   - 智能选择 `docker compose` 或 `docker-compose` 命令

2. **Docker daemon 管理**
   - 自动检测 Docker daemon 运行状态
   - macOS 自动启动 Docker Desktop
   - Linux 尝试启动 systemd 服务

3. **服务健康监控**
   - MySQL 内置健康检查配置
   - 智能等待服务完全启动
   - 超时保护和错误恢复

4. **用户体验优化**
   - 彩色日志输出
   - 详细的状态提示
   - 完整的连接信息展示

### 🔧 Docker Compose 优化

- 移除废弃的 `version` 属性
- 添加 MySQL 健康检查配置
- Adminer 服务依赖 MySQL 健康状态
- 优化容器启动顺序

## 🐛 常见问题及解决方案

### Docker 版本问题

**问题：Docker daemon 未运行**
```
Cannot connect to the Docker daemon at unix:///var/run/docker.sock
```
**解决方案：** 
- 新版脚本会自动检测并尝试启动 Docker Desktop
- 如果自动启动失败，请手动启动 Docker Desktop

**问题：端口被占用**
```
Bind for 0.0.0.0:3306 failed: port is already allocated
```
**解决方案：**
```bash
# 查看端口占用
lsof -i :3306
# 或者使用不同端口
# 修改 docker-compose.yml 中的端口映射
```

**问题：容器无法访问初始化脚本**
```
ERROR 2002 (HY000): Can't connect to local MySQL server
```
**解决方案：**
- 新版脚本内置健康检查，会自动等待服务完全启动
- 如果问题持续，检查 `init.sql` 文件是否存在

**问题：健康检查失败**
```
Health check failed
```
**解决方案：**
```bash
# 查看详细日志
docker compose logs mysql
# 重新启动服务
./stop.sh && ./start.sh
```

### 性能优化建议

1. **为 Docker Desktop 分配足够资源：**
   - 内存：至少 4GB
   - CPU：至少 2 核心

2. **数据持久化：**
   - 数据存储在 Docker 卷中，重启容器不会丢失数据
   - 完全清理需要使用 `docker compose down -v`

3. **网络优化：**
   - 使用专用网络 `mysql-network`
   - 避免与其他容器网络冲突

## 📈 版本更新日志

### v2.0 - 健壮性大幅提升
- ✅ 自动 Docker daemon 检测和启动
- ✅ MySQL 健康检查机制
- ✅ 智能等待和超时保护  
- ✅ 增强错误处理和日志输出
- ✅ 移除废弃配置警告
- ✅ 新增停止脚本
- ✅ 优化用户体验

### v1.0 - 基础功能
- ✅ Docker 容器化部署
- ✅ 基础启动脚本
- ✅ Web 管理界面集成

## 🤝 技术支持

如果遇到问题，请按以下步骤排查：

1. **查看脚本输出日志**
2. **检查 Docker 服务状态**
3. **查看容器日志：** `docker compose logs`
4. **重新部署：** `./stop.sh && ./start.sh`

如果问题仍然存在，请提供详细的错误信息和系统环境信息。

## 📝 自定义配置

### 修改默认密码

#### Docker 版本
编辑 `docker-version/docker-compose.yml` 文件：
```yaml
environment:
  MYSQL_ROOT_PASSWORD: 你的新密码
  MYSQL_PASSWORD: 你的新密码
```

#### 本地安装版本
```sql
# 连接到 MySQL
mysql -u root -p

# 修改密码
ALTER USER 'root'@'localhost' IDENTIFIED BY '新密码';
ALTER USER 'admin'@'localhost' IDENTIFIED BY '新密码';
FLUSH PRIVILEGES;
```

### 修改端口

#### Docker 版本
编辑 `docker-version/docker-compose.yml` 文件：
```yaml
ports:
  - "新端口:3306"
```

#### 本地安装版本
编辑 MySQL 配置文件 `my.cnf` 或 `my.ini`：
```ini
[mysqld]
port = 新端口
```

## 📞 技术支持

如果遇到问题，请按以下步骤排查：

1. 确认系统环境符合要求
2. 检查防火墙和端口设置
3. 查看相关日志文件
4. 参考官方文档

## 📄 许可证

本项目仅供学习和开发使用。 