<template>
  <div class="admin-layout">
    <el-card>
      <template #header>
        <span>管理员端</span>
      </template>
      <div class="placeholder">
        <el-empty description="管理员功能开发中..." />
        <p>当前用户：{{ userStore.userInfo?.realName }}</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
</script>

<style scoped>
.placeholder {
  padding: 50px 0;
  text-align: center;
}
</style> 