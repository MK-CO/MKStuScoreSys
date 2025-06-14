<template>
  <div class="admin-dashboard">
    <!-- 统计卡片区域 -->
    <div class="stats-grid">
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon user-icon">
            <el-icon size="32"><UserFilled /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.totalUsers }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon course-icon">
            <el-icon size="32"><Reading /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.totalCourses }}</div>
            <div class="stat-label">课程总数</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon score-icon">
            <el-icon size="32"><DataLine /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.totalScores }}</div>
            <div class="stat-label">成绩记录</div>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card" shadow="hover">
        <div class="stat-content">
          <div class="stat-icon avg-icon">
            <el-icon size="32"><TrendCharts /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.avgScore }}</div>
            <div class="stat-label">平均分</div>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 图表区域 -->
    <div class="charts-grid">
      <el-card class="chart-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>成绩分布统计</span>
            <el-button type="primary" size="small" @click="refreshCharts">刷新</el-button>
          </div>
        </template>
        <div class="chart-container" ref="scoreChartRef"></div>
      </el-card>
      
      <el-card class="chart-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span>课程类型分布</span>
          </div>
        </template>
        <div class="chart-container" ref="courseChartRef"></div>
      </el-card>
    </div>
    
    <!-- 快速操作区域 -->
    <div class="quick-actions">
      <el-card shadow="hover">
        <template #header>
          <span>快速操作</span>
        </template>
        <div class="action-buttons">
          <el-button type="primary" @click="goToPage('/admin/course-info')">
            <el-icon><Reading /></el-icon>
            课程管理
          </el-button>
          <el-button type="success" @click="goToPage('/admin/users')">
            <el-icon><UserFilled /></el-icon>
            用户管理
          </el-button>
          <el-button type="warning" @click="goToPage('/admin/score-info')">
            <el-icon><DataLine /></el-icon>
            成绩管理
          </el-button>
          <el-button type="info" @click="goToPage('/admin/score-statistics')">
            <el-icon><TrendCharts /></el-icon>
            统计分析
          </el-button>
        </div>
      </el-card>
    </div>
    
    <!-- 最近活动 -->
    <div class="recent-activities">
      <el-card shadow="hover">
        <template #header>
          <span>最近活动</span>
        </template>
        <el-timeline>
          <el-timeline-item
            v-for="activity in recentActivities"
            :key="activity.id"
            :timestamp="activity.time"
            :type="activity.type"
          >
            {{ activity.content }}
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  UserFilled,
  Reading,
  DataLine,
  TrendCharts
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const router = useRouter()

// 响应式数据
const stats = ref({
  totalUsers: 1248,
  totalCourses: 156,
  totalScores: 8924,
  avgScore: 82.5
})

const recentActivities = ref([
  {
    id: 1,
    content: '新增课程《高等数学》',
    time: '2024-01-15 10:30',
    type: 'primary'
  },
  {
    id: 2,
    content: '用户张三提交了成绩申诉',
    time: '2024-01-15 09:15',
    type: 'warning'
  },
  {
    id: 3,
    content: '系统完成了数据备份',
    time: '2024-01-15 08:00',
    type: 'success'
  },
  {
    id: 4,
    content: '教师李四更新了课程信息',
    time: '2024-01-14 16:45',
    type: 'info'
  }
])

// 图表引用
const scoreChartRef = ref(null)
const courseChartRef = ref(null)

// 方法
const goToPage = (path) => {
  router.push(path)
}

const refreshCharts = () => {
  initCharts()
  ElMessage.success('图表已刷新')
}

const initCharts = () => {
  nextTick(() => {
    // 成绩分布图表
    if (scoreChartRef.value) {
      const scoreChart = echarts.init(scoreChartRef.value)
      const scoreOption = {
        title: {
          text: '成绩分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['0-60', '60-70', '70-80', '80-90', '90-100']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [120, 200, 150, 80, 70],
          type: 'bar',
          itemStyle: {
            color: '#409eff'
          }
        }]
      }
      scoreChart.setOption(scoreOption)
    }
    
    // 课程类型分布图表
    if (courseChartRef.value) {
      const courseChart = echarts.init(courseChartRef.value)
      const courseOption = {
        title: {
          text: '课程类型分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        series: [{
          type: 'pie',
          radius: '50%',
          data: [
            { value: 60, name: '必修课' },
            { value: 40, name: '选修课' },
            { value: 30, name: '实践课' }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      courseChart.setOption(courseOption)
    }
  })
}

// 生命周期
onMounted(() => {
  initCharts()
})
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

/* 统计卡片网格 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
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
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.user-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.course-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.score-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.avg-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

/* 图表网格 */
.charts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.chart-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 300px;
  width: 100%;
}

/* 快速操作 */
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

/* 最近活动 */
.recent-activities {
  margin-bottom: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .charts-grid {
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