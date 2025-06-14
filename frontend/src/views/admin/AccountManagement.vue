<template>
  <div class="account-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>账号管理</h2>
      <p>管理系统中所有用户账号，包括学生、教师和管理员</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="20" style="align-items: center;">
        <el-col :span="6">
          <el-input
            v-model="searchQuery"
            placeholder="搜索用户名、邮箱或姓名"
            @keyup.enter="searchAccounts"
            clearable
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-select v-model="selectedRole" placeholder="选择角色" clearable @change="filterByRole">
            <el-option
              v-for="role in roles"
              :key="role.value"
              :label="role.label"
              :value="role.value"
            />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="selectedStatus" placeholder="选择状态" clearable @change="filterByStatus">
            <el-option
              v-for="status in statusOptions"
              :key="status.value"
              :label="status.label"
              :value="status.value"
            />
          </el-select>
        </el-col>
        <el-col :span="10">
          <el-button type="primary" @click="showAddDialog">
            <el-icon><Plus /></el-icon>
            添加账号
          </el-button>
          <el-button @click="batchDelete" :disabled="selectedAccounts.length === 0">
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

    <!-- 账号列表 -->
    <el-card>
      <el-table
        :data="filteredAccounts"
        stripe
        @selection-change="handleSelectionChange"
        v-loading="loading"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="avatar" label="头像" width="80">
          <template #default="{ row }">
            <el-avatar :src="row.avatar" :size="40">
              {{ row.username.charAt(0).toUpperCase() }}
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="getRoleTagType(row.role)">
              {{ getRoleLabel(row.role) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="院系" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'">
              {{ row.status === 'active' ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastLogin" label="最后登录" width="160" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" fixed="right" width="120">
          <template #default="{ row }">
            <el-dropdown @command="(command) => handleCommand(command, row)">
              <el-button size="small" type="primary">
                操作
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="view">
                    <el-icon><View /></el-icon>
                    查看详情
                  </el-dropdown-item>
                  <el-dropdown-item command="edit">
                    <el-icon><Edit /></el-icon>
                    编辑账户
                  </el-dropdown-item>
                  <el-dropdown-item command="toggle" divided>
                    <el-icon><Switch /></el-icon>
                    {{ row.status === 'active' ? '禁用账户' : '启用账户' }}
                  </el-dropdown-item>
                  <el-dropdown-item command="delete" divided>
                    <el-icon><Delete /></el-icon>
                    删除账户
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="totalAccounts"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑账号弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEditing ? '编辑账号' : '添加账号'"
      width="50%"
    >
      <el-form
        ref="accountFormRef"
        :model="accountForm"
        :rules="accountRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="accountForm.username" :disabled="isEditing" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="accountForm.realName" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="accountForm.email" type="email" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="accountForm.phone" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="角色" prop="role">
              <el-select v-model="accountForm.role" style="width: 100%">
                <el-option
                  v-for="role in roles"
                  :key="role.value"
                  :label="role.label"
                  :value="role.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="院系" prop="department">
              <el-select v-model="accountForm.department" style="width: 100%">
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

        <el-row :gutter="20" v-if="!isEditing">
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="accountForm.password" type="password" show-password />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="accountForm.confirmPassword" type="password" show-password />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="状态">
          <el-radio-group v-model="accountForm.status">
            <el-radio value="active">正常</el-radio>
            <el-radio value="inactive">禁用</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="accountForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveAccount">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看账号详情弹窗 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="账号详情"
      width="40%"
    >
      <div v-if="selectedAccount" class="account-detail">
        <div class="detail-header">
          <el-avatar :src="selectedAccount.avatar" :size="80">
            {{ selectedAccount.username.charAt(0).toUpperCase() }}
          </el-avatar>
          <div class="detail-info">
            <h3>{{ selectedAccount.realName }}</h3>
            <p>{{ selectedAccount.username }}</p>
            <el-tag :type="getRoleTagType(selectedAccount.role)">
              {{ getRoleLabel(selectedAccount.role) }}
            </el-tag>
          </div>
        </div>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="用户名">{{ selectedAccount.username }}</el-descriptions-item>
          <el-descriptions-item label="真实姓名">{{ selectedAccount.realName }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ selectedAccount.email }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ selectedAccount.phone }}</el-descriptions-item>
          <el-descriptions-item label="角色">
            <el-tag :type="getRoleTagType(selectedAccount.role)">
              {{ getRoleLabel(selectedAccount.role) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="院系">{{ selectedAccount.department }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="selectedAccount.status === 'active' ? 'success' : 'danger'">
              {{ selectedAccount.status === 'active' ? '正常' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="最后登录">{{ selectedAccount.lastLogin }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ selectedAccount.createTime }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">
            {{ selectedAccount.remark || '无' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 导入数据弹窗 -->
    <el-dialog
      v-model="importDialogVisible"
      title="导入账号数据"
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
import { Search, Plus, Delete, Download, Upload, UploadFilled, ArrowDown, View, Edit, Switch } from '@element-plus/icons-vue'

export default {
  name: 'AccountManagement',
  components: {
    Search,
    Plus,
    Delete,
    Download,
    Upload,
    UploadFilled,
    ArrowDown,
    View,
    Edit,
    Switch
  },
  setup() {
    const loading = ref(false)
    const searchQuery = ref('')
    const selectedRole = ref('')
    const selectedStatus = ref('')
    const selectedAccounts = ref([])
    const currentPage = ref(1)
    const pageSize = ref(20)
    const totalAccounts = ref(0)

    // 弹窗控制
    const dialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const importDialogVisible = ref(false)
    const isEditing = ref(false)

    // 表单引用
    const accountFormRef = ref(null)

    // 选中的账号详情
    const selectedAccount = ref(null)

    // 角色选项
    const roles = ref([
      { label: '学生', value: 'student' },
      { label: '教师', value: 'teacher' },
      { label: '管理员', value: 'admin' }
    ])

    // 状态选项
    const statusOptions = ref([
      { label: '正常', value: 'active' },
      { label: '禁用', value: 'inactive' }
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

    // 账号表单
    const accountForm = reactive({
      username: '',
      realName: '',
      email: '',
      phone: '',
      role: '',
      department: '',
      password: '',
      confirmPassword: '',
      status: 'active',
      remark: ''
    })

    // 表单验证规则
    const accountRules = reactive({
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      realName: [
        { required: true, message: '请输入真实姓名', trigger: 'blur' }
      ],
      email: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ],
      phone: [
        { required: true, message: '请输入手机号码', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
      ],
      role: [
        { required: true, message: '请选择角色', trigger: 'change' }
      ],
      department: [
        { required: true, message: '请选择院系', trigger: 'change' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur' },
        {
          validator: (rule, value, callback) => {
            if (value !== accountForm.password) {
              callback(new Error('两次输入的密码不一致'))
            } else {
              callback()
            }
          },
          trigger: 'blur'
        }
      ]
    })

    // 模拟账号数据
    const accounts = ref([
      {
        id: 1,
        username: 'admin',
        realName: '系统管理员',
        email: 'admin@example.com',
        phone: '13800138000',
        role: 'admin',
        department: '管理学院',
        status: 'active',
        lastLogin: '2024-01-15 14:30:00',
        createTime: '2023-09-01 09:00:00',
        avatar: '',
        remark: '系统超级管理员'
      },
      {
        id: 2,
        username: 'teacher001',
        realName: '张教授',
        email: 'zhang@example.com',
        phone: '13800138001',
        role: 'teacher',
        department: '计算机学院',
        status: 'active',
        lastLogin: '2024-01-15 10:15:00',
        createTime: '2023-09-05 10:30:00',
        avatar: '',
        remark: '计算机系主任'
      },
      {
        id: 3,
        username: 'student001',
        realName: '李小明',
        email: 'li@example.com',
        phone: '13800138002',
        role: 'student',
        department: '计算机学院',
        status: 'active',
        lastLogin: '2024-01-15 16:45:00',
        createTime: '2023-09-10 14:20:00',
        avatar: '',
        remark: '2023级计算机科学与技术专业'
      },
      {
        id: 4,
        username: 'teacher002',
        realName: '王老师',
        email: 'wang@example.com',
        phone: '13800138003',
        role: 'teacher',
        department: '数学学院',
        status: 'active',
        lastLogin: '2024-01-14 20:30:00',
        createTime: '2023-09-03 11:45:00',
        avatar: '',
        remark: '高等数学教师'
      },
      {
        id: 5,
        username: 'student002',
        realName: '刘小红',
        email: 'liu@example.com',
        phone: '13800138004',
        role: 'student',
        department: '物理学院',
        status: 'inactive',
        lastLogin: '2024-01-10 13:20:00',
        createTime: '2023-09-12 16:10:00',
        avatar: '',
        remark: '因违纪暂时禁用'
      }
    ])

    // 过滤后的账号列表
    const filteredAccounts = computed(() => {
      let filtered = accounts.value

      // 搜索过滤
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        filtered = filtered.filter(account =>
          account.username.toLowerCase().includes(query) ||
          account.realName.toLowerCase().includes(query) ||
          account.email.toLowerCase().includes(query)
        )
      }

      // 角色过滤
      if (selectedRole.value) {
        filtered = filtered.filter(account => account.role === selectedRole.value)
      }

      // 状态过滤
      if (selectedStatus.value) {
        filtered = filtered.filter(account => account.status === selectedStatus.value)
      }

      totalAccounts.value = filtered.length
      return filtered
    })

    // 获取角色标签类型
    const getRoleTagType = (role) => {
      const tagTypes = {
        admin: 'danger',
        teacher: 'warning',
        student: 'success'
      }
      return tagTypes[role] || ''
    }

    // 获取角色标签文本
    const getRoleLabel = (role) => {
      const roleLabels = {
        admin: '管理员',
        teacher: '教师',
        student: '学生'
      }
      return roleLabels[role] || role
    }

    // 搜索账号
    const searchAccounts = () => {
      // 触发计算属性重新计算
      currentPage.value = 1
    }

    // 按角色筛选
    const filterByRole = () => {
      currentPage.value = 1
    }

    // 按状态筛选
    const filterByStatus = () => {
      currentPage.value = 1
    }

    // 显示添加弹窗
    const showAddDialog = () => {
      isEditing.value = false
      resetForm()
      dialogVisible.value = true
    }

    // 编辑账号
    const editAccount = (account) => {
      isEditing.value = true
      Object.assign(accountForm, {
        id: account.id,
        username: account.username,
        realName: account.realName,
        email: account.email,
        phone: account.phone,
        role: account.role,
        department: account.department,
        status: account.status,
        remark: account.remark
      })
      dialogVisible.value = true
    }

    // 查看账号详情
    const viewAccount = (account) => {
      selectedAccount.value = account
      viewDialogVisible.value = true
    }

    // 切换账号状态
    const toggleStatus = (account) => {
      const newStatus = account.status === 'active' ? 'inactive' : 'active'
      const actionText = newStatus === 'active' ? '启用' : '禁用'
      
      ElMessageBox.confirm(
        `确定要${actionText}账号 "${account.username}" 吗？`,
        '确认操作',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        account.status = newStatus
        ElMessage.success(`账号已${actionText}`)
      })
    }

    // 删除账号
    const deleteAccount = (account) => {
      const index = accounts.value.findIndex(item => item.id === account.id)
      if (index > -1) {
        accounts.value.splice(index, 1)
        ElMessage.success('账号删除成功')
      }
    }

    // 批量删除
    const batchDelete = () => {
      if (selectedAccounts.value.length === 0) {
        ElMessage.warning('请选择要删除的账号')
        return
      }

      ElMessageBox.confirm(
        `确定要删除选中的 ${selectedAccounts.value.length} 个账号吗？`,
        '批量删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        const selectedIds = selectedAccounts.value.map(account => account.id)
        accounts.value = accounts.value.filter(account => !selectedIds.includes(account.id))
        selectedAccounts.value = []
        ElMessage.success('批量删除成功')
      })
    }

    // 保存账号
    const saveAccount = () => {
      accountFormRef.value.validate((valid) => {
        if (valid) {
          if (isEditing.value) {
            // 编辑账号
            const index = accounts.value.findIndex(account => account.id === accountForm.id)
            if (index > -1) {
              Object.assign(accounts.value[index], accountForm)
              ElMessage.success('账号更新成功')
            }
          } else {
            // 添加账号
            const newAccount = {
              ...accountForm,
              id: Date.now(),
              lastLogin: '-',
              createTime: new Date().toLocaleString(),
              avatar: ''
            }
            accounts.value.unshift(newAccount)
            ElMessage.success('账号添加成功')
          }
          dialogVisible.value = false
        }
      })
    }

    // 重置表单
    const resetForm = () => {
      Object.assign(accountForm, {
        username: '',
        realName: '',
        email: '',
        phone: '',
        role: '',
        department: '',
        password: '',
        confirmPassword: '',
        status: 'active',
        remark: ''
      })
    }

    // 处理选择变化
    const handleSelectionChange = (selection) => {
      selectedAccounts.value = selection
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

    // 处理命令
    const handleCommand = (command, row) => {
      switch (command) {
        case 'view':
          viewAccount(row)
          break
        case 'edit':
          editAccount(row)
          break
        case 'toggle':
          toggleStatus(row)
          break
        case 'delete':
          deleteAccount(row)
          break
      }
    }

    onMounted(() => {
      // 初始化数据
      totalAccounts.value = accounts.value.length
    })

    return {
      loading,
      searchQuery,
      selectedRole,
      selectedStatus,
      selectedAccounts,
      currentPage,
      pageSize,
      totalAccounts,
      dialogVisible,
      viewDialogVisible,
      importDialogVisible,
      isEditing,
      accountFormRef,
      selectedAccount,
      roles,
      statusOptions,
      departments,
      accountForm,
      accountRules,
      accounts,
      filteredAccounts,
      getRoleTagType,
      getRoleLabel,
      searchAccounts,
      filterByRole,
      filterByStatus,
      showAddDialog,
      editAccount,
      viewAccount,
      toggleStatus,
      deleteAccount,
      batchDelete,
      saveAccount,
      resetForm,
      handleSelectionChange,
      handleSizeChange,
      handleCurrentChange,
      exportData,
      importData,
      handleFileChange,
      confirmImport,
      handleCommand
    }
  }
}
</script>

<style scoped>
.account-management {
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

.account-detail {
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