import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'


// import VueSimpleAlert from "vue-simple-alert";

// Vue.use(VueSimpleAlert);

// import Alert from 'alert';

// Vue.use(Alert)

import {BootstrapVue, IconsPlugin} from 'bootstrap-vue'


import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue, IconsPlugin);



Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
