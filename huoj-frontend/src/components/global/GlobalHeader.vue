<template>
  <div id="globalHeader">
    <el-row :gutter="24" align="middle" justify="space-between">
      <el-col :span="21">
        <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
        >
          <router-link to="/" style="text-decoration: none">
            <div class="title-bar">
              <el-avatar
                class="logo"
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                alt="logo"
              />
              <div class="title">HUOJ</div>
            </div>
          </router-link>
          <!-- 动态生成菜单项 -->
          <template v-for="item in visibleRoutes" :key="item.path">
            <!-- 如果有子路由，则使用 el-sub-menu -->
            <el-sub-menu v-if="item.children && item.children.length" :index="item.path">
              <template #title>{{ item.name }}</template>
              <el-menu-item v-for="child in item.children" :key="child.path" :index="child.path">
                {{ child.name }}
              </el-menu-item>
            </el-sub-menu>
            <!-- 否则使用 el-menu-item -->
            <el-menu-item v-else :index="item.path">{{ item.name }}</el-menu-item>
          </template>
        </el-menu>
      </el-col>
      <el-col :span="3">
        <div v-if="loginUserStore.loginUser.id">
          <el-dropdown trigger="click">
            <el-space size="large" style="cursor: pointer">
              <el-avatar
                class="logo"
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                alt="logo"
              />
              <el-tooltip :content="loginUserStore.loginUser.userName">
                <span class="username">{{ loginUserStore.loginUser.userName }}</span>
              </el-tooltip>
            </el-space>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="doLogout">注销 </el-dropdown-item>
                <el-dropdown-item disabled>Action 4</el-dropdown-item>
                <el-dropdown-item divided>Action 5</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div v-else style="display: flex; justify-content: center">
          <el-button type="primary" size="large">登录</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'
import { routes } from '@/router/routes.ts'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import checkAccess from '@/access/checkAccess.ts'
import { userLogout } from '@/api/userController.ts'

const loginUserStore = useLoginUserStore()

/*// 过滤掉隐藏的菜单项
const visibleRoutes = computed(() => {
  return routes.filter((route) => {
    if (route.meta?.hideInMenu) {
      return false
    }
    // 判断当前用户是否具有访问权限
    return checkAccess(loginUserStore.loginUser, route.meta?.access)
  })
})*/
// 过滤掉隐藏的菜单项，并递归处理子路由
const visibleRoutes = computed(() => {
  const filterRoute = (route) => {
    // 如果当前路由被标记为隐藏
    if (route.meta?.hideInMenu) {
      return false
    }

    // 检查权限
    return !(route.meta?.access && !checkAccess(loginUserStore.loginUser, route.meta.access))
  }

  const processRoutes = (routes) => {
    return routes.reduce((acc, route) => {
      // 处理当前路由是否可见
      const hasAccess = filterRoute(route)

      // 如果有子路由，递归处理子路由
      let children = []
      if (route.children?.length) {
        children = processRoutes(route.children)
      }

      // 如果自己可见 或者 子路由中存在可见项，则保留该路由
      if (hasAccess || children.length > 0) {
        acc.push({
          ...route,
          children: children.length > 0 ? children : undefined,
        })
      }

      return acc
    }, [])
  }

  return processRoutes(routes)
})

const activeIndex = ref('/')
const router = useRouter()
router.afterEach((to) => {
  activeIndex.value = to.path
})
const handleSelect = (key: string) => {
  router.push(key)
}
const doLogout = () => {
  userLogout()
  router.push('/user/login')
}
</script>
<style scoped>
#globalHeader .title-bar {
  display: flex;
  margin-right: 16px;
  align-items: center;
}

.title {
  color: black;
  font-size: 20px;
  margin-left: 16px;
}

.logo {
  height: 50px;
}

.username {
  white-space: nowrap; /* 防止文本换行 */
  overflow: hidden; /* 隐藏溢出部分 */
  text-overflow: ellipsis; /* 添加省略号 */
  max-width: 100px; /* 根据实际需求设置最大宽度 */
}
</style>
