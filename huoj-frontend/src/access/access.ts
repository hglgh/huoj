import router from '@/router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import ACCESS_ENUM from '@/access/accessEnum.ts'
import checkAccess from '@/access/checkAccess.ts'

//是否为首次获取登录用户信息
let isFirstGetLoginUser = true

router.beforeEach(async (to, from, next) => {
  const loginUserStore = useLoginUserStore()
  let loginUser = loginUserStore.loginUser
  //确保页面刷新时，首次加载时，能等待后端返回登录用户信息在校验权限
  if (isFirstGetLoginUser) {
    await loginUserStore.fetchLoginUser()
    loginUser = loginUserStore.loginUser
    isFirstGetLoginUser = false
  }
  // 判断权限,仅管理员可见
  const needAccess = to.meta?.access ?? ACCESS_ENUM.NOT_LOGGED_IN
  const result = checkAccess(loginUser, needAccess as string)
  if (!result) {
    if (loginUser.userRole === ACCESS_ENUM.NOT_LOGGED_IN) {
      return next(`user/login?redirect=${to.fullPath}`)
    }
    return next('/noAuth')
  }
  next()
})
