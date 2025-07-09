import AccessEnum from '@/access/accessEnum.ts'

/**
 * 检测权限 (判断当前登录用户是否有权限访问当前页面)
 * @param loginUser   当前登录用户信息
 * @param needAccess  需要的权限，默认为 NOT_LOGGED_IN
 * @returns {boolean} 是否有权限
 */
const checkAccess = (
  loginUser: any,
  needAccess: string = AccessEnum.NOT_LOGGED_IN,
): boolean => {
  const userRole = loginUser?.userRole ?? AccessEnum.NOT_LOGGED_IN

  switch (needAccess) {
    // 不需要登录
    case AccessEnum.NOT_LOGGED_IN:
      return true

    // 需要登录
    case AccessEnum.USER:
      return userRole !== AccessEnum.NOT_LOGGED_IN

    // 需要管理员权限
    case AccessEnum.ADMIN:
      return userRole === AccessEnum.ADMIN

    default:
      return false // 默认无权限
  }
}

export default checkAccess
