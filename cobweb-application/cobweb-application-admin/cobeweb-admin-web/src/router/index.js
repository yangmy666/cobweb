import { createRouter, createWebHashHistory } from "vue-router"

//固定路由
const routes = [
  {
    path: '/',
    component: ()=>import('@/pages/Home.vue'),
    children:[
      {
        path: '/cluster',
        component: ()=>import('@/pages/cluster')
      },
      {
        path: '/node',
        component: ()=>import('@/pages/cluster/node')
      },
      {
        path: "/release",
        component: ()=>import('@/pages/release')
      },
      {
        path: "/container",
        component: ()=>import('@/pages/container')
      },
      {
        path: "/release/editForm",
        component: ()=>import('@/pages/release/EditForm')
      },
      {
        path: "/test",
        component: ()=>import('@/pages/test')
      }
    ]
  },


]

const index = createRouter({
  history: createWebHashHistory(),
  routes: routes,
})

export default index
