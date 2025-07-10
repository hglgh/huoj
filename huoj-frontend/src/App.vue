<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

/**
 * todo 全局初始化函数，有些初始化逻辑，如获取用户信息，初始化权限等需放在这里
 */
const doInit = () => {
  console.log('doInit')
}
onMounted(() => {
  doInit()
})
</script>

<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
      <router-view />
    </template>
    <template v-else>
      <BasicLayout />
    </template>
  </div>
</template>

<style scoped></style>
