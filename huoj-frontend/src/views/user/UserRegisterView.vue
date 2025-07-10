<template>
  <div id="userRegister">
    <el-form :model="userRegisterForm" v-model:rules="rules" ref="ruleFormRef">
      <el-form-item label="账号" prop="userAccount">
        <el-input v-model="userRegisterForm.userAccount" placeholder="请输入账号" />
      </el-form-item>
      <el-form-item label="密码" prop="userPassword">
        <el-input
          type="password"
          v-model="userRegisterForm.userPassword"
          placeholder="请输入密码"
        />
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPassword">
        <el-input
          type="password"
          v-model="userRegisterForm.checkPassword"
          placeholder="请输入确认密码"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doRegister(ruleFormRef)">注册</el-button>
        <el-button @click="goLogin">已有账号？去登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { userRegister } from '@/api/userController.ts'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
const ruleFormRef = ref<FormInstance>() // 获取 el-form 实例

// 定义表单数据模型
const userRegisterForm = reactive<API.UserRegisterRequest>({})

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
  checkPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule: never, value: string, callback: any) => {
        if (value !== userRegisterForm.userPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
}

// 注册逻辑
const doRegister = async (formEl: FormInstance | undefined) => {
  if (!formEl) return

  try {
    await formEl.validate()

    const res = await userRegister(userRegisterForm)
    if (res.data.code === 0 && res.data.data) {
      ElMessage.success('注册成功')
      goLogin()
    } else {
      ElMessage.error('注册失败:' + res.data.message)
    }
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
  } catch (error) {
    ElMessage.error('校验失败,请确认输入')
  }
}
// ... existing code ...

// 跳转登录
const goLogin = () => {
  router.push('/user/login')
}
</script>

<style></style>
