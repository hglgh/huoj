<template>
  <div id="userLogin">
    <el-form :model="userLoginForm" v-model:rules="rules" ref="ruleFormRef">
      <el-form-item label="账号" prop="userAccount">
        <el-input v-model="userLoginForm.userAccount" placeholder="请输入账号" />
      </el-form-item>
      <el-form-item label="密码" prop="userPassword">
        <el-input type="password" v-model="userLoginForm.userPassword" placeholder="请输入密码" />
      </el-form-item>
      <el-form-item label="验证码" prop="captcha" size="large">
        <el-space>
          <el-input v-model="userLoginForm.captcha" placeholder="请输入验证码" />
          <img :src="captcha?.captchaImage" style="width: 128px; height: 48px" alt="captcha" @click="getCaptchaVO" />
        </el-space>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doRegister(ruleFormRef)">登录</el-button>
        <el-button @click="goRegister">尚未账号？去注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { getCaptcha, userLogin } from '@/api/userController.ts'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'

const router = useRouter()
const ruleFormRef = ref<FormInstance>() // 获取 el-form 实例

// 定义表单数据模型
const userLoginForm = reactive<API.UserLoginRequest>({})
const captcha = ref<API.CaptchaVO>()
const loginUserStore = useLoginUserStore()

// 定义表单校验规则
const rules = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 4, max: 16, message: '账号长度应在4到16位之间', trigger: 'blur' },
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, message: '密码长度不能小于8位', trigger: 'blur' },
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { min: 4, max: 4, message: '验证码长度为4位', trigger: 'blur' },
  ],
}

const getCaptchaVO = async () => {
  const res = await getCaptcha()
  if (res.data.code === 0 && res.data.data) {
    captcha.value = res.data.data
    userLoginForm.captchaKey = res.data.data.captchaKey
  } else {
    ElMessage.error('获取验证码失败:' + res.data.message)
  }
}
onMounted(() => {
  getCaptchaVO()
})

// 注册逻辑
const doRegister = async (formEl: FormInstance | undefined) => {
  if (!formEl) return

  try {
    await formEl.validate()

    const res = await userLogin(userLoginForm)
    if (res.data.code === 0 && res.data.data) {
      ElMessage.success('登录成功')
      loginUserStore.setLoginUser(res.data.data)
      await router.push({
        path: '/',
        replace: true,
      })
    } else {
      ElMessage.error('登录失败:' + res.data.message)
    }
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
  } catch (error) {
    ElMessage.error('校验失败,请确认输入')
  }
}
// ... existing code ...

// 跳转注册
const goRegister = () => {
  router.push('/user/register')
}
</script>

<style></style>
