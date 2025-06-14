<template>
  <div class="main-layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="sidebarWidth" class="sidebar" :class="{ collapsed: isCollapsed }">
        <!-- Logo区域 -->
        <div class="logo-section">
          <div class="logo-content">
            <div class="logo-icon">
              <svg viewBox="0 0 1024 1024" width="32" height="32">
                <path fill="#409eff" d="M512 85.333333a426.666667 426.666667 0 1 1 0 853.333334 426.666667 426.666667 0 0 1 0-853.333334z m0 85.333334a341.333333 341.333333 0 1 0 0 682.666666 341.333333 341.333333 0 0 0 0-682.666666z m-149.333333 170.666666h298.666666v85.333334H362.666667v-85.333334z m0 170.666667h298.666666v85.333333H362.666667v-85.333333z m0 170.666667h213.333333v85.333333H362.666667v-85.333333z"/>
              </svg>
            </div>
            <div class="logo-text" v-show="!isCollapsed">
              <h3>{{ systemTitle }}</h3>
              <p>{{ userTypeName }}端</p>
            </div>
          </div>
        </div>
        
        <!-- 菜单 -->
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          :collapse="isCollapsed"
          :unique-opened="true"
          @select="handleMenuSelect"
        >
          <template v-for="menu in menuList" :key="menu.name">
            <!-- 有子菜单的情况 -->
            <el-sub-menu v-if="menu.children && menu.children.length > 0" :index="menu.name">
              <template #title>
                <el-icon><component :is="menu.icon" /></el-icon>
                <span>{{ menu.title }}</span>
              </template>
              <el-menu-item 
                v-for="child in menu.children" 
                :key="child.name"
                :index="child.name"
              >
                <el-icon><component :is="child.icon" /></el-icon>
                <span>{{ child.title }}</span>
              </el-menu-item>
            </el-sub-menu>
            
            <!-- 没有子菜单的情况 -->
            <el-menu-item v-else :index="menu.name">
              <el-icon><component :is="menu.icon" /></el-icon>
              <span>{{ menu.title }}</span>
            </el-menu-item>
          </template>
        </el-menu>
      </el-aside>
      
      <!-- 主要内容区域 -->
      <el-container class="main-container">
        <!-- 顶部导航栏 -->
        <el-header class="header">
          <div class="header-left">
            <!-- 折叠按钮 -->
            <el-button 
              text 
              @click="toggleSidebar"
              class="collapse-btn"
            >
              <el-icon size="18">
                <Fold v-if="!isCollapsed" />
                <Expand v-else />
              </el-icon>
            </el-button>
            
            <!-- 面包屑导航 -->
            <el-breadcrumb separator="/" class="breadcrumb">
              <el-breadcrumb-item v-for="item in breadcrumbList" :key="item.name">
                {{ item.title }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          
          <div class="header-right">
            <!-- 全屏按钮 -->
            <el-tooltip content="全屏" placement="bottom">
              <el-button text @click="toggleFullscreen" class="header-btn">
                <el-icon size="18">
                  <FullScreen />
                </el-icon>
              </el-button>
            </el-tooltip>
            
            <!-- 用户信息 -->
            <el-dropdown @command="handleUserCommand" class="user-dropdown">
              <div class="user-info">
                <el-avatar :size="32" :src="userInfo?.avatar">
                  {{ userInfo?.realName?.charAt(0) || userInfo?.username?.charAt(0) }}
                </el-avatar>
                <span class="username" v-show="!isCollapsed">{{ userInfo?.realName || userInfo?.username }}</span>
                <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>
                    个人信息
                  </el-dropdown-item>
                  <el-dropdown-item command="settings">
                    <el-icon><Setting /></el-icon>
                    系统设置
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <!-- 标签页区域 -->
        <div class="tabs-container" v-if="tabsStore.activeTabs.length > 0">
          <el-tabs
            v-model="tabsStore.activeTab"
            type="card"
            class="main-tabs"
            @tab-click="handleTabClick"
            @tab-remove="handleTabRemove"
          >
            <el-tab-pane
              v-for="tab in tabsStore.activeTabs"
              :key="tab.name"
              :label="tab.title"
              :name="tab.name"
              :closable="tab.closable"
            >
              <template #label>
                <span class="tab-label">
                  <el-icon v-if="tab.icon" class="tab-icon">
                    <component :is="tab.icon" />
                  </el-icon>
                  {{ tab.title }}
                </span>
              </template>
            </el-tab-pane>
          </el-tabs>
          
          <!-- 标签页右键菜单 -->
          <el-dropdown
            trigger="contextmenu"
            @command="handleTabCommand"
            class="tab-context-menu"
          >
            <div></div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="refresh">
                  <el-icon><Refresh /></el-icon>
                  刷新页面
                </el-dropdown-item>
                <el-dropdown-item command="closeOthers">
                  <el-icon><Close /></el-icon>
                  关闭其他
                </el-dropdown-item>
                <el-dropdown-item command="closeAll">
                  <el-icon><Close /></el-icon>
                  关闭所有
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        
        <!-- 主要内容区域 -->
        <el-main class="main-content">
          <div class="content-wrapper">
            <router-view v-slot="{ Component, route }">
              <keep-alive>
                <component :is="Component" :key="route.fullPath" />
              </keep-alive>
            </router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Fold,
  Expand,
  FullScreen,
  ArrowDown,
  User,
  Setting,
  SwitchButton,
  Refresh,
  Close,
  Odometer,
  Reading,
  Calendar,
  DataLine,
  TrendCharts,
  UserFilled,
  Management
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { useTabsStore } from '@/stores/tabs'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const tabsStore = useTabsStore()

// 响应式数据
const isCollapsed = ref(false)
const isFullscreen = ref(false)

// 计算属性
const userInfo = computed(() => userStore.userInfo)
const sidebarWidth = computed(() => isCollapsed.value ? '64px' : '250px')
const activeMenu = computed(() => route.name)

// 系统标题
const systemTitle = computed(() => '学生成绩管理系统')

// 用户类型名称
const userTypeName = computed(() => {
  switch (userInfo.value?.userType) {
    case 1: return '管理员'
    case 2: return '教师'
    case 3: return '学生'
    default: return '用户'
  }
})

// 菜单配置
const menuList = computed(() => {
  const userType = userInfo.value?.userType
  
  switch (userType) {
    case 1: // 管理员
      return [
        {
          name: 'AdminDashboard',
          title: '控制台',
          icon: 'Odometer',
          path: '/admin/dashboard'
        },
        {
          name: 'CourseInfoManagement',
          title: '课程信息管理',
          icon: 'Reading',
          path: '/admin/course-info'
        },
        {
          name: 'ScheduleManagement',
          title: '课程表管理',
          icon: 'Calendar',
          path: '/admin/schedule'
        },
        {
          name: 'ScoreInfoManagement',
          title: '成绩信息管理',
          icon: 'DataLine',
          path: '/admin/score-info'
        },
        {
          name: 'ScoreStatistics',
          title: '成绩统计分析',
          icon: 'TrendCharts',
          path: '/admin/score-statistics'
        },
        {
          name: 'UserManagement',
          title: '用户管理',
          icon: 'UserFilled',
          path: '/admin/users'
        },
        {
          name: 'AccountManagement',
          title: '账号管理',
          icon: 'Management',
          path: '/admin/accounts'
        }
      ]
    case 2: // 教师
      return [
        {
          name: 'TeacherDashboard',
          title: '工作台',
          icon: 'Odometer',
          path: '/teacher/dashboard'
        },
        {
          name: 'TeacherSchedule',
          title: '课程安排',
          icon: 'Calendar',
          path: '/teacher/schedule'
        },
        {
          name: 'TeacherScores',
          title: '成绩管理',
          icon: 'DataLine',
          path: '/teacher/scores'
        }
      ]
    case 3: // 学生
      return [
        {
          name: 'StudentDashboard',
          title: '个人中心',
          icon: 'Odometer',
          path: '/student/dashboard'
        },
        {
          name: 'StudentSchedule',
          title: '我的课程表',
          icon: 'Calendar',
          path: '/student/schedule'
        },
        {
          name: 'StudentScores',
          title: '成绩查询',
          icon: 'DataLine',
          path: '/student/scores'
        }
      ]
    default:
      return []
  }
})

// 面包屑导航
const breadcrumbList = computed(() => {
  const currentMenu = findMenuByName(route.name, menuList.value)
  if (currentMenu) {
    return [
      { name: 'home', title: '首页' },
      { name: currentMenu.name, title: currentMenu.title }
    ]
  }
  return [{ name: 'home', title: '首页' }]
})

// 方法
const findMenuByName = (name, menus) => {
  for (const menu of menus) {
    if (menu.name === name) {
      return menu
    }
    if (menu.children) {
      const found = findMenuByName(name, menu.children)
      if (found) return found
    }
  }
  return null
}

// 切换侧边栏
const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}

// 切换全屏
const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
    isFullscreen.value = true
  } else {
    document.exitFullscreen()
    isFullscreen.value = false
  }
}

// 处理菜单选择
const handleMenuSelect = (menuName) => {
  const menu = findMenuByName(menuName, menuList.value)
  if (menu) {
    // 添加到标签页
    tabsStore.addTab({
      name: menu.name,
      title: menu.title,
      path: menu.path,
      icon: menu.icon
    })
    
    // 路由跳转
    if (route.name !== menu.name) {
      router.push(menu.path)
    }
  }
}

// 处理标签页点击
const handleTabClick = (tab) => {
  const tabData = tabsStore.activeTabs.find(t => t.name === tab.paneName)
  if (tabData && route.path !== tabData.path) {
    router.push(tabData.path)
  }
}

// 处理标签页移除
const handleTabRemove = (tabName) => {
  const nextPath = tabsStore.removeTab(tabName)
  if (nextPath && route.path !== nextPath) {
    router.push(nextPath)
  } else if (tabsStore.activeTabs.length === 0) {
    // 如果没有标签页了，跳转到默认页面
    const defaultMenu = menuList.value[0]
    if (defaultMenu) {
      handleMenuSelect(defaultMenu.name)
    }
  }
}

// 处理标签页右键菜单
const handleTabCommand = (command) => {
  const currentTabName = tabsStore.activeTab
  
  switch (command) {
    case 'refresh':
      // 刷新当前页面
      window.location.reload()
      break
    case 'closeOthers':
      tabsStore.closeOtherTabs(currentTabName)
      break
    case 'closeAll':
      tabsStore.closeAllTabs()
      // 跳转到默认页面
      const defaultMenu = menuList.value[0]
      if (defaultMenu) {
        handleMenuSelect(defaultMenu.name)
      }
      break
  }
}

// 处理用户下拉菜单
const handleUserCommand = async (command) => {
  switch (command) {
    case 'profile':
      ElMessage.info('个人信息功能开发中...')
      break
    case 'settings':
      ElMessage.info('系统设置功能开发中...')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        userStore.logout()
        tabsStore.closeAllTabs()
        ElMessage.success('退出登录成功')
        router.push('/login')
      } catch {
        // 用户取消操作
      }
      break
  }
}

// 监听路由变化，自动添加标签页
watch(
  () => route.name,
  (newRouteName) => {
    if (newRouteName) {
      const menu = findMenuByName(newRouteName, menuList.value)
      if (menu) {
        tabsStore.addTab({
          name: menu.name,
          title: menu.title,
          path: menu.path,
          icon: menu.icon
        })
      }
    }
  },
  { immediate: true }
)

// 组件挂载时初始化
onMounted(() => {
  // 检查用户类型
  if (!userInfo.value?.userType) {
    ElMessage.error('用户类型未知，请重新登录')
    userStore.logout()
    router.push('/login')
    return
  }
  
  // 初始化默认标签页
  const defaultMenu = menuList.value[0]
  if (defaultMenu) {
    tabsStore.initDefaultTab({
      name: defaultMenu.name,
      title: defaultMenu.title,
      path: defaultMenu.path,
      icon: defaultMenu.icon,
      closable: false // 默认标签页不可关闭
    })
  }
})
</script>

<style scoped>
.main-layout {
  height: 100vh;
  overflow: hidden;
}

/* 侧边栏样式 */
.sidebar {
  background: #304156;
  transition: width 0.3s;
  overflow: hidden;
}

.sidebar.collapsed {
  width: 64px !important;
}

.logo-section {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #404854;
  padding: 0 16px;
}

.logo-content {
  display: flex;
  align-items: center;
  gap: 12px;
  color: white;
}

.logo-text h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.logo-text p {
  margin: 0;
  font-size: 12px;
  color: #909399;
}

.sidebar-menu {
  border: none;
  background: #304156;
  height: calc(100vh - 64px);
  overflow-y: auto;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 250px;
}

:deep(.sidebar-menu .el-menu-item) {
  color: #bfcbd9;
  height: 48px;
  line-height: 48px;
}

:deep(.sidebar-menu .el-menu-item:hover) {
  background-color: #4a5568;
  color: #fff;
}

:deep(.sidebar-menu .el-menu-item.is-active) {
  background-color: #409eff;
  color: #fff;
}

:deep(.sidebar-menu .el-sub-menu__title) {
  color: #bfcbd9;
  height: 48px;
  line-height: 48px;
}

:deep(.sidebar-menu .el-sub-menu__title:hover) {
  background-color: #4a5568;
  color: #fff;
}

/* 主容器样式 */
.main-container {
  background: #f0f2f5;
}

/* 顶部导航栏样式 */
.header {
  background: white;
  border-bottom: 1px solid #e8e8e8;
  padding: 0 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  padding: 8px;
}

.breadcrumb {
  font-size: 14px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-btn {
  padding: 8px;
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f5f5;
}

.username {
  font-size: 14px;
  color: #333;
}

.dropdown-icon {
  font-size: 12px;
  color: #999;
}

/* 标签页样式 */
.tabs-container {
  background: white;
  border-bottom: 1px solid #e8e8e8;
  padding: 0 16px;
}

.main-tabs {
  height: 40px;
}

:deep(.main-tabs .el-tabs__header) {
  margin: 0;
  border-bottom: none;
}

:deep(.main-tabs .el-tabs__nav) {
  border: none;
}

:deep(.main-tabs .el-tabs__item) {
  height: 32px;
  line-height: 32px;
  border: 1px solid #d9d9d9;
  border-radius: 4px 4px 0 0;
  margin-right: 4px;
  padding: 0 12px;
  background: #fafafa;
  color: #666;
  font-size: 12px;
}

:deep(.main-tabs .el-tabs__item.is-active) {
  background: #409eff;
  color: white;
  border-color: #409eff;
}

:deep(.main-tabs .el-tabs__item:hover) {
  color: #409eff;
}

.tab-label {
  display: flex;
  align-items: center;
  gap: 4px;
}

.tab-icon {
  font-size: 12px;
}

/* 主要内容区域样式 */
.main-content {
  padding: 16px;
  overflow-y: auto;
}

.content-wrapper {
  background: white;
  border-radius: 4px;
  min-height: calc(100vh - 144px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    z-index: 1000;
    height: 100vh;
  }
  
  .main-container {
    margin-left: 0;
  }
  
  .username {
    display: none;
  }
}
</style> 