import HomeView from '@/views/HomeView.vue'
import ACCESS_ENUM from '@/access/accessEnum.ts'

export const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
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
