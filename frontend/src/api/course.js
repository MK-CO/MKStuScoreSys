import request from '@/utils/request'

/**
 * 分页查询课程列表
 * @param {Object} params 查询参数
 */
export function getCourseList(params) {
  return request({
    url: '/courses/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询课程详情
 * @param {Number} id 课程ID
 */
export function getCourseById(id) {
  return request({
    url: `/courses/${id}`,
    method: 'get'
  })
}

/**
 * 根据课程编码查询课程
 * @param {String} courseCode 课程编码
 */
export function getCourseByCourseCode(courseCode) {
  return request({
    url: `/courses/code/${courseCode}`,
    method: 'get'
  })
}

/**
 * 创建课程
 * @param {Object} data 课程数据
 */
export function createCourse(data) {
  return request({
    url: '/courses',
    method: 'post',
    data
  })
}

/**
 * 更新课程信息
 * @param {Number} id 课程ID
 * @param {Object} data 课程数据
 */
export function updateCourse(id, data) {
  return request({
    url: `/courses/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除课程
 * @param {Number} id 课程ID
 */
export function deleteCourse(id) {
  return request({
    url: `/courses/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除课程
 * @param {Array} ids 课程ID数组
 */
export function deleteCourses(ids) {
  return request({
    url: '/courses/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 检查课程编码是否存在
 * @param {String} courseCode 课程编码
 */
export function checkCourseCode(courseCode) {
  return request({
    url: `/courses/check-code/${courseCode}`,
    method: 'get'
  })
}

/**
 * 获取所有课程列表（不分页）
 */
export function getAllCourses() {
  return request({
    url: '/courses/all',
    method: 'get'
  })
} 