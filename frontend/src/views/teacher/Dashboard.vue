<template>
  <div class="teacher-dashboard">
    <h2>教师仪表板</h2>
    
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>我的课程</span>
            </div>
          </template>
          <div class="text item">
            <el-statistic :value="statistics.myCourses" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>学生总数</span>
            </div>
          </template>
          <div class="text item">
            <el-statistic :value="statistics.totalStudents" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>已录成绩</span>
            </div>
          </template>
          <div class="text item">
            <el-statistic :value="statistics.recordedScores" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>我的课程列表</span>
            </div>
          </template>
          <el-table :data="myCourses" style="width: 100%">
            <el-table-column prop="courseName" label="课程名称" />
            <el-table-column prop="semester" label="学期" />
            <el-table-column prop="studentCount" label="学生数" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最近活动</span>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="activity in recentActivities"
              :key="activity.id"
              :timestamp="activity.timestamp"
            >
              {{ activity.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const statistics = ref({
  myCourses: 0,
  totalStudents: 0,
  recordedScores: 0
})

const myCourses = ref([])
const recentActivities = ref([])

onMounted(() => {
  loadData()
})

const loadData = async () => {
  // 模拟数据
  statistics.value = {
    myCourses: 3,
    totalStudents: 120,
    recordedScores: 85
  }

  myCourses.value = [
    {
      courseName: '高等数学',
      semester: '2024春季',
      studentCount: 45
    },
    {
      courseName: '线性代数',
      semester: '2024春季',
      studentCount: 38
    },
    {
      courseName: '概率论',
      semester: '2024春季',
      studentCount: 37
    }
  ]

  recentActivities.value = [
    {
      id: 1,
      content: '录入了高等数学期末成绩',
      timestamp: '2024-01-15 10:30'
    },
    {
      id: 2,
      content: '更新了线性代数课程安排',
      timestamp: '2024-01-14 14:20'
    },
    {
      id: 3,
      content: '查看了学生成绩统计',
      timestamp: '2024-01-13 09:15'
    }
  ]
}
</script>

<style scoped>
.teacher-dashboard {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}
</style> 