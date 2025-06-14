<template>
  <div class="course-info-management">
    <div class="page-header">
      <h2>课程信息管理</h2>
      <p>管理系统中的所有课程信息</p>
    </div>
    
    <el-card class="main-card">
      <!-- 操作栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加课程
          </el-button>
          <el-button type="danger" :disabled="!hasSelection" @click="handleBatchDelete">
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-input
            v-model="searchText"
            placeholder="搜索课程名称或编码"
            clearable
            @input="handleSearch"
            style="width: 300px"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </div>
      
      <!-- 课程表格 -->
      <el-table
        :data="courses"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        
        <el-table-column prop="courseCode" label="课程编码" width="120" />
        
        <el-table-column prop="courseName" label="课程名称" min-width="150" />
        
        <el-table-column prop="credits" label="学分" width="80" align="center" />
        
        <el-table-column prop="courseType" label="课程类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getCourseTypeTagType(row.courseType)">
              {{ getCourseTypeText(row.courseType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="department" label="开课院系" min-width="120" />
        
        <el-table-column prop="teacher" label="授课教师" width="100" />
        
        <el-table-column prop="capacity" label="容量" width="80" align="center" />
        
        <el-table-column prop="enrolled" label="已选" width="80" align="center" />
        
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '开放' : '关闭' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button size="small" type="info" @click="handleView(row)">
              详情
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 添加/编辑课程弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑课程' : '添加课程'"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="课程编码" prop="courseCode">
          <el-input v-model="form.courseCode" placeholder="请输入课程编码" />
        </el-form-item>
        
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" />
        </el-form-item>
        
        <el-form-item label="学分" prop="credits">
          <el-input-number v-model="form.credits" :min="0" :max="10" :precision="1" />
        </el-form-item>
        
        <el-form-item label="课程类型" prop="courseType">
          <el-select v-model="form.courseType" placeholder="请选择课程类型">
            <el-option label="必修课" :value="1" />
            <el-option label="选修课" :value="2" />
            <el-option label="实践课" :value="3" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="开课院系" prop="department">
          <el-input v-model="form.department" placeholder="请输入开课院系" />
        </el-form-item>
        
        <el-form-item label="授课教师" prop="teacher">
          <el-input v-model="form.teacher" placeholder="请输入授课教师" />
        </el-form-item>
        
        <el-form-item label="课程容量" prop="capacity">
          <el-input-number v-model="form.capacity" :min="1" :max="500" />
        </el-form-item>
        
        <el-form-item label="课程描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入课程描述"
          />
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">开放</el-radio>
            <el-radio :label="0">关闭</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>
    
    <!-- 课程详情弹窗 -->
    <el-dialog v-model="detailVisible" title="课程详情" width="500px">
      <div class="course-detail" v-if="currentCourse">
        <div class="detail-item">
          <label>课程编码：</label>
          <span>{{ currentCourse.courseCode }}</span>
        </div>
        <div class="detail-item">
          <label>课程名称：</label>
          <span>{{ currentCourse.courseName }}</span>
        </div>
        <div class="detail-item">
          <label>学分：</label>
          <span>{{ currentCourse.credits }}</span>
        </div>
        <div class="detail-item">
          <label>课程类型：</label>
          <span>{{ getCourseTypeText(currentCourse.courseType) }}</span>
        </div>
        <div class="detail-item">
          <label>开课院系：</label>
          <span>{{ currentCourse.department }}</span>
        </div>
        <div class="detail-item">
          <label>授课教师：</label>
          <span>{{ currentCourse.teacher }}</span>
        </div>
        <div class="detail-item">
          <label>容量/已选：</label>
          <span>{{ currentCourse.capacity }} / {{ currentCourse.enrolled }}</span>
        </div>
        <div class="detail-item">
          <label>状态：</label>
          <el-tag :type="currentCourse.status === 1 ? 'success' : 'danger'">
            {{ currentCourse.status === 1 ? '开放' : '关闭' }}
          </el-tag>
        </div>
        <div class="detail-item" v-if="currentCourse.description">
          <label>课程描述：</label>
          <p class="description">{{ currentCourse.description }}</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Search } from '@element-plus/icons-vue'
import { 
  getCourseList, 
  createCourse, 
  updateCourse, 
  deleteCourse, 
  deleteCourses 
} from '@/api/course'

// 数据
const loading = ref(false)
const courses = ref([])
const searchText = ref('')
const selectedCourses = ref([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

// 弹窗状态
const dialogVisible = ref(false)
const detailVisible = ref(false)
const isEdit = ref(false)
const currentCourse = ref(null)

// 表单
const formRef = ref()
const form = reactive({
  courseCode: '',
  courseName: '',
  credits: 0,
  courseType: 1,
  department: '',
  teacher: '',
  capacity: 30,
  description: '',
  status: 1
})

// 表单验证规则
const rules = {
  courseCode: [
    { required: true, message: '请输入课程编码', trigger: 'blur' }
  ],
  courseName: [
    { required: true, message: '请输入课程名称', trigger: 'blur' }
  ],
  credits: [
    { required: true, message: '请输入学分', trigger: 'blur' }
  ],
  courseType: [
    { required: true, message: '请选择课程类型', trigger: 'change' }
  ],
  department: [
    { required: true, message: '请输入开课院系', trigger: 'blur' }
  ],
  teacher: [
    { required: true, message: '请输入授课教师', trigger: 'blur' }
  ],
  capacity: [
    { required: true, message: '请输入课程容量', trigger: 'blur' }
  ]
}

// 计算属性
const hasSelection = computed(() => selectedCourses.value.length > 0)

// 方法
const getCourseTypeText = (type) => {
  const typeMap = { 1: '必修课', 2: '选修课', 3: '实践课' }
  return typeMap[type] || '未知'
}

const getCourseTypeTagType = (type) => {
  const typeMap = { 1: 'danger', 2: 'warning', 3: 'success' }
  return typeMap[type] || ''
}

const loadCourses = async () => {
  loading.value = true
  try {
    const params = {
      current: currentPage.value,
      size: pageSize.value,
      keyword: searchText.value || undefined
    }
    
    const response = await getCourseList(params)
    if (response.code === 200) {
      courses.value = response.data.records
      total.value = response.data.total
    } else {
      ElMessage.error(response.message || '加载课程数据失败')
    }
  } catch (error) {
    console.error('加载课程数据失败:', error)
    ElMessage.error('加载课程数据失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadCourses()
}

const handleSelectionChange = (selection) => {
  selectedCourses.value = selection
}

const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleView = (row) => {
  currentCourse.value = row
  detailVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除课程"${row.courseName}"吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await deleteCourse(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadCourses()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除课程失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的${selectedCourses.value.length}个课程吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const ids = selectedCourses.value.map(item => item.id)
    const response = await deleteCourses(ids)
    if (response.code === 200) {
      ElMessage.success('批量删除成功')
      selectedCourses.value = []
      loadCourses()
    } else {
      ElMessage.error(response.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除课程失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    if (isEdit.value) {
      // 编辑课程
      const response = await updateCourse(form.id, form)
      if (response.code === 200) {
        ElMessage.success('修改成功')
        dialogVisible.value = false
        loadCourses()
      } else {
        ElMessage.error(response.message || '修改失败')
      }
    } else {
      // 添加课程
      const response = await createCourse(form)
      if (response.code === 200) {
        ElMessage.success('添加成功')
        dialogVisible.value = false
        loadCourses()
      } else {
        ElMessage.error(response.message || '添加失败')
      }
    }
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

const resetForm = () => {
  Object.assign(form, {
    courseCode: '',
    courseName: '',
    credits: 0,
    courseType: 1,
    department: '',
    teacher: '',
    capacity: 30,
    description: '',
    status: 1
  })
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadCourses()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadCourses()
}

// 生命周期
onMounted(() => {
  loadCourses()
})
</script>

<style scoped>
.course-info-management {
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
}

.main-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.toolbar-left {
  display: flex;
  gap: 10px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.course-detail {
  padding: 10px 0;
}

.detail-item {
  display: flex;
  margin-bottom: 15px;
  align-items: flex-start;
}

.detail-item label {
  flex: 0 0 100px;
  font-weight: 600;
  color: #606266;
}

.detail-item span {
  flex: 1;
  color: #303133;
}

.description {
  margin: 0;
  line-height: 1.6;
  color: #606266;
}

.dialog-footer {
  text-align: right;
}
</style> 