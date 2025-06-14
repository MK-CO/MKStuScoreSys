<template>
  <div class="student-dashboard">
    <div class="dashboard-header">
      <h2>学生个人中心</h2>
      <p>欢迎回来，{{ userInfo?.realName || userInfo?.username }}同学</p>
    </div>
    
    <!-- 统计卡片 -->
    <div class="stats-grid">
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon course-icon">
            <el-icon size="24"><Reading /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.totalCourses }}</div>
            <div class="stat-label">已选课程</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon credit-icon">
            <el-icon size="24"><Medal /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.totalCredits }}</div>
            <div class="stat-label">已获学分</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon gpa-icon">
            <el-icon size="24"><TrendCharts /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.gpa }}</div>
            <div class="stat-label">平均绩点</div>
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
          <el-button type="primary" @click="goToPage('/student/schedule')">
            <el-icon><Calendar /></el-icon>
            我的课程表
          </el-button>
          <el-button type="success" @click="goToPage('/student/scores')">
            <el-icon><DataLine /></el-icon>
            成绩查询
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
          <el-table-column prop="teacher" label="任课教师" width="120" />
          <el-table-column prop="classroom" label="教室" width="120" />
          <el-table-column label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === '进行中' ? 'success' : 'info'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    
    <!-- 最近成绩 -->
    <div class="recent-scores">
      <el-card shadow="hover">
        <template #header>
          <span>最近成绩</span>
        </template>
        <el-table :data="recentScores" style="width: 100%">
          <el-table-column prop="courseName" label="课程名称" />
          <el-table-column prop="score" label="成绩" width="80">
            <template #default="scope">
              <span :class="getScoreClass(scope.row.score)">{{ scope.row.score }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="credit" label="学分" width="80" />
          <el-table-column prop="date" label="录入时间" width="120" />
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
  Medal,
  TrendCharts,
  Calendar,
  DataLine
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const userInfo = computed(() => userStore.userInfo)

const stats = ref({
  totalCourses: 12,
  totalCredits: 45,
  gpa: 3.8
})

const todayCourses = ref([
  {
    time: '08:00-09:40',
    courseName: '高等数学',
    teacher: '张教授',
    classroom: 'A101',
    status: '未开始'
  },
  {
    time: '10:00-11:40',
    courseName: '大学英语',
    teacher: '李老师',
    classroom: 'B203',
    status: '进行中'
  },
  {
    time: '14:00-15:40',
    courseName: '计算机基础',
    teacher: '王老师',
    classroom: 'C301',
    status: '未开始'
  }
])

const recentScores = ref([
  {
    courseName: '高等数学',
    score: 92,
    credit: 4,
    date: '2024-01-10'
  },
  {
    courseName: '大学英语',
    score: 88,
    credit: 3,
    date: '2024-01-08'
  },
  {
    courseName: '计算机基础',
    score: 95,
    credit: 3,
    date: '2024-01-05'
  }
])

const goToPage = (path) => {
  router.push(path)
}

const getScoreClass = (score) => {
  if (score >= 90) return 'score-excellent'
  if (score >= 80) return 'score-good'
  if (score >= 70) return 'score-fair'
  if (score >= 60) return 'score-pass'
  return 'score-fail'
}
</script>

<style scoped>
.student-dashboard {
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

.credit-icon {
  background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
}

.gpa-icon {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
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

.today-courses,
.recent-scores {
  margin-bottom: 20px;
}

/* 成绩颜色 */
.score-excellent {
  color: #67c23a;
  font-weight: bold;
}

.score-good {
  color: #409eff;
  font-weight: bold;
}

.score-fair {
  color: #e6a23c;
  font-weight: bold;
}

.score-pass {
  color: #909399;
}

.score-fail {
  color: #f56c6c;
  font-weight: bold;
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