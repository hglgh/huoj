<template>
  <div id="userLayout">
    <el-container>
      <el-header class="header">
        <el-space>
          <img
            style="width: 48px;"
            src="https://gw.alipayobjects.com/zos/rmsportal/KDpgvguMpGfqaHPjicRK.svg"
            alt="logo"
          />
          <div>HUOJ</div>
        </el-space>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
      <el-footer class="footer">
        <el-link href="https://github.com/hglgh" target="_blank">
          by 程序员: 请别把我整破防
        </el-link>
      </el-footer>
    </el-container>
  </div>
</template>
<script lang="ts" setup></script>
<style scoped>
#userLayout {
text-align: center;
}
#userLayout .header {
  margin-bottom: 16px;
  box-shadow: #eee 1px 1px 5px;
}

#userLayout .main {
  max-width: 1400px;
  margin: 0 auto 28px;
  padding: 20px;
  box-sizing: border-box;
  background: linear-gradient(to right, #fefefe, #ffff);
}

#userLayout .footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  background: #efefef;
  text-align: center;
}
</style>
