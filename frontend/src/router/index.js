import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/Home.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/admin',
      component: () => import('@/views/MainLayout.vue'),
      meta: { requiresAuth: true, userType: 1 },
      children: [
        {
          path: '',
          redirect: '/admin/dashboard'
        },
        {
          path: 'dashboard',
          name: 'AdminDashboard',
          component: () => import('@/views/admin/AdminDashboard.vue')
        },
        {
          path: 'course-info',
          name: 'CourseInfoManagement',
          component: () => import('@/views/admin/CourseInfoManagement.vue')
        },
        {
          path: 'schedule',
          name: 'ScheduleManagement',
          component: () => import('@/views/admin/ScheduleManagement.vue')
        },
        {
          path: 'score-info',
          name: 'ScoreInfoManagement',
          component: () => import('@/views/admin/ScoreInfoManagement.vue')
        },
        {
          path: 'score-statistics',
          name: 'ScoreStatistics',
          component: () => import('@/views/admin/ScoreStatistics.vue')
        },
        {
          path: 'users',
          name: 'UserManagement',
          component: () => import('@/views/admin/UserManagement.vue')
        },
        {
          path: 'accounts',
          name: 'AccountManagement',
          component: () => import('@/views/admin/AccountManagement.vue')
        }
      ]
    },
    {
      path: '/teacher',
      component: () => import('@/views/MainLayout.vue'),
      meta: { requiresAuth: true, userType: 2 },
      children: [
        {
          path: '',
          redirect: '/teacher/dashboard'
        },
        {
          path: 'dashboard',
          name: 'TeacherDashboard',
          component: () => import('@/views/teacher/TeacherDashboard.vue')
        },
        {
          path: 'schedule',
          name: 'TeacherSchedule',
          component: () => import('@/views/teacher/TeacherSchedule.vue')
        },
        {
          path: 'scores',
          name: 'TeacherScores',
          component: () => import('@/views/teacher/TeacherScores.vue')
        }
      ]
    },
    {
      path: '/student',
      component: () => import('@/views/MainLayout.vue'),
      meta: { requiresAuth: true, userType: 3 },
      children: [
        {
          path: '',
          redirect: '/student/dashboard'
        },
        {
          path: 'dashboard',
          name: 'StudentDashboard',
          component: () => import('@/views/student/StudentDashboard.vue')
        },
        {
          path: 'schedule',
          name: 'StudentSchedule',
          component: () => import('@/views/student/StudentSchedule.vue')
        },
        {
          path: 'scores',
          name: 'StudentScores',
          component: () => import('@/views/student/StudentScores.vue')
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  console.log('路由守卫检查:', {
    to: to.path,
    token: userStore.token,
    userInfo: userStore.userInfo
  })
  
  if (to.meta.requiresAuth) {
    if (!userStore.token) {
      ElMessage.warning('请先登录')
      next('/login')
      return
    }
    
    // 检查用户信息是否完整
    if (!userStore.userInfo || typeof userStore.userInfo.userType === 'undefined' || userStore.userInfo.userType === null) {
      console.error('用户信息检查失败:', {
        userInfo: userStore.userInfo,
        userType: userStore.userInfo?.userType
      })
      ElMessage.error('用户信息不完整，请重新登录')
      userStore.logout()
      next('/login')
      return
    }
    
    // 检查用户类型权限
    if (to.meta.userType && userStore.userInfo.userType !== to.meta.userType) {
      ElMessage.warning('您没有权限访问该页面')
      // 根据用户类型重定向到对应的首页
      switch (userStore.userInfo.userType) {
        case 1:
          next('/admin/dashboard')
          break
        case 2:
          next('/teacher/dashboard')
          break
        case 3:
          next('/student/dashboard')
          break
        default:
          ElMessage.error('用户类型未知，请重新登录')
          userStore.logout()
          next('/login')
      }
      return
    }
  }
  
  next()
})

export default router 