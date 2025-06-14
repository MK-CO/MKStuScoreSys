<template>
  <div class="student-schedule">
    <h2>我的课程表</h2>
    <el-calendar v-model="selectedDate">
      <template #date-cell="{ data }">
        <div class="calendar-cell">
          <p>{{ data.day.split('-').slice(2).join('-') }}</p>
          <div v-if="getScheduleForDate(data.day).length > 0" class="schedule-items">
            <div 
              v-for="schedule in getScheduleForDate(data.day)" 
              :key="schedule.id"
              class="schedule-item"
            >
              {{ schedule.courseName }} {{ schedule.time }}
            </div>
          </div>
        </div>
      </template>
    </el-calendar>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const selectedDate = ref(new Date())
const schedules = ref([
  {
    id: 1,
    courseName: '高等数学',
    date: '2024-01-15',
    time: '08:00-10:00',
    classroom: 'A101',
    teacher: '张教授'
  },
  {
    id: 2,
    courseName: '线性代数',
    date: '2024-01-16',
    time: '14:00-16:00',
    classroom: 'B202',
    teacher: '李教授'
  },
  {
    id: 3,
    courseName: '概率论',
    date: '2024-01-17',
    time: '10:00-12:00',
    classroom: 'C303',
    teacher: '王教授'
  }
])

const getScheduleForDate = (date) => {
  return schedules.value.filter(schedule => schedule.date === date)
}
</script>

<style scoped>
.student-schedule {
  padding: 20px;
}

.calendar-cell {
  height: 100px;
  overflow: hidden;
}

.schedule-items {
  margin-top: 5px;
}

.schedule-item {
  font-size: 12px;
  background-color: #67C23A;
  color: white;
  padding: 2px 4px;
  margin: 1px 0;
  border-radius: 2px;
}
</style> 