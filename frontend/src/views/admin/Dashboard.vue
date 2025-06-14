<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>总用户数</span>
            </div>
          </template>
          <div class="text item">
            <el-statistic :value="statistics.totalUsers" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>总课程数</span>
            </div>
          </template>
          <div class="text item">
            <el-statistic :value="statistics.totalCourses" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>总成绩记录</span>
            </div>
          </template>
          <div class="text item">
            <el-statistic :value="statistics.totalScores" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>活跃用户</span>
            </div>
          </template>
          <div class="text item">
            <el-statistic :value="statistics.activeUsers" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>用户类型分布</span>
            </div>
          </template>
          <div ref="userChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>成绩分布</span>
            </div>
          </template>
          <div ref="scoreChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const statistics = ref({
  totalUsers: 0,
  totalCourses: 0,
  totalScores: 0,
  activeUsers: 0
})

const userChart = ref()
const scoreChart = ref()

onMounted(() => {
  loadStatistics()
  initCharts()
})

const loadStatistics = async () => {
  // 这里应该调用API获取统计数据
  statistics.value = {
    totalUsers: 150,
    totalCourses: 25,
    totalScores: 1200,
    activeUsers: 89
  }
}

const initCharts = () => {
  // 用户类型分布图
  const userChartInstance = echarts.init(userChart.value)
  userChartInstance.setOption({
    title: {
      text: '用户类型分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '用户类型',
        type: 'pie',
        radius: '50%',
        data: [
          { value: 10, name: '管理员' },
          { value: 40, name: '教师' },
          { value: 100, name: '学生' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })

  // 成绩分布图
  const scoreChartInstance = echarts.init(scoreChart.value)
  scoreChartInstance.setOption({
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
    series: [
      {
        data: [50, 120, 300, 450, 280],
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
.dashboard {
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