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
          <el-menu-item v-for="item in visibleRoutes" :key="item.path" :index="item.path">
            {{ item.name }}
          </el-menu-item>
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
                <el-dropdown-item>Action 1</el-dropdown-item>
                <el-dropdown-item>Action 2</el-dropdown-item>
                <el-dropdown-item>Action 3</el-dropdown-item>
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

const loginUserStore = useLoginUserStore()
loginUserStore.fetchLoginUser()

// 过滤掉隐藏的菜单项
const visibleRoutes = computed(() => {
  return routes.filter((route) => {
    if (route.meta?.hideInMenu) {
      return false
    }
    // 判断当前用户是否具有访问权限
    return checkAccess(loginUserStore.loginUser, route.meta?.access)
  })
})

const activeIndex = ref('/')
const router = useRouter()
router.afterEach((to) => {
  activeIndex.value = to.path
})
const handleSelect = (key: string) => {
  router.push(key)
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
