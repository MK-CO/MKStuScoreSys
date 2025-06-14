<template>
  <div class="teacher-score-query">
    <h2>成绩查询</h2>
    
    <div class="filter-bar">
      <el-form :inline="true" :model="filterForm">
        <el-form-item label="课程">
          <el-select v-model="filterForm.courseId" placeholder="请选择课程" clearable>
            <el-option
              v-for="course in myCourses"
              :key="course.courseId"
              :label="course.courseName"
              :value="course.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学期">
          <el-input v-model="filterForm.semester" placeholder="请输入学期" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadScores">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="scores" style="width: 100%" v-loading="loading">
      <el-table-column prop="studentName" label="学生姓名" />
      <el-table-column prop="studentId" label="学号" />
      <el-table-column prop="courseName" label="课程名称" />
      <el-table-column prop="semester" label="学期" />
      <el-table-column prop="score" label="成绩" width="100">
        <template #default="scope">
          <el-tag :type="getScoreColor(scope.row.score)">
            {{ scope.row.score }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="grade" label="等级" width="80" />
      <el-table-column prop="createTime" label="录入时间" />
    </el-table>

    <div class="statistics" style="margin-top: 20px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-statistic title="平均分" :value="statistics.average" :precision="1" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="最高分" :value="statistics.max" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="最低分" :value="statistics.min" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="及格率" :value="statistics.passRate" suffix="%" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'

const loading = ref(false)
const scores = ref([])
const myCourses = ref([])

const filterForm = ref({
  courseId: '',
  semester: ''
})

const statistics = computed(() => {
  if (scores.value.length === 0) {
    return { average: 0, max: 0, min: 0, passRate: 0 }
  }
  
  const scoreValues = scores.value.map(s => s.score)
  const average = scoreValues.reduce((a, b) => a + b, 0) / scoreValues.length
  const max = Math.max(...scoreValues)
  const min = Math.min(...scoreValues)
  const passCount = scoreValues.filter(s => s >= 60).length
  const passRate = (passCount / scoreValues.length * 100).toFixed(1)
  
  return { average, max, min, passRate }
})

onMounted(() => {
  loadMyCourses()
  loadScores()
})

const loadMyCourses = async () => {
  // 模拟数据
  myCourses.value = [
    { courseId: 1, courseName: '高等数学' },
    { courseId: 2, courseName: '线性代数' },
    { courseId: 3, courseName: '概率论' }
  ]
}

const loadScores = async () => {
  loading.value = true
  try {
    // 模拟数据
    scores.value = [
      {
        scoreId: 1,
        studentId: '20240001',
        studentName: '李同学',
        courseId: 1,
        courseName: '高等数学',
        semester: '2024春季',
        score: 85.5,
        grade: 'B',
        createTime: '2024-01-15 10:00:00'
      },
      {
        scoreId: 2,
        studentId: '20240002',
        studentName: '王同学',
        courseId: 1,
        courseName: '高等数学',
        semester: '2024春季',
        score: 92.0,
        grade: 'A',
        createTime: '2024-01-16 10:00:00'
      },
      {
        scoreId: 3,
        studentId: '20240003',
        studentName: '张同学',
        courseId: 1,
        courseName: '高等数学',
        semester: '2024春季',
        score: 78.5,
        grade: 'C',
        createTime: '2024-01-17 10:00:00'
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
    courseId: '',
    semester: ''
  }
  loadScores()
}
</script>

<style scoped>
.teacher-score-query {
  padding: 20px;
}

.filter-bar {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.statistics {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 4px;
}
</style> 