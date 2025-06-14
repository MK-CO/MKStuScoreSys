import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useTabsStore = defineStore('tabs', () => {
  // 状态
  const tabs = ref([])
  const activeTab = ref('')

  // 计算属性
  const activeTabs = computed(() => tabs.value)
  const currentTab = computed(() => tabs.value.find(tab => tab.name === activeTab.value))

  // 添加标签页
  const addTab = (tab) => {
    const existingTab = tabs.value.find(t => t.name === tab.name)
    if (!existingTab) {
      tabs.value.push({
        name: tab.name,
        title: tab.title,
        path: tab.path,
        closable: tab.closable !== false, // 默认可关闭
        icon: tab.icon || null
      })
    }
    activeTab.value = tab.name
  }

  // 移除标签页
  const removeTab = (tabName) => {
    const index = tabs.value.findIndex(tab => tab.name === tabName)
    if (index > -1) {
      const removedTab = tabs.value[index]
      tabs.value.splice(index, 1)
      
      // 如果移除的是当前激活的标签页，需要切换到其他标签页
      if (activeTab.value === tabName && tabs.value.length > 0) {
        // 优先选择右边的标签页，如果没有则选择左边的
        const nextTab = tabs.value[index] || tabs.value[index - 1]
        if (nextTab) {
          activeTab.value = nextTab.name
          return nextTab.path
        }
      }
    }
    return null
  }

  // 设置激活的标签页
  const setActiveTab = (tabName) => {
    const tab = tabs.value.find(t => t.name === tabName)
    if (tab) {
      activeTab.value = tabName
      return tab.path
    }
    return null
  }

  // 关闭其他标签页
  const closeOtherTabs = (tabName) => {
    const currentTab = tabs.value.find(tab => tab.name === tabName)
    if (currentTab) {
      tabs.value = [currentTab]
      activeTab.value = tabName
    }
  }

  // 关闭所有标签页
  const closeAllTabs = () => {
    tabs.value = []
    activeTab.value = ''
  }

  // 关闭右侧标签页
  const closeRightTabs = (tabName) => {
    const index = tabs.value.findIndex(tab => tab.name === tabName)
    if (index > -1) {
      tabs.value = tabs.value.slice(0, index + 1)
      // 如果当前激活的标签页被关闭了，切换到指定的标签页
      if (!tabs.value.find(tab => tab.name === activeTab.value)) {
        activeTab.value = tabName
      }
    }
  }

  // 初始化默认标签页
  const initDefaultTab = (defaultTab) => {
    if (tabs.value.length === 0) {
      addTab(defaultTab)
    }
  }

  return {
    tabs,
    activeTab,
    activeTabs,
    currentTab,
    addTab,
    removeTab,
    setActiveTab,
    closeOtherTabs,
    closeAllTabs,
    closeRightTabs,
    initDefaultTab
  }
}) 