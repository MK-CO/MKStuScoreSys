<template>
  <div class="home-container">
    <!-- 顶部导航 -->
    <nav class="navbar">
      <div class="nav-brand">
        <div class="nav-logo">
          <svg viewBox="0 0 1024 1024" width="32" height="32">
            <path fill="#4f46e5" d="M512 85.333333a426.666667 426.666667 0 1 1 0 853.333334 426.666667 426.666667 0 0 1 0-853.333334z m0 85.333334a341.333333 341.333333 0 1 0 0 682.666666 341.333333 341.333333 0 0 0 0-682.666666z m-149.333333 170.666666h298.666666v85.333334H362.666667v-85.333334z m0 170.666667h298.666666v85.333333H362.666667v-85.333333z m0 170.666667h213.333333v85.333333H362.666667v-85.333333z"/>
          </svg>
          <span class="nav-title">学生成绩管理系统</span>
        </div>
      </div>
      <div class="nav-actions">
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">
            <el-avatar :size="32" :src="userInfo?.avatar">
              {{ userInfo?.username?.charAt(0)?.toUpperCase() }}
            </el-avatar>
            <span class="username">{{ userInfo?.username }}</span>
            <el-icon><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人信息</el-dropdown-item>
              <el-dropdown-item command="settings">设置</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </nav>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <!-- 欢迎区域 -->
      <section class="welcome-section">
        <div class="container">
          <div class="welcome-content">
            <div class="welcome-text">
              <h1 class="welcome-title">
                欢迎回来，{{ userInfo?.username }}！
              </h1>
              <p class="welcome-subtitle">
                {{ getUserTypeText(userInfo?.userType) }}
              </p>
              <div class="welcome-actions">
                <el-button type="primary" size="large" @click="navigateToUserHome">
                  进入{{ getUserTypeText(userInfo?.userType) }}工作台
                </el-button>
              </div>
            </div>
            <div class="welcome-image">
              <div class="floating-card" @click="handleFloatingCardClick('analytics')">
                <div class="card-content">
                  <div class="card-icon">📊</div>
                  <div class="card-text">数据分析</div>
                </div>
              </div>
              <div class="floating-card delay-1" @click="handleFloatingCardClick('courses')">
                <div class="card-content">
                  <div class="card-icon">📚</div>
                  <div class="card-text">课程管理</div>
                </div>
              </div>
              <div class="floating-card delay-2" @click="handleFloatingCardClick('scores')">
                <div class="card-content">
                  <div class="card-icon">🎯</div>
                  <div class="card-text">成绩统计</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 快速入口区域 -->
      <section class="quick-access-section">
        <div class="container">
          <h2 class="section-title">快速入口</h2>
          <div class="quick-access-grid">
            <div 
              v-for="item in quickAccessItems" 
              :key="item.name"
              class="quick-access-item"
              @click="navigateTo(item.path)"
            >
              <div class="item-icon">{{ item.icon }}</div>
              <div class="item-content">
                <h3 class="item-title">{{ item.name }}</h3>
                <p class="item-description">{{ item.description }}</p>
              </div>
              <div class="item-arrow">
                <el-icon><arrow-right /></el-icon>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 系统信息区域 -->
      <section class="system-info-section">
        <div class="container">
          <h2 class="section-title">系统概览</h2>
          <div class="info-cards">
            <div class="info-card">
              <div class="info-card-header">
                <div class="info-card-icon">👥</div>
                <h3>用户统计</h3>
              </div>
              <div class="info-card-content">
                <div class="stat-item">
                  <span class="stat-label">总用户数</span>
                  <span class="stat-value">1,234</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">活跃用户</span>
                  <span class="stat-value">892</span>
                </div>
              </div>
            </div>
            
            <div class="info-card">
              <div class="info-card-header">
                <div class="info-card-icon">📚</div>
                <h3>课程信息</h3>
              </div>
              <div class="info-card-content">
                <div class="stat-item">
                  <span class="stat-label">总课程数</span>
                  <span class="stat-value">156</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">本学期课程</span>
                  <span class="stat-value">48</span>
                </div>
              </div>
            </div>
            
            <div class="info-card">
              <div class="info-card-header">
                <div class="info-card-icon">📊</div>
                <h3>成绩统计</h3>
              </div>
              <div class="info-card-content">
                <div class="stat-item">
                  <span class="stat-label">已录入成绩</span>
                  <span class="stat-value">2,456</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">平均分</span>
                  <span class="stat-value">85.6</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { ArrowDown, ArrowRight } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const userInfo = computed(() => userStore.userInfo)

// 获取用户类型文本
const getUserTypeText = (userType) => {
  switch (userType) {
    case 1:
      return '管理员'
    case 2:
      return '教师'
    case 3:
      return '学生'
    default:
      return '用户'
  }
}

// 根据用户类型获取快速入口项目
const quickAccessItems = computed(() => {
  const userType = userInfo.value?.userType
  const baseItems = []
  
  switch (userType) {
    case 1: // 管理员
      baseItems.push(
        { name: '管理控制台', description: '系统管理和配置', icon: '🎛️', path: '/admin' },
        { name: '用户管理', description: '管理系统用户账户', icon: '👥', path: '/admin/users' },
        { name: '课程管理', description: '管理课程信息', icon: '📚', path: '/admin/course-info' },
        { name: '成绩统计', description: '查看系统成绩统计', icon: '📊', path: '/admin/score-statistics' }
      )
      break
    case 2: // 教师
      baseItems.push(
        { name: '教师工作台', description: '教师专用功能面板', icon: '👨‍🏫', path: '/teacher' },
        { name: '课程安排', description: '查看和管理课程安排', icon: '📅', path: '/teacher/schedule' },
        { name: '成绩查询', description: '查询和管理学生成绩', icon: '📋', path: '/teacher/scores' }
      )
      break
    case 3: // 学生
      baseItems.push(
        { name: '学生中心', description: '学生个人学习中心', icon: '👨‍🎓', path: '/student' },
        { name: '课程表', description: '查看个人课程安排', icon: '📅', path: '/student/schedule' },
        { name: '成绩查询', description: '查看个人成绩信息', icon: '📊', path: '/student/scores' }
      )
      break
  }
  
  return baseItems
})

// 导航到用户对应的主页
const navigateToUserHome = () => {
  const userType = userInfo.value?.userType
  
  // 检查用户类型是否存在
  if (!userType) {
    ElMessage.error('用户类型未知，请重新登录')
    userStore.logout()
    router.push('/login')
    return
  }
  
  switch (userType) {
    case 1:
      router.push('/admin/dashboard')
      break
    case 2:
      router.push('/teacher/dashboard')
      break
    case 3:
      router.push('/student/dashboard')
      break
    default:
      ElMessage.error('用户类型未知，请重新登录')
      userStore.logout()
      router.push('/login')
  }
}

// 导航到指定路径
const navigateTo = (path) => {
  router.push(path)
}

// 处理下拉菜单命令
const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      ElMessage.info('个人信息功能开发中...')
      break
    case 'settings':
      ElMessage.info('设置功能开发中...')
      break
    case 'logout':
      userStore.logout()
      router.push('/login')
      break
  }
}

// 处理浮动卡片点击事件
const handleFloatingCardClick = (cardType) => {
  const userType = userInfo.value?.userType
  
  switch (cardType) {
    case 'analytics':
      // 数据分析 - 根据用户类型跳转到相应的统计页面
      switch (userType) {
        case 1: // 管理员
          router.push('/admin/score-statistics')
          break
        case 2: // 教师
          router.push('/teacher/scores')
          break
        case 3: // 学生
          router.push('/student/scores')
          break
        default:
          ElMessage.warning('请先登录')
      }
      break
      
    case 'courses':
      // 课程管理 - 根据用户类型跳转到相应的课程页面
      switch (userType) {
        case 1: // 管理员
          router.push('/admin/course-info')
          break
        case 2: // 教师
          router.push('/teacher/schedule')
          break
        case 3: // 学生
          router.push('/student/schedule')
          break
        default:
          ElMessage.warning('请先登录')
      }
      break
      
    case 'scores':
      // 成绩统计 - 根据用户类型跳转到相应的成绩页面
      switch (userType) {
        case 1: // 管理员
          router.push('/admin/score-info')
          break
        case 2: // 教师
          router.push('/teacher/scores')
          break
        case 3: // 学生
          router.push('/student/scores')
          break
        default:
          ElMessage.warning('请先登录')
      }
      break
      
    default:
      ElMessage.info('功能开发中...')
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

/* 导航栏样式 */
.navbar {
  background: white;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-brand {
  display: flex;
  align-items: center;
}

.nav-logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.nav-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.nav-actions {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.el-dropdown-link:hover {
  background-color: #f3f4f6;
}

.username {
  font-size: 14px;
  color: #374151;
}

/* 主要内容区域 */
.main-content {
  padding: 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

/* 欢迎区域 */
.welcome-section {
  padding: 60px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  position: relative;
  overflow: hidden;
}

.welcome-content {
  display: flex;
  align-items: center;
  min-height: 400px;
  gap: 40px;
}

.welcome-text {
  flex: 1;
}

.welcome-title {
  font-size: 3rem;
  font-weight: 700;
  margin: 0 0 16px 0;
  line-height: 1.2;
}

.welcome-subtitle {
  font-size: 1.25rem;
  opacity: 0.9;
  margin: 0 0 32px 0;
}

.welcome-actions {
  display: flex;
  gap: 16px;
}

.welcome-image {
  flex: 0 0 300px;
  position: relative;
  height: 300px;
}

.floating-card {
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 20px;
  animation: float 6s ease-in-out infinite;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.floating-card:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-5px) scale(1.05);
  box-shadow: 0 8px 32px rgba(255, 255, 255, 0.3);
  animation-play-state: paused;
}

.floating-card:active {
  transform: translateY(-3px) scale(1.02);
}

.floating-card:nth-child(1) {
  top: 20px;
  right: 20px;
  animation-delay: 0s;
}

.floating-card:nth-child(2) {
  top: 120px;
  right: 80px;
  animation-delay: 2s;
}

.floating-card:nth-child(3) {
  top: 220px;
  right: 40px;
  animation-delay: 4s;
}

.card-content {
  text-align: center;
  user-select: none;
}

.card-icon {
  font-size: 24px;
  margin-bottom: 8px;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.card-text {
  font-size: 14px;
  font-weight: 500;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-20px); }
}

/* 快速入口区域 */
.quick-access-section {
  padding: 60px 0;
  background: white;
}

.section-title {
  font-size: 2rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 40px 0;
  text-align: center;
}

.quick-access-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 24px;
  max-width: 100%;
}

.quick-access-item {
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  min-height: 100px;
}

.quick-access-item:hover {
  border-color: #4f46e5;
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(79, 70, 229, 0.12);
}

.item-icon {
  font-size: 32px;
  min-width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.item-content {
  flex: 1;
  min-width: 0;
}

.item-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 4px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.item-description {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
  line-height: 1.4;
}

.item-arrow {
  color: #9ca3af;
  transition: transform 0.2s;
  flex-shrink: 0;
}

.quick-access-item:hover .item-arrow {
  transform: translateX(4px);
  color: #4f46e5;
}

/* 系统信息区域 */
.system-info-section {
  padding: 60px 0;
  background: #f8fafc;
}

.info-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.info-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.info-card-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
}

.info-card-icon {
  font-size: 24px;
}

.info-card-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.info-card-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-label {
  font-size: 14px;
  color: #6b7280;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .quick-access-grid {
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  }
  
  .info-cards {
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  }
}

@media (max-width: 768px) {
  .welcome-content {
    flex-direction: column;
    text-align: center;
    gap: 40px;
  }
  
  .welcome-text {
    flex: none;
  }
  
  .welcome-image {
    flex: none;
    margin-top: 0;
  }
  
  .welcome-title {
    font-size: 2rem;
  }
  
  .floating-card {
    position: relative;
    display: inline-block;
    margin: 10px;
    animation: none;
  }
  
  .floating-card:nth-child(1),
  .floating-card:nth-child(2),
  .floating-card:nth-child(3) {
    position: relative;
    top: auto;
    right: auto;
  }
  
  .floating-card:hover {
    transform: scale(1.05);
    animation-play-state: running;
  }
  
  .quick-access-grid {
    grid-template-columns: 1fr;
  }
  
  .info-cards {
    grid-template-columns: 1fr;
  }
  
  .item-title {
    white-space: normal;
    overflow: visible;
    text-overflow: unset;
  }
  
  .navbar {
    padding: 0 16px;
  }
  
  .nav-title {
    font-size: 16px;
  }
  
  .username {
    display: none;
  }
  
  .el-dropdown-link {
    padding: 8px;
    gap: 4px;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 0 16px;
  }
  
  .welcome-section {
    padding: 40px 0;
  }
  
  .quick-access-section,
  .system-info-section {
    padding: 40px 0;
  }
  
  .quick-access-item {
    padding: 20px;
    min-height: 80px;
  }
  
  .item-icon {
    font-size: 28px;
    min-width: 40px;
    height: 40px;
  }
  
  .item-title {
    font-size: 16px;
  }
  
  .item-description {
    font-size: 13px;
  }
  
  .navbar {
    padding: 0 12px;
  }
  
  .nav-title {
    font-size: 14px;
  }
}
</style> 