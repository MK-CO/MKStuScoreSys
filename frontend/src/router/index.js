import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/admin',
      name: 'Admin',
      component: () => import('@/views/admin/AdminLayout.vue'),
      meta: { requiresAuth: true, userType: 1 },
      children: [
        {
          path: '',
          redirect: '/admin/dashboard'
        },
        {
          path: 'dashboard',
          name: 'AdminDashboard',
          component: () => import('@/views/admin/Dashboard.vue')
        },
        {
          path: 'users',
          name: 'UserManagement',
          component: () => import('@/views/admin/UserManagement.vue')
        },
        {
          path: 'courses',
          name: 'CourseManagement',
          component: () => import('@/views/admin/CourseManagement.vue')
        },
        {
          path: 'scores',
          name: 'ScoreManagement',
          component: () => import('@/views/admin/ScoreManagement.vue')
        }
      ]
    },
    {
      path: '/teacher',
      name: 'Teacher',
      component: () => import('@/views/teacher/TeacherLayout.vue'),
      meta: { requiresAuth: true, userType: 2 },
      children: [
        {
          path: '',
          redirect: '/teacher/dashboard'
        },
        {
          path: 'dashboard',
          name: 'TeacherDashboard',
          component: () => import('@/views/teacher/Dashboard.vue')
        },
        {
          path: 'schedule',
          name: 'TeacherSchedule',
          component: () => import('@/views/teacher/Schedule.vue')
        },
        {
          path: 'scores',
          name: 'TeacherScores',
          component: () => import('@/views/teacher/ScoreQuery.vue')
        }
      ]
    },
    {
      path: '/student',
      name: 'Student',
      component: () => import('@/views/student/StudentLayout.vue'),
      meta: { requiresAuth: true, userType: 3 },
      children: [
        {
          path: '',
          redirect: '/student/dashboard'
        },
        {
          path: 'dashboard',
          name: 'StudentDashboard',
          component: () => import('@/views/student/Dashboard.vue')
        },
        {
          path: 'schedule',
          name: 'StudentSchedule',
          component: () => import('@/views/student/Schedule.vue')
        },
        {
          path: 'scores',
          name: 'StudentScores',
          component: () => import('@/views/student/ScoreQuery.vue')
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth) {
    if (!userStore.token) {
      next('/login')
      return
    }
    
    // 检查用户类型权限
    if (to.meta.userType && userStore.userInfo?.userType !== to.meta.userType) {
      // 根据用户类型重定向到对应的首页
      switch (userStore.userInfo?.userType) {
        case 1:
          next('/admin')
          break
        case 2:
          next('/teacher')
          break
        case 3:
          next('/student')
          break
        default:
          next('/login')
      }
      return
    }
  }
  
  next()
})

export default router 