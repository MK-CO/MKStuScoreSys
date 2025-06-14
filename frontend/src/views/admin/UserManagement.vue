<template>
  <div class="user-management">
    <div class="header">
      <h2>用户管理</h2>
      <el-button type="primary" @click="showAddDialog = true">
        <el-icon><Plus /></el-icon>
        添加用户
      </el-button>
    </div>

    <el-table :data="users" style="width: 100%" v-loading="loading">
      <el-table-column prop="userId" label="用户ID" width="100" />
      <el-table-column prop="userName" label="用户名" />
      <el-table-column prop="realName" label="真实姓名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="userType" label="用户类型" width="100">
        <template #default="scope">
          <el-tag :type="getUserTypeColor(scope.row.userType)">
            {{ getUserTypeName(scope.row.userType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="editUser(scope.row)">编辑</el-button>
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

    <!-- 添加/编辑用户对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="editingUser ? '编辑用户' : '添加用户'"
      width="500px"
    >
      <el-form :model="userForm" :rules="rules" ref="userFormRef" label-width="100px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="userForm.userName" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="userForm.realName" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" />
        </el-form-item>
        <el-form-item label="用户类型" prop="userType">
          <el-select v-model="userForm.userType" placeholder="请选择用户类型">
            <el-option label="管理员" :value="1" />
            <el-option label="教师" :value="2" />
            <el-option label="学生" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!editingUser">
          <el-input v-model="userForm.password" type="password" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddDialog = false">取消</el-button>
          <el-button type="primary" @click="saveUser">确定</el-button>
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
const users = ref([])
const showAddDialog = ref(false)
const editingUser = ref(null)
const userFormRef = ref()

const userForm = ref({
  userName: '',
  realName: '',
  email: '',
  userType: '',
  password: ''
})

const rules = {
  userName: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  userType: [
    { required: true, message: '请选择用户类型', trigger: 'change' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

onMounted(() => {
  loadUsers()
})

const loadUsers = async () => {
  loading.value = true
  try {
    // 模拟数据
    users.value = [
      {
        userId: 1,
        userName: 'admin',
        realName: '系统管理员',
        email: 'admin@example.com',
        userType: 1,
        status: 1,
        createTime: '2024-01-01 10:00:00'
      },
      {
        userId: 2,
        userName: 'teacher1',
        realName: '张教授',
        email: 'zhang@example.com',
        userType: 2,
        status: 1,
        createTime: '2024-01-02 10:00:00'
      },
      {
        userId: 3,
        userName: 'student1',
        realName: '李同学',
        email: 'li@example.com',
        userType: 3,
        status: 1,
        createTime: '2024-01-03 10:00:00'
      }
    ]
  } catch (error) {
    ElMessage.error('加载用户数据失败')
  } finally {
    loading.value = false
  }
}

const getUserTypeName = (type) => {
  const typeMap = {
    1: '管理员',
    2: '教师',
    3: '学生'
  }
  return typeMap[type] || '未知'
}

const getUserTypeColor = (type) => {
  const colorMap = {
    1: 'danger',
    2: 'warning',
    3: 'success'
  }
  return colorMap[type] || ''
}

const editUser = (user) => {
  editingUser.value = user
  userForm.value = { ...user }
  delete userForm.value.password // 编辑时不显示密码
  showAddDialog.value = true
}

const saveUser = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate((valid) => {
    if (valid) {
      try {
        if (editingUser.value) {
          // 更新用户
          const index = users.value.findIndex(u => u.userId === editingUser.value.userId)
          if (index !== -1) {
            users.value[index] = { ...userForm.value, userId: editingUser.value.userId }
          }
          ElMessage.success('用户更新成功')
        } else {
          // 添加用户
          const newUser = {
            ...userForm.value,
            userId: Date.now(),
            status: 1,
            createTime: new Date().toLocaleString()
          }
          users.value.push(newUser)
          ElMessage.success('用户添加成功')
        }
        
        showAddDialog.value = false
        resetForm()
      } catch (error) {
        ElMessage.error('保存失败')
      }
    }
  })
}

const toggleStatus = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要${user.status === 1 ? '禁用' : '启用'}用户"${user.userName}"吗？`,
      '确认操作',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    user.status = user.status === 1 ? 0 : 1
    ElMessage.success(`用户${user.status === 1 ? '启用' : '禁用'}成功`)
  } catch {
    // 用户取消操作
  }
}

const resetForm = () => {
  userForm.value = {
    userName: '',
    realName: '',
    email: '',
    userType: '',
    password: ''
  }
  editingUser.value = null
}
</script>

<style scoped>
.user-management {
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