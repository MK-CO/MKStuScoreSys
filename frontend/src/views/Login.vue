<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>
    
    <div class="login-content">
      <!-- 左侧信息区域 -->
      <div class="login-info">
        <div class="info-content">
          <div class="logo-section">
            <div class="logo-icon">
              <svg viewBox="0 0 1024 1024" width="64" height="64">
                <path fill="#4f46e5" d="M512 85.333333a426.666667 426.666667 0 1 1 0 853.333334 426.666667 426.666667 0 0 1 0-853.333334z m0 85.333334a341.333333 341.333333 0 1 0 0 682.666666 341.333333 341.333333 0 0 0 0-682.666666z m-149.333333 170.666666h298.666666v85.333334H362.666667v-85.333334z m0 170.666667h298.666666v85.333333H362.666667v-85.333333z m0 170.666667h213.333333v85.333333H362.666667v-85.333333z"/>
              </svg>
            </div>
            <h1 class="system-title">学生成绩管理系统</h1>
            <p class="system-subtitle">Student Score Management System</p>
          </div>
          
          <div class="feature-list">
            <div class="feature-item">
              <div class="feature-icon">📊</div>
              <div class="feature-text">
                <h3>智能分析</h3>
                <p>全面的成绩数据分析与可视化</p>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">👥</div>
              <div class="feature-text">
                <h3>多角色管理</h3>
                <p>支持管理员、教师、学生多重身份</p>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-icon">🔐</div>
              <div class="feature-text">
                <h3>安全可靠</h3>
                <p>完善的权限控制与数据安全保障</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧登录表单 -->
      <div class="login-form-section">
        <div class="form-container">
          <div class="form-header">
            <h2>欢迎登录</h2>
            <p>请选择您的身份并输入登录信息</p>
          </div>
          
          <el-form
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            class="login-form"
            @keyup.enter="handleLogin"
          >
            <!-- 用户类型选择 -->
            <div class="user-type-section">
              <p class="form-label">选择身份</p>
              <div class="user-type-tabs">
                <div 
                  class="user-type-tab"
                  :class="{ active: loginForm.userType === 3 }"
                  @click="loginForm.userType = 3"
                >
                  <div class="tab-icon">👨‍🎓</div>
                  <span>学生</span>
                </div>
                <div 
                  class="user-type-tab"
                  :class="{ active: loginForm.userType === 2 }"
                  @click="loginForm.userType = 2"
                >
                  <div class="tab-icon">👨‍🏫</div>
                  <span>教师</span>
                </div>
                <div 
                  class="user-type-tab"
                  :class="{ active: loginForm.userType === 1 }"
                  @click="loginForm.userType = 1"
                >
                  <div class="tab-icon">👨‍💼</div>
                  <span>管理员</span>
                </div>
              </div>
            </div>
            
            <!-- 用户名输入 -->
            <el-form-item prop="username" class="form-item">
              <label class="form-label">用户名</label>
              <el-input
                v-model="loginForm.username"
                placeholder="请输入用户名"
                size="large"
                class="custom-input"
              >
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            
            <!-- 密码输入 -->
            <el-form-item prop="password" class="form-item">
              <label class="form-label">密码</label>
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                class="custom-input"
                show-password
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            
            <!-- 登录按钮 -->
            <el-form-item class="form-item">
              <el-button
                type="primary"
                size="large"
                class="login-button"
                :loading="loading"
                @click="handleLogin"
              >
                <span v-if="!loading">立即登录</span>
                <span v-else>登录中...</span>
              </el-button>
            </el-form-item>
          </el-form>
          
          <!-- 测试账号提示 -->
          <div class="demo-accounts">
            <div class="demo-title">
              <el-icon><InfoFilled /></el-icon>
              <span>测试账号</span>
            </div>
            <div class="demo-list">
              <div class="demo-item">
                <span class="demo-role">管理员</span>
                <span class="demo-account">admin / 123456</span>
              </div>
              <div class="demo-item">
                <span class="demo-role">教师</span>
                <span class="demo-account">teacher001 / 123456</span>
              </div>
              <div class="demo-item">
                <span class="demo-role">学生</span>
                <span class="demo-account">student001 / 123456</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, InfoFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 表单引用
const loginFormRef = ref()

// 加载状态
const loading = ref(false)

// 登录表单
const loginForm = reactive({
  username: '',
  password: '',
  userType: 3 // 默认选择学生
})

// 表单验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return
    
    loading.value = true
    
    const result = await userStore.login(loginForm)
    
    if (result.success) {
      ElMessage.success('登录成功')
      
      // 根据用户类型跳转到对应页面
      switch (result.data.userType) {
        case 1:
          router.push('/admin')
          break
        case 2:
          router.push('/teacher')
          break
        case 3:
          router.push('/student')
          break
        default:
          router.push('/')
      }
    } else {
      ElMessage.error(result.message || '登录失败')
    }
  } catch (error) {
    console.error('登录错误:', error)
    ElMessage.error('登录失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
* {
  box-sizing: border-box;
}

.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -150px;
  right: -150px;
  animation: float 20s infinite linear;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -100px;
  left: -100px;
  animation: float 15s infinite linear reverse;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  left: 10%;
  animation: float 25s infinite linear;
}

@keyframes float {
  0% { transform: translate(0, 0) rotate(0deg); }
  33% { transform: translate(30px, -30px) rotate(120deg); }
  66% { transform: translate(-20px, 20px) rotate(240deg); }
  100% { transform: translate(0, 0) rotate(360deg); }
}

/* 主要内容区域 */
.login-content {
  display: flex;
  min-height: 100vh;
  position: relative;
  z-index: 1;
  width: 100%;
}

/* 左侧信息区域 */
.login-info {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30px;
  color: white;
  min-width: 0; /* 防止flex项目溢出 */
}

.info-content {
  max-width: 400px;
  width: 100%;
}

.logo-section {
  text-align: center;
  margin-bottom: 30px;
}

.logo-icon {
  margin-bottom: 12px;
}

.system-title {
  font-size: 2rem;
  font-weight: 700;
  margin: 0 0 8px 0;
  background: linear-gradient(45deg, #fff, #e0e7ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.system-subtitle {
  font-size: 0.95rem;
  opacity: 0.9;
  margin: 0;
  font-weight: 300;
  letter-spacing: 1px;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  gap: 14px;
}

.feature-icon {
  font-size: 1.6rem;
  min-width: 45px;
  height: 45px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
}

.feature-text h3 {
  margin: 0 0 4px 0;
  font-size: 1rem;
  font-weight: 600;
}

.feature-text p {
  margin: 0;
  opacity: 0.8;
  line-height: 1.3;
  font-size: 0.85rem;
}

/* 右侧登录表单区域 */
.login-form-section {
  flex: 0 0 460px;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 25px;
  box-shadow: -5px 0 15px rgba(0, 0, 0, 0.1);
}

.form-container {
  width: 100%;
  max-width: 380px;
}

.form-header {
  text-align: center;
  margin-bottom: 25px;
}

.form-header h2 {
  font-size: 1.5rem;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 5px 0;
}

.form-header p {
  color: #6b7280;
  margin: 0;
  font-size: 0.85rem;
}

/* 用户类型选择 */
.user-type-section {
  margin-bottom: 20px;
}

.user-type-tabs {
  display: flex;
  gap: 5px;
  margin-top: 8px;
}

.user-type-tab {
  flex: 1;
  padding: 10px 6px;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: white;
}

.user-type-tab:hover {
  border-color: #4f46e5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(79, 70, 229, 0.15);
}

.user-type-tab.active {
  border-color: #4f46e5;
  background: #4f46e5;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(79, 70, 229, 0.25);
}

.tab-icon {
  font-size: 1.2rem;
  margin-bottom: 4px;
}

.user-type-tab span {
  font-size: 0.8rem;
  font-weight: 500;
}

/* 表单样式 */
.form-label {
  display: block;
  margin-bottom: 5px;
  color: #374151;
  font-weight: 500;
  font-size: 0.8rem;
}

.form-item {
  margin-bottom: 18px;
}

:deep(.custom-input .el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 0 0 1px #e5e7eb;
  transition: all 0.3s ease;
  background: white;
  border: none;
  padding: 12px 16px;
}

:deep(.custom-input .el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #d1d5db;
}

:deep(.custom-input.is-focus .el-input__wrapper) {
  box-shadow: 0 0 0 2px #4f46e5;
}

:deep(.custom-input .el-input__inner) {
  font-size: 15px;
  color: #1f2937;
}

:deep(.custom-input .el-input__prefix-inner) {
  color: #6b7280;
}

/* 登录按钮 */
.login-button {
  width: 100%;
  height: 44px;
  border-radius: 10px;
  background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%);
  border: none;
  font-size: 15px;
  font-weight: 600;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(79, 70, 229, 0.4);
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(79, 70, 229, 0.5);
}

.login-button:active {
  transform: translateY(0);
}

/* 测试账号区域 */
.demo-accounts {
  margin-top: 20px;
  padding: 14px;
  background: #f8fafc;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

.demo-title {
  display: flex;
  align-items: center;
  gap: 5px;
  margin-bottom: 10px;
  color: #475569;
  font-weight: 600;
  font-size: 0.8rem;
}

.demo-list {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.demo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.75rem;
}

.demo-role {
  color: #64748b;
  font-weight: 500;
}

.demo-account {
  color: #334155;
  font-family: monospace;
  background: white;
  padding: 4px 8px;
  border-radius: 6px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .login-form-section {
    flex: 0 0 420px;
  }
  
  .login-info {
    padding: 25px 20px;
  }
  
  .system-title {
    font-size: 1.8rem;
  }
  
  .feature-list {
    gap: 18px;
  }
}

@media (max-width: 1024px) {
  .login-content {
    flex-direction: column;
  }
  
  .login-info {
    flex: none;
    padding: 25px 20px;
    min-height: 40vh;
  }
  
  .login-form-section {
    flex: none;
    background: white;
    min-height: 60vh;
    padding: 20px;
  }
  
  .feature-list {
    flex-direction: row;
    overflow-x: auto;
    padding-bottom: 8px;
    gap: 14px;
  }
  
  .feature-item {
    flex: none;
    min-width: 200px;
  }
}

@media (max-width: 768px) {
  .login-info {
    padding: 20px 15px;
  }
  
  .system-title {
    font-size: 1.6rem;
  }
  
  .login-form-section {
    padding: 20px 15px;
  }
  
  .user-type-tabs {
    flex-direction: column;
    gap: 8px;
  }
  
  .feature-list {
    flex-direction: column;
    gap: 14px;
  }
  
  .feature-item {
    min-width: auto;
  }
}

@media (max-width: 480px) {
  .login-info {
    padding: 15px 10px;
  }
  
  .login-form-section {
    padding: 15px 10px;
  }
  
  .system-title {
    font-size: 1.4rem;
  }
  
  .feature-icon {
    min-width: 40px;
    height: 40px;
    font-size: 1.3rem;
  }
}
</style> 