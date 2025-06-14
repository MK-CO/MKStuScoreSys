<template>
  <div class="score-management">
    <div class="header">
      <h2>成绩管理</h2>
      <el-button type="primary" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        录入成绩
      </el-button>
    </div>

    <div class="filter-bar">
      <el-form :inline="true" :model="filterForm">
        <el-form-item label="课程">
          <el-select v-model="filterForm.courseId" placeholder="请选择课程" clearable>
            <el-option
              v-for="course in courses"
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
      <el-table-column prop="scoreId" label="成绩ID" width="100" />
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
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-dropdown @command="(command) => handleCommand(command, scope.row)">
            <el-button size="small" type="primary">
              操作
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="edit">
                  <el-icon><Edit /></el-icon>
                  编辑成绩
                </el-dropdown-item>
                <el-dropdown-item command="delete" divided>
                  <el-icon><Delete /></el-icon>
                  删除成绩
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑成绩对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="editingScore ? '编辑成绩' : '录入成绩'"
      width="500px"
    >
      <el-form :model="scoreForm" :rules="rules" ref="scoreFormRef" label-width="100px">
        <el-form-item label="学生" prop="studentId">
          <el-select v-model="scoreForm.studentId" placeholder="请选择学生" filterable>
            <el-option
              v-for="student in students"
              :key="student.userId"
              :label="`${student.realName} (${student.userName})`"
              :value="student.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="课程" prop="courseId">
          <el-select v-model="scoreForm.courseId" placeholder="请选择课程">
            <el-option
              v-for="course in courses"
              :key="course.courseId"
              :label="course.courseName"
              :value="course.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学期" prop="semester">
          <el-input v-model="scoreForm.semester" placeholder="如：2024春季" />
        </el-form-item>
        <el-form-item label="成绩" prop="score">
          <el-input-number 
            v-model="scoreForm.score" 
            :min="0" 
            :max="100" 
            :precision="1"
            @change="updateGrade"
          />
        </el-form-item>
        <el-form-item label="等级">
          <el-input v-model="scoreForm.grade" readonly />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddDialog = false">取消</el-button>
          <el-button type="primary" @click="saveScore">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, ArrowDown, Edit, Delete } from '@element-plus/icons-vue'

const loading = ref(false)
const scores = ref([])
const courses = ref([])
const students = ref([])
const showAddDialog = ref(false)
const editingScore = ref(null)
const scoreFormRef = ref()

const filterForm = ref({
  courseId: '',
  semester: ''
})

const scoreForm = ref({
  studentId: '',
  courseId: '',
  semester: '',
  score: 0,
  grade: ''
})

const rules = {
  studentId: [
    { required: true, message: '请选择学生', trigger: 'change' }
  ],
  courseId: [
    { required: true, message: '请选择课程', trigger: 'change' }
  ],
  semester: [
    { required: true, message: '请输入学期', trigger: 'blur' }
  ],
  score: [
    { required: true, message: '请输入成绩', trigger: 'blur' }
  ]
}

onMounted(() => {
  loadScores()
  loadCourses()
  loadStudents()
})

const loadScores = async () => {
  loading.value = true
  try {
    // 模拟数据
    scores.value = [
      {
        scoreId: 1,
        studentId: 3,
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
        studentId: 3,
        studentName: '李同学',
        courseId: 2,
        courseName: '线性代数',
        semester: '2024春季',
        score: 92.0,
        grade: 'A',
        createTime: '2024-01-16 10:00:00'
      }
    ]
  } catch (error) {
    ElMessage.error('加载成绩数据失败')
  } finally {
    loading.value = false
  }
}

const loadCourses = async () => {
  try {
    // 模拟数据
    courses.value = [
      { courseId: 1, courseName: '高等数学' },
      { courseId: 2, courseName: '线性代数' },
      { courseId: 3, courseName: '概率论' }
    ]
  } catch (error) {
    ElMessage.error('加载课程数据失败')
  }
}

const loadStudents = async () => {
  try {
    // 模拟数据
    students.value = [
      { userId: 3, userName: '20240001', realName: '李同学' },
      { userId: 4, userName: '20240002', realName: '王同学' },
      { userId: 5, userName: '20240003', realName: '张同学' }
    ]
  } catch (error) {
    ElMessage.error('加载学生数据失败')
  }
}

const getScoreColor = (score) => {
  if (score >= 90) return 'success'
  if (score >= 80) return 'warning'
  if (score >= 60) return 'info'
  return 'danger'
}

const getGrade = (score) => {
  if (score >= 90) return 'A'
  if (score >= 80) return 'B'
  if (score >= 70) return 'C'
  if (score >= 60) return 'D'
  return 'F'
}

const updateGrade = () => {
  scoreForm.value.grade = getGrade(scoreForm.value.score)
}

const editScore = (score) => {
  editingScore.value = score
  scoreForm.value = { ...score }
  showAddDialog.value = true
}

const saveScore = async () => {
  if (!scoreFormRef.value) return
  
  await scoreFormRef.value.validate((valid) => {
    if (valid) {
      try {
        const student = students.value.find(s => s.userId === scoreForm.value.studentId)
        const course = courses.value.find(c => c.courseId === scoreForm.value.courseId)
        
        if (editingScore.value) {
          // 更新成绩
          const index = scores.value.findIndex(s => s.scoreId === editingScore.value.scoreId)
          if (index !== -1) {
            scores.value[index] = {
              ...scoreForm.value,
              scoreId: editingScore.value.scoreId,
              studentName: student?.realName,
              courseName: course?.courseName
            }
          }
          ElMessage.success('成绩更新成功')
        } else {
          // 添加成绩
          const newScore = {
            ...scoreForm.value,
            scoreId: Date.now(),
            studentName: student?.realName,
            courseName: course?.courseName,
            createTime: new Date().toLocaleString()
          }
          scores.value.push(newScore)
          ElMessage.success('成绩录入成功')
        }
        
        showAddDialog.value = false
        resetForm()
      } catch (error) {
        ElMessage.error('保存失败')
      }
    }
  })
}

const deleteScore = async (score) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除学生"${score.studentName}"的"${score.courseName}"成绩吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const index = scores.value.findIndex(s => s.scoreId === score.scoreId)
    if (index !== -1) {
      scores.value.splice(index, 1)
      ElMessage.success('成绩删除成功')
    }
  } catch {
    // 用户取消操作
  }
}

const resetForm = () => {
  scoreForm.value = {
    studentId: '',
    courseId: '',
    semester: '',
    score: 0,
    grade: ''
  }
  editingScore.value = null
}

const resetFilter = () => {
  filterForm.value = {
    courseId: '',
    semester: ''
  }
  loadScores()
}

const handleCommand = (command, score) => {
  if (command === 'edit') {
    editScore(score)
  } else if (command === 'delete') {
    deleteScore(score)
  }
}
</script>

<style scoped>
.score-management {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h2 {
  margin: 0;
}

.filter-bar {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 4px;
}
</style> 