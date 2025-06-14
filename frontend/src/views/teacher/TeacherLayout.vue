<template>
  <el-container class="teacher-layout">
    <el-aside width="200px">
      <el-menu
        :default-active="$route.path"
        class="el-menu-vertical"
        router
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <div class="logo">
          <h3>教师系统</h3>
        </div>
        <el-menu-item index="/teacher/dashboard">
          <el-icon><House /></el-icon>
          <span>仪表板</span>
        </el-menu-item>
        <el-menu-item index="/teacher/schedule">
          <el-icon><Calendar /></el-icon>
          <span>课程安排</span>
        </el-menu-item>
        <el-menu-item index="/teacher/scores">
          <el-icon><Document /></el-icon>
          <span>成绩查询</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-content">
          <span>欢迎，{{ userStore.userInfo?.realName }}</span>
          <el-button @click="logout">退出登录</el-button>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { House, Calendar, Document } from '@element-plus/icons-vue'

const userStore = useUserStore()
const router = useRouter()

const logout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.teacher-layout {
  height: 100vh;
}

.logo {
  padding: 20px;
  text-align: center;
  color: white;
  border-bottom: 1px solid #434a50;
}

.logo h3 {
  margin: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}

.el-menu-vertical {
  height: 100%;
}
</style> 