import HomeView from '@/views/HomeView.vue'
import ACCESS_ENUM from '@/access/accessEnum.ts'
import UserLayout from '@/layouts/UserLayout.vue'

export const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
  },
  {
    path: '/user',
    name: '用户',
    component: UserLayout,
    meta: {
      hideInMenu: true,
    },
    children: [
      {
        path: '/user/register',
        name: '用户注册',
        component: () => import('@/views/user/UserRegisterView.vue'),
        meta: {
          hideInMenu: true,
        },
      },
      {
        path: '/user/login',
        name: '用户登录',
        component: () => import('@/views/user/UserLoginView.vue'),
        meta: {
          hideInMenu: true,
        },
      },
    ],
  },
  {
    path: '/test',
    name: 'test',
    component: () => import('@/views/TestView.vue'),
    meta: {
      access: ACCESS_ENUM.ADMIN,
    },
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (About.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import('../views/AboutView.vue'),
  },
  {
    path: '/noAuth',
    name: 'noAuth',
    component: () => import('../views/NoAuthView.vue'),
    meta: {
      hideInMenu: true,
    },
  },
]
