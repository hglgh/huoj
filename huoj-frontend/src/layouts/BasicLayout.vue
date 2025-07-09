<template>
  <div id="basicLayout">
    <el-container>
      <el-header class="header">
        <GlobalHeader />
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
      <el-footer class="footer">
        <el-link href="https://github.com/hglgh" target="_blank"> by 程序员: 请别把我整破防 </el-link>
      </el-footer>
    </el-container>
  </div>
</template>
<script lang="ts" setup>
import GlobalHeader from '@/components/global/GlobalHeader.vue'
</script>
<style scoped>
#basicLayout .header{
  margin-bottom: 16px;
  box-shadow: #eee 1px 1px 5px;
}
#basicLayout .main{
  max-width: 1400px;
  margin: 0 auto 28px;
  padding: 20px;
  box-sizing: border-box;
  background: linear-gradient(to right, #fefefe, #ffff);
}
#basicLayout .footer{
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  background: #efefef;
  text-align: center;
}
</style>
