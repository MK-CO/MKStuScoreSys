<template>
  <div class="course-management">
    <div class="header">
      <h2>课程管理</h2>
      <el-button type="primary" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        添加课程
      </el-button>
    </div>

    <el-table :data="courses" style="width: 100%" v-loading="loading">
      <el-table-column prop="courseId" label="课程ID" width="100" />
      <el-table-column prop="courseName" label="课程名称" />
      <el-table-column prop="courseCode" label="课程代码" />
      <el-table-column prop="credits" label="学分" width="80" />
      <el-table-column prop="teacherName" label="授课教师" />
      <el-table-column prop="semester" label="学期" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="editCourse(scope.row)">编辑</el-button>
          <el-button 
            size="small" 
            :type="scope.row.status === 1 ? 'danger' : 'success'"
            @click="toggleStatus(scope.row)"
          >
            {{ scope.row.status === 1 ? '禁用' : '启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      style="margin-top: 20px; justify-content: center;"
    />

    <!-- 添加/编辑课程对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="editingCourse ? '编辑课程' : '添加课程'"
      width="500px"
    >
      <el-form :model="courseForm" :rules="rules" ref="courseFormRef" label-width="100px">
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="courseForm.courseName" />
        </el-form-item>
        <el-form-item label="课程代码" prop="courseCode">
          <el-input v-model="courseForm.courseCode" />
        </el-form-item>
        <el-form-item label="学分" prop="credits">
          <el-input-number v-model="courseForm.credits" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="授课教师" prop="teacherId">
          <el-select v-model="courseForm.teacherId" placeholder="请选择教师">
            <el-option
              v-for="teacher in teachers"
              :key="teacher.userId"
              :label="teacher.userName"
              :value="teacher.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学期" prop="semester">
          <el-input v-model="courseForm.semester" placeholder="如：2024春季" />
        </el-form-item>
        <el-form-item label="课程描述" prop="description">
          <el-input
            v-model="courseForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入课程描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddDialog = false">取消</el-button>
          <el-button type="primary" @click="saveCourse">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const loading = ref(false)
const courses = ref([])
const teachers = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showAddDialog = ref(false)
const editingCourse = ref(null)
const courseFormRef = ref()

const courseForm = ref({
  courseName: '',
  courseCode: '',
  credits: 1,
  teacherId: '',
  semester: '',
  description: ''
})

const rules = {
  courseName: [
    { required: true, message: '请输入课程名称', trigger: 'blur' }
  ],
  courseCode: [
    { required: true, message: '请输入课程代码', trigger: 'blur' }
  ],
  credits: [
    { required: true, message: '请输入学分', trigger: 'blur' }
  ],
  teacherId: [
    { required: true, message: '请选择授课教师', trigger: 'change' }
  ],
  semester: [
    { required: true, message: '请输入学期', trigger: 'blur' }
  ]
}

onMounted(() => {
  loadCourses()
  loadTeachers()
})

const loadCourses = async () => {
  loading.value = true
  try {
    // 这里应该调用API获取课程数据
    // 模拟数据
    courses.value = [
      {
        courseId: 1,
        courseName: '高等数学',
        courseCode: 'MATH001',
        credits: 4,
        teacherId: 1,
        teacherName: '张教授',
        semester: '2024春季',
        status: 1,
        description: '高等数学基础课程'
      },
      {
        courseId: 2,
        courseName: '线性代数',
        courseCode: 'MATH002',
        credits: 3,
        teacherId: 2,
        teacherName: '李教授',
        semester: '2024春季',
        status: 1,
        description: '线性代数基础课程'
      }
    ]
    total.value = courses.value.length
  } catch (error) {
    ElMessage.error('加载课程数据失败')
  } finally {
    loading.value = false
  }
}

const loadTeachers = async () => {
  try {
    // 这里应该调用API获取教师数据
    // 模拟数据
    teachers.value = [
      { userId: 1, userName: '张教授' },
      { userId: 2, userName: '李教授' },
      { userId: 3, userName: '王教授' }
    ]
  } catch (error) {
    ElMessage.error('加载教师数据失败')
  }
}

const editCourse = (course) => {
  editingCourse.value = course
  courseForm.value = { ...course }
  showAddDialog.value = true
}

const saveCourse = async () => {
  if (!courseFormRef.value) return
  
  await courseFormRef.value.validate((valid) => {
    if (valid) {
      try {
        if (editingCourse.value) {
          // 更新课程
          const index = courses.value.findIndex(c => c.courseId === editingCourse.value.courseId)
          if (index !== -1) {
            courses.value[index] = { ...courseForm.value, courseId: editingCourse.value.courseId }
          }
          ElMessage.success('课程更新成功')
        } else {
          // 添加课程
          const newCourse = {
            ...courseForm.value,
            courseId: Date.now(),
            teacherName: teachers.value.find(t => t.userId === courseForm.value.teacherId)?.userName,
            status: 1
          }
          courses.value.push(newCourse)
          total.value++
          ElMessage.success('课程添加成功')
        }
        
        showAddDialog.value = false
        resetForm()
      } catch (error) {
        ElMessage.error('保存失败')
      }
    }
  })
}

const toggleStatus = async (course) => {
  try {
    await ElMessageBox.confirm(
      `确定要${course.status === 1 ? '禁用' : '启用'}课程"${course.courseName}"吗？`,
      '确认操作',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    course.status = course.status === 1 ? 0 : 1
    ElMessage.success(`课程${course.status === 1 ? '启用' : '禁用'}成功`)
  } catch {
    // 用户取消操作
  }
}

const resetForm = () => {
  courseForm.value = {
    courseName: '',
    courseCode: '',
    credits: 1,
    teacherId: '',
    semester: '',
    description: ''
  }
  editingCourse.value = null
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadCourses()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadCourses()
}
</script>

<style scoped>
.course-management {
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
</style> 