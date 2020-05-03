import Vue from 'vue';
import VueRouter from 'vue-router';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    redirect: '/home',
  },
  {
    path: '/home',
    component: () => import('../views/home/Home'),
    children: [
      {
        path: '',
        redirect: 'alarm-setting',
      },
      {
        path: 'alarm-setting',
        component: () => import('../views/alarmSetting/AlarmSetting'),
      },
      {
        path: 'alarm-type-indicator',
        component: () => import('../views/alarmTypeIndicator/AlarmTypeIndicator'),
      },
      {
        path: 'alarm-info',
        component: () => import('../views/alarmInfo/AlarmInfo'),
      },
      {
        path: 'current-alarm',
        component: () => import('../views/currentAlarm/CurrentAlarm'),
      },
      // {
      //   path: 'alarm-history',
      //   component: () => import('../views/alarmHistory/AlarmHistory'),
      // },
    ],
  },
];

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes,
});

export default router;
