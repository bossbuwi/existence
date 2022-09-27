import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import axios from 'axios'

Vue.config.productionTip = false
axios.defaults.withCredentials = true
axios.defaults.baseURL = process.env.VUE_DEFAULT_URL

async function initialize () {
  let serverData = [] as []
  await fetch('symphony/con/settings/setting/switchable/disabled')
    .then(response => response.json())
    .then(data => {
      serverData = data
    })

  new Vue({
    router,
    store,
    vuetify,
    beforeCreate () {
      store.dispatch('SetDisabledSwitchableFeatures', serverData)
    },
    render: h => h(App)
  }).$mount('#app')
}

initialize()
