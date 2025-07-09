import router from '@/router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import ACCESS_ENUM from '@/access/accessEnum.ts'

router.beforeEach((to, from, next) => {
  const loginUserStore = useLoginUserStore()
  // 判断权限,仅管理员可见
  if (to.meta?.access === ACCESS_ENUM.ADMIN) {
    if (loginUserStore.loginUser?.userRole !== ACCESS_ENUM.ADMIN) {
      return next('/noAuth')
    }
    return next()
  }
  next()
})
