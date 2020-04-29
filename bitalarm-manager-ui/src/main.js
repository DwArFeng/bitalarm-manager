import Vue from 'vue';
import router from '@/router';
import '@/store';
import App from './App.vue';
import '@/elements';
import 'element-ui/lib/theme-chalk/index.css';
import '@/util/http';

Vue.config.productionTip = false;

new Vue({
  router,
  render: (h) => h(App),
}).$mount('#app');
