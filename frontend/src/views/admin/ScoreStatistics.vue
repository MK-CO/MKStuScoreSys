<template>
  <div class="score-statistics">
    <div class="page-header">
      <h2>成绩统计分析</h2>
      <p>全面分析学生成绩数据，提供多维度统计视图</p>
    </div>
    
    <!-- 筛选条件 -->
    <el-card class="filter-card" shadow="never">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-select v-model="selectedSemester" placeholder="选择学期" clearable>
            <el-option
              v-for="semester in semesters"
              :key="semester.value"
              :label="semester.label"
              :value="semester.value"
            />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="selectedCourse" placeholder="选择课程" clearable>
            <el-option
              v-for="course in courses"
              :key="course.value"
              :label="course.label"
              :value="course.value"
            />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="selectedDepartment" placeholder="选择院系" clearable>
            <el-option
              v-for="dept in departments"
              :key="dept.value"
              :label="dept.label"
              :value="dept.value"
            />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="loadStatistics">
            <el-icon><Search /></el-icon>
            查询分析
          </el-button>
          <el-button @click="exportReport">
            <el-icon><Download /></el-icon>
            导出报告
          </el-button>
        </el-col>
      </el-row>
    </el-card>
    
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="statistics-overview">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-value">{{ overview.totalStudents }}</div>
            <div class="stat-label">总学生数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-value">{{ overview.averageScore }}</div>
            <div class="stat-label">平均分</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-value">{{ overview.passRate }}%</div>
            <div class="stat-label">及格率</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-value">{{ overview.excellentRate }}%</div>
            <div class="stat-label">优秀率</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-section">
      <!-- 成绩分布图 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>成绩分布统计</span>
            </div>
          </template>
          <div ref="scoreDistributionChart" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
      
      <!-- 各科目平均分对比 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>各科目平均分对比</span>
            </div>
          </template>
          <div ref="subjectComparisonChart" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="charts-section">
      <!-- 成绩趋势分析 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>成绩趋势分析</span>
            </div>
          </template>
          <div ref="trendChart" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
      
      <!-- 等级分布饼图 -->
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>等级分布</span>
            </div>
          </template>
          <div ref="gradeDistributionChart" style="width: 100%; height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 详细统计表格 -->
    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>详细统计数据</span>
        </div>
      </template>
      
      <el-table :data="statisticsData" stripe>
        <el-table-column prop="course" label="课程" width="150" />
        <el-table-column prop="totalStudents" label="总人数" width="100" />
        <el-table-column prop="averageScore" label="平均分" width="100">
          <template #default="{ row }">
            <span :class="getScoreClass(row.averageScore)">
              {{ row.averageScore }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="maxScore" label="最高分" width="100" />
        <el-table-column prop="minScore" label="最低分" width="100" />
        <el-table-column prop="passRate" label="及格率" width="100">
          <template #default="{ row }">
            <el-progress 
              :percentage="row.passRate" 
              :color="getProgressColor(row.passRate)"
              :stroke-width="8"
            />
          </template>
        </el-table-column>
        <el-table-column prop="excellentRate" label="优秀率" width="100">
          <template #default="{ row }">
            <el-progress 
              :percentage="row.excellentRate" 
              :color="getProgressColor(row.excellentRate)"
              :stroke-width="8"
            />
          </template>
        </el-table-column>
        <el-table-column prop="standardDeviation" label="标准差" width="100" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="viewDetail(row)">详细分析</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详细分析弹窗 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="详细分析"
      width="60%"
    >
      <div v-if="selectedDetailData">
        <h3>{{ selectedDetailData.course }} - 详细统计</h3>
        
        <el-row :gutter="20" class="detail-stats">
          <el-col :span="8">
            <div class="detail-item">
              <div class="detail-label">总人数</div>
              <div class="detail-value">{{ selectedDetailData.totalStudents }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <div class="detail-label">平均分</div>
              <div class="detail-value">{{ selectedDetailData.averageScore }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <div class="detail-label">中位数</div>
              <div class="detail-value">{{ selectedDetailData.median }}</div>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="grade-breakdown">
          <el-col :span="24">
            <h4>成绩分段统计</h4>
            <el-table :data="selectedDetailData.gradeBreakdown" size="small">
              <el-table-column prop="range" label="分数段" />
              <el-table-column prop="count" label="人数" />
              <el-table-column prop="percentage" label="占比" />
            </el-table>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Download } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const selectedSemester = ref('')
const selectedCourse = ref('')
const selectedDepartment = ref('')
const detailDialogVisible = ref(false)
const selectedDetailData = ref(null)

// 图表实例
const scoreDistributionChart = ref(null)
const subjectComparisonChart = ref(null)
const trendChart = ref(null)
const gradeDistributionChart = ref(null)

// 筛选选项
const semesters = ref([
  { label: '2023-2024学年第一学期', value: '2023-1' },
  { label: '2023-2024学年第二学期', value: '2023-2' },
  { label: '2024-2025学年第一学期', value: '2024-1' }
])

const courses = ref([
  { label: '高等数学', value: 'math' },
  { label: '大学英语', value: 'english' },
  { label: '计算机基础', value: 'computer' },
  { label: '大学物理', value: 'physics' }
])

const departments = ref([
  { label: '计算机学院', value: 'cs' },
  { label: '数学学院', value: 'math' },
  { label: '物理学院', value: 'physics' },
  { label: '外语学院', value: 'foreign' }
])

// 统计概览数据
const overview = reactive({
  totalStudents: 1248,
  averageScore: 78.5,
  passRate: 85.2,
  excellentRate: 23.8
})

// 详细统计数据
const statisticsData = ref([
  {
    course: '高等数学',
    totalStudents: 312,
    averageScore: 75.2,
    maxScore: 98,
    minScore: 45,
    passRate: 82,
    excellentRate: 18,
    standardDeviation: 12.8,
    median: 76,
    gradeBreakdown: [
      { range: '90-100', count: 28, percentage: '9.0%' },
      { range: '80-89', count: 78, percentage: '25.0%' },
      { range: '70-79', count: 98, percentage: '31.4%' },
      { range: '60-69', count: 76, percentage: '24.4%' },
      { range: '0-59', count: 32, percentage: '10.3%' }
    ]
  },
  {
    course: '大学英语',
    totalStudents: 298,
    averageScore: 82.1,
    maxScore: 95,
    minScore: 52,
    passRate: 88,
    excellentRate: 32,
    standardDeviation: 10.5,
    median: 83,
    gradeBreakdown: [
      { range: '90-100', count: 45, percentage: '15.1%' },
      { range: '80-89', count: 89, percentage: '29.9%' },
      { range: '70-79', count: 92, percentage: '30.9%' },
      { range: '60-69', count: 56, percentage: '18.8%' },
      { range: '0-59', count: 16, percentage: '5.4%' }
    ]
  },
  {
    course: '计算机基础',
    totalStudents: 285,
    averageScore: 79.8,
    maxScore: 100,
    minScore: 38,
    passRate: 86,
    excellentRate: 28,
    standardDeviation: 14.2,
    median: 81,
    gradeBreakdown: [
      { range: '90-100', count: 42, percentage: '14.7%' },
      { range: '80-89', count: 82, percentage: '28.8%' },
      { range: '70-79', count: 88, percentage: '30.9%' },
      { range: '60-69', count: 53, percentage: '18.6%' },
      { range: '0-59', count: 20, percentage: '7.0%' }
    ]
  },
  {
    course: '大学物理',
    totalStudents: 353,
    averageScore: 76.4,
    maxScore: 96,
    minScore: 41,
    passRate: 81,
    excellentRate: 21,
    standardDeviation: 13.6,
    median: 77,
    gradeBreakdown: [
      { range: '90-100', count: 35, percentage: '9.9%' },
      { range: '80-89', count: 89, percentage: '25.2%' },
      { range: '70-79', count: 118, percentage: '33.4%' },
      { range: '60-69', count: 78, percentage: '22.1%' },
      { range: '0-59', count: 33, percentage: '9.3%' }
    ]
  }
])

// 初始化图表
const initCharts = () => {
  nextTick(() => {
    // 成绩分布图
    if (scoreDistributionChart.value) {
      const chart1 = echarts.init(scoreDistributionChart.value)
      chart1.setOption({
        title: { text: '成绩分布' },
        tooltip: {},
        xAxis: {
          data: ['0-59', '60-69', '70-79', '80-89', '90-100']
        },
        yAxis: {},
        series: [{
          name: '人数',
          type: 'bar',
          data: [101, 263, 396, 338, 150],
          itemStyle: {
            color: '#409EFF'
          }
        }]
      })
    }

    // 各科目平均分对比
    if (subjectComparisonChart.value) {
      const chart2 = echarts.init(subjectComparisonChart.value)
      chart2.setOption({
        title: { text: '各科目平均分' },
        tooltip: {},
        xAxis: {
          data: ['高等数学', '大学英语', '计算机基础', '大学物理']
        },
        yAxis: {},
        series: [{
          name: '平均分',
          type: 'bar',
          data: [75.2, 82.1, 79.8, 76.4],
          itemStyle: {
            color: '#67C23A'
          }
        }]
      })
    }

    // 成绩趋势分析
    if (trendChart.value) {
      const chart3 = echarts.init(trendChart.value)
      chart3.setOption({
        title: { text: '成绩趋势' },
        tooltip: {},
        xAxis: {
          data: ['第1周', '第4周', '第8周', '第12周', '第16周', '期末']
        },
        yAxis: {},
        series: [{
          name: '平均分',
          type: 'line',
          data: [72, 75, 78, 79, 81, 78.5],
          smooth: true,
          itemStyle: {
            color: '#E6A23C'
          }
        }]
      })
    }

    // 等级分布饼图
    if (gradeDistributionChart.value) {
      const chart4 = echarts.init(gradeDistributionChart.value)
      chart4.setOption({
        title: { text: '等级分布' },
        tooltip: {},
        series: [{
          name: '等级分布',
          type: 'pie',
          radius: '50%',
          data: [
            { value: 150, name: '优秀(90-100)' },
            { value: 338, name: '良好(80-89)' },
            { value: 396, name: '中等(70-79)' },
            { value: 263, name: '及格(60-69)' },
            { value: 101, name: '不及格(0-59)' }
          ]
        }]
      })
    }
  })
}

// 加载统计数据
const loadStatistics = () => {
  ElMessage.success('统计数据已更新')
  // 这里可以根据筛选条件重新加载数据
  initCharts()
}

// 导出报告
const exportReport = () => {
  ElMessage.success('报告导出成功')
}

// 查看详细分析
const viewDetail = (row) => {
  selectedDetailData.value = row
  detailDialogVisible.value = true
}

// 获取分数样式类
const getScoreClass = (score) => {
  if (score >= 90) return 'score-excellent'
  if (score >= 80) return 'score-good'
  if (score >= 70) return 'score-medium'
  if (score >= 60) return 'score-pass'
  return 'score-fail'
}

// 获取进度条颜色
const getProgressColor = (percentage) => {
  if (percentage >= 90) return '#67C23A'
  if (percentage >= 80) return '#E6A23C'
  if (percentage >= 70) return '#409EFF'
  return '#F56C6C'
}

onMounted(() => {
  initCharts()
})
</script>

<style scoped>
.score-statistics {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.filter-card {
  margin-bottom: 20px;
}

.statistics-overview {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
}

.stat-item {
  padding: 20px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.charts-section {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.score-excellent {
  color: #67C23A;
  font-weight: bold;
}

.score-good {
  color: #409EFF;
  font-weight: bold;
}

.score-medium {
  color: #E6A23C;
  font-weight: bold;
}

.score-pass {
  color: #909399;
}

.score-fail {
  color: #F56C6C;
  font-weight: bold;
}

.detail-stats {
  margin-bottom: 20px;
}

.detail-item {
  text-align: center;
  padding: 15px;
  border: 1px solid #EBEEF5;
  border-radius: 4px;
}

.detail-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.detail-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.grade-breakdown {
  margin-top: 20px;
}

.grade-breakdown h4 {
  margin-bottom: 10px;
  color: #303133;
}
</style> 