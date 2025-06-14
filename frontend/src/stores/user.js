import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import Cookies from 'js-cookie'
import { login as loginApi, logout as logoutApi } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(Cookies.get('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const userTypeName = computed(() => {
    if (!userInfo.value) return ''
    switch (userInfo.value.userType) {
      case 1: return '管理员'
      case 2: return '教师'
      case 3: return '学生'
      default: return '未知'
    }
  })

  // 登录
  const login = async (loginForm) => {
    try {
      const response = await loginApi(loginForm)
      if (response.code === 200) {
        const { token: newToken, tokenHead, ...userData } = response.data
        
        // 保存token
        token.value = newToken
        Cookies.set('token', newToken, { expires: 7 }) // 7天过期
        
        // 保存用户信息
        userInfo.value = userData
        localStorage.setItem('userInfo', JSON.stringify(userData))
        
        return { success: true, data: response.data }
      } else {
        return { success: false, message: response.message }
      }
    } catch (error) {
      console.error('登录失败:', error)
      return { success: false, message: error.message || '登录失败' }
    }
  }

  // 登出
  const logout = async () => {
    try {
      await logoutApi()
    } catch (error) {
      console.error('登出请求失败:', error)
    } finally {
      // 清除本地存储
      token.value = ''
      userInfo.value = null
      Cookies.remove('token')
      localStorage.removeItem('userInfo')
    }
  }

  // 更新用户信息
  const updateUserInfo = (newUserInfo) => {
    userInfo.value = { ...userInfo.value, ...newUserInfo }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    userTypeName,
    login,
    logout,
    updateUserInfo
  }
}) 