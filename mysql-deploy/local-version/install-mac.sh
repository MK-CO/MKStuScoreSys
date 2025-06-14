#!/bin/bash

# MySQL Mac 本地安装脚本
echo "===================="
echo "Mac 系统 MySQL 本地安装"
echo "===================="

# 检查是否安装了 Homebrew
if ! command -v brew &> /dev/null; then
    echo "正在安装 Homebrew..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi

# 更新 Homebrew
echo "更新 Homebrew..."
brew update

# 安装 MySQL
echo "正在安装 MySQL..."
brew install mysql

# 启动 MySQL 服务
echo "启动 MySQL 服务..."
brew services start mysql

# 等待服务启动
sleep 5

# 安全设置 MySQL
echo "配置 MySQL 安全设置..."
echo "请按照提示设置 root 密码等安全选项"
mysql_secure_installation

# 创建数据库和用户
echo "创建数据库和用户..."
mysql -u root -p << EOF
CREATE DATABASE IF NOT EXISTS score_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin123456';
GRANT ALL PRIVILEGES ON score_system.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;
EOF

# 导入初始数据
echo "正在导入初始数据..."
mysql -u root -p score_system < init.sql

echo "✅ MySQL 安装完成!"
echo "📍 MySQL 连接信息:"
echo "   主机: localhost"
echo "   端口: 3306"
echo "   数据库: score_system"
echo "   管理员用户: admin"
echo "   管理员密码: admin123456"
echo ""
echo "🔧 MySQL 服务管理命令:"
echo "   启动: brew services start mysql"
echo "   停止: brew services stop mysql"
echo "   重启: brew services restart mysql"
echo "   状态: brew services list | grep mysql" 