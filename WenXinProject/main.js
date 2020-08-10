import Vue from 'vue'
import App from './App'

import store from './store'

import axios from './common/request/interface'

Vue.config.productionTip = false

Vue.prototype.$store = store

Vue.prototype.$axios = axios;
store.$axios = axios;

App.mpType = 'app'

const app = new Vue({
	store,
	...App
})
app.$mount()
