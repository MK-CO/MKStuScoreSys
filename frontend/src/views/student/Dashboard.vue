<template>
  <div class="student-dashboard">
    <h2>学生仪表板</h2>
    
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>已选课程</span>
            </div>
          </template>
          <div class="text item">
            <el-statistic :value="statistics.selectedCourses" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>平均成绩</span>
            </div>
          </template>
          <div class="text item">
            <el-statistic :value="statistics.averageScore" :precision="1" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>学分总计</span>
            </div>
          </template>
          <div class="text item">
            <el-statistic :value="statistics.totalCredits" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>我的课程</span>
            </div>
          </template>
          <el-table :data="myCourses" style="width: 100%">
            <el-table-column prop="courseName" label="课程名称" />
            <el-table-column prop="teacherName" label="授课教师" />
            <el-table-column prop="credits" label="学分" />
            <el-table-column prop="semester" label="学期" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>成绩概览</span>
            </div>
          </template>
          <div ref="scoreChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top: 20px;">
      <el-col :span="24">
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
import * as echarts from 'echarts'

const statistics = ref({
  selectedCourses: 0,
  averageScore: 0,
  totalCredits: 0
})

const myCourses = ref([])
const recentActivities = ref([])
const scoreChart = ref()

onMounted(() => {
  loadData()
  initChart()
})

const loadData = async () => {
  // 模拟数据
  statistics.value = {
    selectedCourses: 5,
    averageScore: 85.6,
    totalCredits: 18
  }

  myCourses.value = [
    {
      courseName: '高等数学',
      teacherName: '张教授',
      credits: 4,
      semester: '2024春季'
    },
    {
      courseName: '线性代数',
      teacherName: '李教授',
      credits: 3,
      semester: '2024春季'
    },
    {
      courseName: '概率论',
      teacherName: '王教授',
      credits: 3,
      semester: '2024春季'
    }
  ]

  recentActivities.value = [
    {
      id: 1,
      content: '查看了高等数学成绩',
      timestamp: '2024-01-15 10:30'
    },
    {
      id: 2,
      content: '查看了课程表',
      timestamp: '2024-01-14 14:20'
    },
    {
      id: 3,
      content: '登录了系统',
      timestamp: '2024-01-13 09:15'
    }
  ]
}

const initChart = () => {
  const chartInstance = echarts.init(scoreChart.value)
  chartInstance.setOption({
    title: {
      text: '各科成绩',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['高等数学', '线性代数', '概率论', '大学物理', '程序设计']
    },
    yAxis: {
      type: 'value',
      min: 0,
      max: 100
    },
    series: [
      {
        data: [85, 92, 78, 88, 90],
        type: 'bar',
        itemStyle: {
          color: '#409EFF'
        }
      }
    ]
  })
}
</script>

<style scoped>
.student-dashboard {
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