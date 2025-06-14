<template>
  <div class="teacher-dashboard">
    <div class="dashboard-header">
      <h2>教师工作台</h2>
      <p>欢迎回来，{{ userInfo?.realName || userInfo?.username }}老师</p>
    </div>
    
    <!-- 统计卡片 -->
    <div class="stats-grid">
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon course-icon">
            <el-icon size="24"><Reading /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.myCourses }}</div>
            <div class="stat-label">我的课程</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon student-icon">
            <el-icon size="24"><UserFilled /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.myStudents }}</div>
            <div class="stat-label">我的学生</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon score-icon">
            <el-icon size="24"><DataLine /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.pendingScores }}</div>
            <div class="stat-label">待录入成绩</div>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 快速操作 -->
    <div class="quick-actions">
      <el-card shadow="hover">
        <template #header>
          <span>快速操作</span>
        </template>
        <div class="action-buttons">
          <el-button type="primary" @click="goToPage('/teacher/schedule')">
            <el-icon><Calendar /></el-icon>
            课程安排
          </el-button>
          <el-button type="success" @click="goToPage('/teacher/scores')">
            <el-icon><DataLine /></el-icon>
            成绩管理
          </el-button>
        </div>
      </el-card>
    </div>
    
    <!-- 今日课程 -->
    <div class="today-courses">
      <el-card shadow="hover">
        <template #header>
          <span>今日课程</span>
        </template>
        <el-table :data="todayCourses" style="width: 100%">
          <el-table-column prop="time" label="时间" width="120" />
          <el-table-column prop="courseName" label="课程名称" />
          <el-table-column prop="classroom" label="教室" width="120" />
          <el-table-column prop="students" label="学生人数" width="100" />
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import {
  Reading,
  UserFilled,
  DataLine,
  Calendar
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const userInfo = computed(() => userStore.userInfo)

const stats = ref({
  myCourses: 8,
  myStudents: 156,
  pendingScores: 23
})

const todayCourses = ref([
  {
    time: '08:00-09:40',
    courseName: '高等数学',
    classroom: 'A101',
    students: 45
  },
  {
    time: '10:00-11:40',
    courseName: '线性代数',
    classroom: 'A102',
    students: 38
  },
  {
    time: '14:00-15:40',
    courseName: '概率论',
    classroom: 'B201',
    students: 42
  }
])

const goToPage = (path) => {
  router.push(path)
}
</script>

<style scoped>
.teacher-dashboard {
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.dashboard-header {
  margin-bottom: 20px;
}

.dashboard-header h2 {
  margin: 0 0 8px 0;
  color: #333;
}

.dashboard-header p {
  margin: 0;
  color: #666;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 8px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.course-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.student-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.score-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.quick-actions {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.action-buttons .el-button {
  flex: 1;
  min-width: 120px;
}

.today-courses {
  margin-bottom: 20px;
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .action-buttons .el-button {
    width: 100%;
  }
}
</style> 