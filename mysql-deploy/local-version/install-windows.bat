@echo off
chcp 65001 >nul

rem MySQL Windows 本地安装脚本
echo ====================
echo Windows 系统 MySQL 本地安装
echo ====================

echo 此脚本将指导您手动安装 MySQL
echo.

echo 📋 安装步骤:
echo 1. 下载 MySQL 安装包
echo 2. 运行安装程序
echo 3. 配置 MySQL 服务
echo 4. 创建数据库和用户
echo.

echo 🔗 下载链接:
echo    MySQL 官方下载: https://dev.mysql.com/downloads/mysql/
echo    选择 "MySQL Installer for Windows"
echo.

echo 💡 安装建议:
echo    - 选择 "Developer Default" 安装类型
echo    - 设置 root 密码为: root123456
echo    - 端口保持默认: 3306
echo    - 启用 "Start the MySQL Server at System Startup"
echo.

pause

echo 📍 安装完成后，请执行以下步骤创建项目数据库:
echo.
echo 1. 打开命令提示符 (以管理员身份运行)
echo 2. 连接到 MySQL:
echo    mysql -u root -p
echo.
echo 3. 执行以下 SQL 命令:
echo    CREATE DATABASE IF NOT EXISTS studb DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
echo    CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin123456';
echo    GRANT ALL PRIVILEGES ON studb.* TO 'admin'@'localhost';
echo    FLUSH PRIVILEGES;
echo    USE studb;
echo    SOURCE init.sql;
echo.

echo 🔧 Windows MySQL 服务管理:
echo    启动服务: net start MySQL80
echo    停止服务: net stop MySQL80
echo    重启服务: net stop MySQL80 && net start MySQL80
echo.

echo 📍 完成后的连接信息:
echo    主机: localhost
echo    端口: 3306
echo    数据库: studb
echo    用户名: admin
echo    密码: admin123456
echo.

echo 🌐 推荐 MySQL 管理工具:
echo    - MySQL Workbench (官方工具)
echo    - phpMyAdmin
echo    - Navicat for MySQL
echo    - HeidiSQL
echo.

pause 