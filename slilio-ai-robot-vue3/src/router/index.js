import { createRouter, createWebHashHistory } from "vue-router";

//统一声明路由
const routes = [
  {
    path: "/", //路由地址
    component: () => import("@/views/Index.vue"), //对应组件
    meta: {
      //meta信息
      title: "AI 机器人首页", //页面标题
    },
  },
];

//创建路由
const router = createRouter({
  //指定路由模式，hash模式指的是url的路径是通过hash符号(#)进行标识
  history: createWebHashHistory(),
  //routers: routers 的缩写
  routes,
});

//ES6 模块导出语句，以用于将router对象导出，以便其他文件可以导入和使用这个对象
export default router