<template>
  <div class="student-score-query">
    <h2>成绩查询</h2>
    
    <div class="filter-bar">
      <el-form :inline="true" :model="filterForm">
        <el-form-item label="学期">
          <el-select v-model="filterForm.semester" placeholder="请选择学期" clearable>
            <el-option label="2024春季" value="2024春季" />
            <el-option label="2023秋季" value="2023秋季" />
            <el-option label="2023春季" value="2023春季" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程类型">
          <el-select v-model="filterForm.courseType" placeholder="请选择课程类型" clearable>
            <el-option label="必修课" value="必修" />
            <el-option label="选修课" value="选修" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadScores">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="scores" style="width: 100%" v-loading="loading">
      <el-table-column prop="courseName" label="课程名称" />
      <el-table-column prop="courseCode" label="课程代码" />
      <el-table-column prop="courseType" label="课程类型" />
      <el-table-column prop="credits" label="学分" />
      <el-table-column prop="teacherName" label="授课教师" />
      <el-table-column prop="semester" label="学期" />
      <el-table-column prop="score" label="成绩" width="100">
        <template #default="scope">
          <el-tag :type="getScoreColor(scope.row.score)">
            {{ scope.row.score }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="grade" label="等级" width="80" />
      <el-table-column prop="gpa" label="绩点" width="80" />
    </el-table>

    <div class="summary" style="margin-top: 20px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card>
            <el-statistic title="总学分" :value="summary.totalCredits" />
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <el-statistic title="平均分" :value="summary.averageScore" :precision="1" />
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <el-statistic title="平均绩点" :value="summary.averageGPA" :precision="2" />
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <el-statistic title="已修课程" :value="summary.totalCourses" />
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="chart-section" style="margin-top: 20px;">
      <el-card>
        <template #header>
          <span>成绩趋势</span>
        </template>
        <div ref="trendChart" style="height: 300px;"></div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import * as echarts from 'echarts'

const loading = ref(false)
const scores = ref([])
const trendChart = ref()

const filterForm = ref({
  semester: '',
  courseType: ''
})

const summary = computed(() => {
  if (scores.value.length === 0) {
    return { totalCredits: 0, averageScore: 0, averageGPA: 0, totalCourses: 0 }
  }
  
  const totalCredits = scores.value.reduce((sum, score) => sum + score.credits, 0)
  const averageScore = scores.value.reduce((sum, score) => sum + score.score, 0) / scores.value.length
  const averageGPA = scores.value.reduce((sum, score) => sum + score.gpa, 0) / scores.value.length
  const totalCourses = scores.value.length
  
  return { totalCredits, averageScore, averageGPA, totalCourses }
})

onMounted(() => {
  loadScores()
  initChart()
})

const loadScores = async () => {
  loading.value = true
  try {
    // 模拟数据
    scores.value = [
      {
        courseName: '高等数学',
        courseCode: 'MATH001',
        courseType: '必修',
        credits: 4,
        teacherName: '张教授',
        semester: '2024春季',
        score: 85.5,
        grade: 'B',
        gpa: 3.5
      },
      {
        courseName: '线性代数',
        courseCode: 'MATH002',
        courseType: '必修',
        credits: 3,
        teacherName: '李教授',
        semester: '2024春季',
        score: 92.0,
        grade: 'A',
        gpa: 4.0
      },
      {
        courseName: '概率论',
        courseCode: 'MATH003',
        courseType: '必修',
        credits: 3,
        teacherName: '王教授',
        semester: '2024春季',
        score: 78.5,
        grade: 'C',
        gpa: 2.5
      },
      {
        courseName: '大学物理',
        courseCode: 'PHYS001',
        courseType: '必修',
        credits: 4,
        teacherName: '赵教授',
        semester: '2023秋季',
        score: 88.0,
        grade: 'B',
        gpa: 3.8
      },
      {
        courseName: '程序设计',
        courseCode: 'CS001',
        courseType: '必修',
        credits: 3,
        teacherName: '陈教授',
        semester: '2023秋季',
        score: 90.0,
        grade: 'A',
        gpa: 4.0
      }
    ]
  } catch (error) {
    console.error('加载成绩数据失败', error)
  } finally {
    loading.value = false
  }
}

const getScoreColor = (score) => {
  if (score >= 90) return 'success'
  if (score >= 80) return 'warning'
  if (score >= 60) return 'info'
  return 'danger'
}

const resetFilter = () => {
  filterForm.value = {
    semester: '',
    courseType: ''
  }
  loadScores()
}

const initChart = () => {
  const chartInstance = echarts.init(trendChart.value)
  chartInstance.setOption({
    title: {
      text: '各学期平均成绩趋势',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['2023春季', '2023秋季', '2024春季']
    },
    yAxis: {
      type: 'value',
      min: 0,
      max: 100
    },
    series: [
      {
        name: '平均成绩',
        data: [82, 89, 85],
        type: 'line',
        smooth: true,
        itemStyle: {
          color: '#409EFF'
        }
      }
    ]
  })
}
</script>

<style scoped>
.student-score-query {
  padding: 20px;
}

.filter-bar {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.summary {
  margin-bottom: 20px;
}

.chart-section {
  margin-top: 20px;
}
</style> 