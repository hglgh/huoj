import { defineStore } from 'pinia'
import { ref } from 'vue'
import ACCESS_ENUM from '@/access/accessEnum.ts'

export const useLoginUserStore = defineStore('loginUser', () => {
  const loginUser = ref({
    userName: '未登录',
    id: 0,
    userRole: ACCESS_ENUM.NOT_LOGGED_IN,
  })

  async function fetchLoginUser() {
    // 测试用户登录， 3s后自动登录
    setTimeout(() => {
      loginUser.value = {
        userName: '测试用户1213123123132213123123',
        id: 1,
        userRole: 'admin',
      }
    }, 3000)
  }

  function setLoginUser(userName: string) {
    loginUser.value.userName = userName
  }

  return { loginUser, fetchLoginUser, setLoginUser }
})
