<template>
  <div class="user-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>用户管理</h2>
      <p>管理系统中的用户信息，包括学生和教师的基本信息维护</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="20" style="align-items: center;">
        <el-col :span="6">
          <el-input
            v-model="searchQuery"
            placeholder="搜索姓名、学号或工号"
            @keyup.enter="searchUsers"
            clearable
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-select v-model="selectedType" placeholder="选择类型" clearable @change="filterByType">
            <el-option
              v-for="type in userTypes"
              :key="type.value"
              :label="type.label"
              :value="type.value"
            />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="selectedDepartment" placeholder="选择院系" clearable @change="filterByDepartment">
            <el-option
              v-for="dept in departments"
              :key="dept.value"
              :label="dept.label"
              :value="dept.value"
            />
          </el-select>
        </el-col>
        <el-col :span="10">
          <el-button type="primary" @click="showAddDialog">
            <el-icon><Plus /></el-icon>
            添加用户
          </el-button>
          <el-button @click="batchDelete" :disabled="selectedUsers.length === 0">
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
          <el-button @click="exportData">
            <el-icon><Download /></el-icon>
            导出数据
          </el-button>
          <el-button @click="importData">
            <el-icon><Upload /></el-icon>
            导入数据
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 用户列表 -->
    <el-card>
      <el-table
        :data="filteredUsers"
        stripe
        @selection-change="handleSelectionChange"
        v-loading="loading"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="avatar" label="头像" width="80">
          <template #default="{ row }">
            <el-avatar :src="row.avatar" :size="40">
              {{ row.name.charAt(0) }}
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="code" label="学号/工号" width="120" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.type === 'student' ? 'success' : 'warning'">
              {{ row.type === 'student' ? '学生' : '教师' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="院系" width="120" />
        <el-table-column prop="major" label="专业/职位" width="150" />
        <el-table-column prop="grade" label="年级/职称" width="100" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
              {{ row.status === 'active' ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="viewUser(row)">查看</el-button>
            <el-button size="small" type="primary" @click="editUser(row)">编辑</el-button>
            <el-popconfirm
              title="确定要删除这个用户吗？"
              @confirm="deleteUser(row)"
            >
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="totalUsers"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑用户弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEditing ? '编辑用户' : '添加用户'"
      width="60%"
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="userForm.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型" prop="type">
              <el-select v-model="userForm.type" style="width: 100%" @change="handleTypeChange">
                <el-option label="学生" value="student" />
                <el-option label="教师" value="teacher" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="userForm.type === 'student' ? '学号' : '工号'" prop="code">
              <el-input v-model="userForm.code" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="院系" prop="department">
              <el-select v-model="userForm.department" style="width: 100%">
                <el-option
                  v-for="dept in departments"
                  :key="dept.value"
                  :label="dept.label"
                  :value="dept.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="userForm.type === 'student' ? '专业' : '职位'" prop="major">
              <el-input v-model="userForm.major" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="userForm.type === 'student' ? '年级' : '职称'" prop="grade">
              <el-input v-model="userForm.grade" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="userForm.phone" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="userForm.email" type="email" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="userForm.gender">
                <el-radio value="male">男</el-radio>
                <el-radio value="female">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker
                v-model="userForm.birthDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="userForm.status">
                <el-radio value="active">正常</el-radio>
                <el-radio value="inactive">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入学/入职时间" prop="joinDate">
              <el-date-picker
                v-model="userForm.joinDate"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="地址">
          <el-input v-model="userForm.address" />
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="userForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveUser">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看用户详情弹窗 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="用户详情"
      width="50%"
    >
      <div v-if="selectedUser" class="user-detail">
        <div class="detail-header">
          <el-avatar :src="selectedUser.avatar" :size="80">
            {{ selectedUser.name.charAt(0) }}
          </el-avatar>
          <div class="detail-info">
            <h3>{{ selectedUser.name }}</h3>
            <p>{{ selectedUser.code }}</p>
            <el-tag :type="selectedUser.type === 'student' ? 'success' : 'warning'">
              {{ selectedUser.type === 'student' ? '学生' : '教师' }}
            </el-tag>
          </div>
        </div>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="姓名">{{ selectedUser.name }}</el-descriptions-item>
          <el-descriptions-item :label="selectedUser.type === 'student' ? '学号' : '工号'">
            {{ selectedUser.code }}
          </el-descriptions-item>
          <el-descriptions-item label="类型">
            <el-tag :type="selectedUser.type === 'student' ? 'success' : 'warning'">
              {{ selectedUser.type === 'student' ? '学生' : '教师' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="院系">{{ selectedUser.department }}</el-descriptions-item>
          <el-descriptions-item :label="selectedUser.type === 'student' ? '专业' : '职位'">
            {{ selectedUser.major }}
          </el-descriptions-item>
          <el-descriptions-item :label="selectedUser.type === 'student' ? '年级' : '职称'">
            {{ selectedUser.grade }}
          </el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ selectedUser.phone }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ selectedUser.email }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ selectedUser.gender === 'male' ? '男' : '女' }}</el-descriptions-item>
          <el-descriptions-item label="出生日期">{{ selectedUser.birthDate }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="selectedUser.status === 'active' ? 'success' : 'danger'">
              {{ selectedUser.status === 'active' ? '正常' : '停用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item :label="selectedUser.type === 'student' ? '入学时间' : '入职时间'">
            {{ selectedUser.joinDate }}
          </el-descriptions-item>
          <el-descriptions-item label="地址" :span="2">{{ selectedUser.address || '无' }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ selectedUser.remark || '无' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 导入数据弹窗 -->
    <el-dialog
      v-model="importDialogVisible"
      title="导入用户数据"
      width="30%"
    >
      <el-upload
        class="upload-demo"
        drag
        action="#"
        :auto-upload="false"
        :on-change="handleFileChange"
        accept=".xlsx,.xls,.csv"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          将文件拖到此处，或<em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            只能上传 xlsx/xls/csv 文件，且不超过 2MB
          </div>
        </template>
      </el-upload>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="importDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmImport">确定导入</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Delete, Download, Upload, UploadFilled } from '@element-plus/icons-vue'

export default {
  name: 'UserManagement',
  components: {
    Search,
    Plus,
    Delete,
    Download,
    Upload,
    UploadFilled
  },
  setup() {
    const loading = ref(false)
    const searchQuery = ref('')
    const selectedType = ref('')
    const selectedDepartment = ref('')
    const selectedUsers = ref([])
    const currentPage = ref(1)
    const pageSize = ref(20)
    const totalUsers = ref(0)

    // 弹窗控制
    const dialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const importDialogVisible = ref(false)
    const isEditing = ref(false)

    // 表单引用
    const userFormRef = ref(null)

    // 选中的用户详情
    const selectedUser = ref(null)

    // 用户类型选项
    const userTypes = ref([
      { label: '学生', value: 'student' },
      { label: '教师', value: 'teacher' }
    ])

    // 院系选项
    const departments = ref([
      { label: '计算机学院', value: '计算机学院' },
      { label: '数学学院', value: '数学学院' },
      { label: '物理学院', value: '物理学院' },
      { label: '外语学院', value: '外语学院' },
      { label: '经济学院', value: '经济学院' },
      { label: '管理学院', value: '管理学院' }
    ])

    // 用户表单
    const userForm = reactive({
      name: '',
      code: '',
      type: 'student',
      department: '',
      major: '',
      grade: '',
      phone: '',
      email: '',
      gender: 'male',
      birthDate: '',
      status: 'active',
      joinDate: '',
      address: '',
      remark: ''
    })

    // 表单验证规则
    const userRules = reactive({
      name: [
        { required: true, message: '请输入姓名', trigger: 'blur' }
      ],
      code: [
        { required: true, message: '请输入学号/工号', trigger: 'blur' }
      ],
      type: [
        { required: true, message: '请选择类型', trigger: 'change' }
      ],
      department: [
        { required: true, message: '请选择院系', trigger: 'change' }
      ],
      major: [
        { required: true, message: '请输入专业/职位', trigger: 'blur' }
      ],
      phone: [
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
      ],
      email: [
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ]
    })

    // 模拟用户数据
    const users = ref([
      {
        id: 1,
        name: '张三',
        code: '2023001',
        type: 'student',
        department: '计算机学院',
        major: '计算机科学与技术',
        grade: '2023',
        phone: '13800138001',
        email: 'zhangsan@example.com',
        gender: 'male',
        birthDate: '2005-03-15',
        status: 'active',
        joinDate: '2023-09-01',
        address: '北京市海淀区',
        avatar: '',
        remark: '优秀学生'
      },
      {
        id: 2,
        name: '李四',
        code: '2023002',
        type: 'student',
        department: '数学学院',
        major: '数学与应用数学',
        grade: '2023',
        phone: '13800138002',
        email: 'lisi@example.com',
        gender: 'female',
        birthDate: '2005-07-20',
        status: 'active',
        joinDate: '2023-09-01',
        address: '上海市浦东新区',
        avatar: '',
        remark: ''
      },
      {
        id: 3,
        name: '王教授',
        code: 'T001',
        type: 'teacher',
        department: '计算机学院',
        major: '软件工程系',
        grade: '教授',
        phone: '13800138003',
        email: 'wangprof@example.com',
        gender: 'male',
        birthDate: '1975-12-10',
        status: 'active',
        joinDate: '2010-09-01',
        address: '北京市朝阳区',
        avatar: '',
        remark: '博士生导师'
      },
      {
        id: 4,
        name: '刘老师',
        code: 'T002',
        type: 'teacher',
        department: '数学学院',
        major: '高等数学教研室',
        grade: '副教授',
        phone: '13800138004',
        email: 'liuteacher@example.com',
        gender: 'female',
        birthDate: '1980-05-08',
        status: 'active',
        joinDate: '2015-03-01',
        address: '北京市西城区',
        avatar: '',
        remark: '优秀教师'
      },
      {
        id: 5,
        name: '陈小明',
        code: '2022005',
        type: 'student',
        department: '物理学院',
        major: '应用物理学',
        grade: '2022',
        phone: '13800138005',
        email: 'chenxm@example.com',
        gender: 'male',
        birthDate: '2004-11-25',
        status: 'inactive',
        joinDate: '2022-09-01',
        address: '广东省深圳市',
        avatar: '',
        remark: '因个人原因休学'
      }
    ])

    // 过滤后的用户列表
    const filteredUsers = computed(() => {
      let filtered = users.value

      // 搜索过滤
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        filtered = filtered.filter(user =>
          user.name.toLowerCase().includes(query) ||
          user.code.toLowerCase().includes(query)
        )
      }

      // 类型过滤
      if (selectedType.value) {
        filtered = filtered.filter(user => user.type === selectedType.value)
      }

      // 院系过滤
      if (selectedDepartment.value) {
        filtered = filtered.filter(user => user.department === selectedDepartment.value)
      }

      totalUsers.value = filtered.length
      return filtered
    })

    // 搜索用户
    const searchUsers = () => {
      currentPage.value = 1
    }

    // 按类型筛选
    const filterByType = () => {
      currentPage.value = 1
    }

    // 按院系筛选
    const filterByDepartment = () => {
      currentPage.value = 1
    }

    // 显示添加弹窗
    const showAddDialog = () => {
      isEditing.value = false
      resetForm()
      dialogVisible.value = true
    }

    // 编辑用户
    const editUser = (user) => {
      isEditing.value = true
      Object.assign(userForm, {
        id: user.id,
        name: user.name,
        code: user.code,
        type: user.type,
        department: user.department,
        major: user.major,
        grade: user.grade,
        phone: user.phone,
        email: user.email,
        gender: user.gender,
        birthDate: user.birthDate,
        status: user.status,
        joinDate: user.joinDate,
        address: user.address,
        remark: user.remark
      })
      dialogVisible.value = true
    }

    // 查看用户详情
    const viewUser = (user) => {
      selectedUser.value = user
      viewDialogVisible.value = true
    }

    // 删除用户
    const deleteUser = (user) => {
      const index = users.value.findIndex(item => item.id === user.id)
      if (index > -1) {
        users.value.splice(index, 1)
        ElMessage.success('用户删除成功')
      }
    }

    // 批量删除
    const batchDelete = () => {
      if (selectedUsers.value.length === 0) {
        ElMessage.warning('请选择要删除的用户')
        return
      }

      ElMessageBox.confirm(
        `确定要删除选中的 ${selectedUsers.value.length} 个用户吗？`,
        '批量删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        const selectedIds = selectedUsers.value.map(user => user.id)
        users.value = users.value.filter(user => !selectedIds.includes(user.id))
        selectedUsers.value = []
        ElMessage.success('批量删除成功')
      })
    }

    // 保存用户
    const saveUser = () => {
      userFormRef.value.validate((valid) => {
        if (valid) {
          if (isEditing.value) {
            // 编辑用户
            const index = users.value.findIndex(user => user.id === userForm.id)
            if (index > -1) {
              Object.assign(users.value[index], userForm)
              ElMessage.success('用户更新成功')
            }
          } else {
            // 添加用户
            const newUser = {
              ...userForm,
              id: Date.now(),
              avatar: ''
            }
            users.value.unshift(newUser)
            ElMessage.success('用户添加成功')
          }
          dialogVisible.value = false
        }
      })
    }

    // 重置表单
    const resetForm = () => {
      Object.assign(userForm, {
        name: '',
        code: '',
        type: 'student',
        department: '',
        major: '',
        grade: '',
        phone: '',
        email: '',
        gender: 'male',
        birthDate: '',
        status: 'active',
        joinDate: '',
        address: '',
        remark: ''
      })
    }

    // 处理类型变化
    const handleTypeChange = () => {
      // 可以根据类型变化调整表单
    }

    // 处理选择变化
    const handleSelectionChange = (selection) => {
      selectedUsers.value = selection
    }

    // 分页处理
    const handleSizeChange = (size) => {
      pageSize.value = size
      currentPage.value = 1
    }

    const handleCurrentChange = (page) => {
      currentPage.value = page
    }

    // 导出数据
    const exportData = () => {
      ElMessage.success('数据导出成功')
    }

    // 导入数据
    const importData = () => {
      importDialogVisible.value = true
    }

    // 处理文件上传
    const handleFileChange = (file) => {
      // 处理文件上传逻辑
    }

    // 确认导入
    const confirmImport = () => {
      ElMessage.success('数据导入成功')
      importDialogVisible.value = false
    }

    onMounted(() => {
      // 初始化数据
      totalUsers.value = users.value.length
    })

    return {
      loading,
      searchQuery,
      selectedType,
      selectedDepartment,
      selectedUsers,
      currentPage,
      pageSize,
      totalUsers,
      dialogVisible,
      viewDialogVisible,
      importDialogVisible,
      isEditing,
      userFormRef,
      selectedUser,
      userTypes,
      departments,
      userForm,
      userRules,
      users,
      filteredUsers,
      searchUsers,
      filterByType,
      filterByDepartment,
      showAddDialog,
      editUser,
      viewUser,
      deleteUser,
      batchDelete,
      saveUser,
      resetForm,
      handleTypeChange,
      handleSelectionChange,
      handleSizeChange,
      handleCurrentChange,
      exportData,
      importData,
      handleFileChange,
      confirmImport
    }
  }
}
</script>

<style scoped>
.user-management {
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

.toolbar-card {
  margin-bottom: 20px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.user-detail {
  padding: 10px 0;
}

.detail-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #EBEEF5;
}

.detail-info {
  margin-left: 20px;
}

.detail-info h3 {
  margin: 0 0 5px 0;
  color: #303133;
}

.detail-info p {
  margin: 0 0 10px 0;
  color: #909399;
  font-size: 14px;
}

.upload-demo {
  text-align: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 