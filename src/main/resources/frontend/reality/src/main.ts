import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import axios from 'axios'

Vue.config.productionTip = false
axios.defaults.withCredentials = true
axios.defaults.baseURL = process.env.VUE_DEFAULT_URL

initializeApplication()

function initializeApplication () {
  // fetchDependencies()
  new Vue({
    router,
    store,
    vuetify,
    render: h => h(App)
  }).$mount('#app')
}

// function fetchDependencies () {
//   store.dispatch('GetDisabledSwitchableFeatures')
//   store.dispatch('GetFrontendList')
// }
