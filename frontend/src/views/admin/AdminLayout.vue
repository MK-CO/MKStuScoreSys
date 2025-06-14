<template>
  <div class="admin-layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="250px" class="sidebar">
        <div class="logo-section">
          <h2>管理员端</h2>
          <p>欢迎, {{ userStore.userInfo?.realName }}</p>
        </div>
        
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          router
          @select="handleMenuSelect"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><Odometer /></el-icon>
            <span>控制台</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/course-info">
            <el-icon><Reading /></el-icon>
            <span>课程信息管理</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/schedule">
            <el-icon><Calendar /></el-icon>
            <span>课程表管理</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/score-info">
            <el-icon><DataLine /></el-icon>
            <span>成绩信息管理</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/score-statistics">
            <el-icon><TrendCharts /></el-icon>
            <span>成绩统计分析</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/users">
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
          
          <el-menu-item index="/admin/accounts">
            <el-icon><Setting /></el-icon>
            <span>账号管理</span>
          </el-menu-item>
        </el-menu>
        
        <div class="logout-section">
          <el-button 
            type="danger" 
            plain 
            @click="handleLogout"
            class="logout-btn"
          >
            <el-icon><SwitchButton /></el-icon>
            退出登录
          </el-button>
        </div>
      </el-aside>
      
      <!-- 主要内容区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Odometer, 
  Reading, 
  Calendar, 
  DataLine, 
  TrendCharts, 
  User, 
  Setting, 
  SwitchButton 
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 处理菜单选择
const handleMenuSelect = (index) => {
  router.push(index)
}

// 处理退出登录
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    userStore.logout()
    ElMessage.success('退出登录成功')
    router.push('/login')
  } catch {
    // 用户取消操作
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

.sidebar {
  background: #304156;
  color: white;
  display: flex;
  flex-direction: column;
}

.logo-section {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid #404854;
}

.logo-section h2 {
  margin: 0 0 8px 0;
  color: #fff;
  font-size: 1.5rem;
}

.logo-section p {
  margin: 0;
  color: #909399;
  font-size: 0.9rem;
}

.sidebar-menu {
  flex: 1;
  border: none;
  background: #304156;
}

.sidebar-menu .el-menu-item {
  color: #bfcbd9;
  height: 50px;
  line-height: 50px;
  margin: 0 10px;
  border-radius: 4px;
}

.sidebar-menu .el-menu-item:hover {
  background-color: #4a5568;
  color: #fff;
}

.sidebar-menu .el-menu-item.is-active {
  background-color: #409eff;
  color: #fff;
}

.logout-section {
  padding: 20px;
  border-top: 1px solid #404854;
}

.logout-btn {
  width: 100%;
  color: #909399;
  border-color: #909399;
}

.logout-btn:hover {
  color: #f56c6c;
  border-color: #f56c6c;
}

.main-content {
  background: #f5f5f5;
  padding: 20px;
}
</style> 