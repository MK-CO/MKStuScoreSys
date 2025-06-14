<template>
  <div class="student-schedule">
    <div class="page-header">
      <h2>我的课程表</h2>
      <p>查看您的课程安排和时间表</p>
    </div>
    
    <el-card shadow="hover">
      <div class="placeholder-content">
        <el-icon size="64" color="#409eff"><Calendar /></el-icon>
        <h3>课程表功能</h3>
        <p>此功能正在开发中，敬请期待...</p>
        <el-button type="primary" @click="goBack">返回个人中心</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Calendar } from '@element-plus/icons-vue'

const router = useRouter()

const goBack = () => {
  router.push('/student/dashboard')
}
</script>

<style scoped>
.student-schedule {
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #333;
}

.page-header p {
  margin: 0;
  color: #666;
}

.placeholder-content {
  text-align: center;
  padding: 60px 20px;
}

.placeholder-content h3 {
  margin: 20px 0 10px 0;
  color: #333;
}

.placeholder-content p {
  margin: 0 0 30px 0;
  color: #666;
}
</style> 