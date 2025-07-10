import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getLoginUser } from '@/api/userController.ts'
import AccessEnum from '@/access/accessEnum.ts'
import '@/api/typings.d.ts'

export const useLoginUserStore = defineStore('loginUser', () => {
  const loginUser = ref<API.LoginUserVO>({
    userRole: AccessEnum.NOT_LOGGED_IN,
  })

  async function fetchLoginUser() {
    const response = await getLoginUser()
    if (response.data.code === 0 && response.data.data) {
      loginUser.value = response.data.data
    }
  }

  function setLoginUser(newLoginUser: API.LoginUserVO) {
    loginUser.value = newLoginUser
  }

  return { loginUser, fetchLoginUser, setLoginUser }
})
