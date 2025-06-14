import request from '@/utils/request'

/**
 * 用户登录
 * @param {Object} data 登录表单数据
 */
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

/**
 * 用户登出
 */
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

/**
 * 测试接口连通性
 */
export function testConnection() {
  return request({
    url: '/auth/test',
    method: 'get'
  })
} 