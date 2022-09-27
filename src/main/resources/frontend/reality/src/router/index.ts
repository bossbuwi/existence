/* eslint-disable */
import Vue from 'vue'
import VueRouter, { RouteConfig } from 'vue-router'
import VueMeta from 'vue-meta'
import store from '../store/index'

import DashboardView from '../views/DashboardView.vue'
import MiscView from '../views/MiscView.vue'
import CalendarView from '../views/CalendarView.vue'
import EventsView from '../views/EventsView.vue'
import CoblogView from '../views/CoblogView.vue'
import ReportsView from '../views/ReportsView.vue'
import UserProfileView from '../views/UserProfileView.vue'
import SettingsView from '../views/SettingsView.vue'
import AboutView from '../views/AboutView.vue'

Vue.use(VueRouter)
Vue.use(VueMeta)

const isAuth = store.getters.isAuthenticated
const user = store.getters.getUserState
const disabledFeatures = store.getters.getDisabledSwitchableList

const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'dashboard',
    component: DashboardView
  },
  {
    path: '/misc',
    name: 'misc',
    component: MiscView,
    props: true
  },
  {
    path: '/calendar',
    name: 'calendar',
    component: CalendarView
  },
  {
    path: '/events',
    name: 'events',
    component: EventsView
  },
  {
    path: '/coblogs',
    name: 'coblogs',
    component: CoblogView,
    beforeEnter: (to, from, next) => {
      const feature = 'RQM001'
      const filteredArr = disabledFeatures.filter((x: any) => x.key === feature)
      if (filteredArr.length === 0) {
        next()
      } else {
        next('/')
      }
    }
  },
  {
    path: '/search',
    name: 'search',
    component: ReportsView,
    beforeEnter: (to, from, next) => {
      const feature = 'ELS001'
      const filteredArr = disabledFeatures.filter((x: any) => x.key === feature)
      if (filteredArr.length === 0) {
        next()
      } else {
        next('/')
      }
    }
  },
  {
    path: '/user',
    name: 'user',
    component: UserProfileView,
    beforeEnter: (to, from, next) => {
      if (!isAuth) {
        next('/')
      } else {
        next()
      }
    }
  },
  {
    path: '/settings',
    name: 'settings',
    component: SettingsView,
    beforeEnter: (to, from, next) => {
      const superuser = user.roles.find((x: string) => x === 'ROLE_SUPERUSER')
      if (superuser === undefined) {
        next('/')
      } else {
        next()
      }
    }
  },
  {
    path: '/about',
    name: 'about',
    component: AboutView
  }
]

const router = new VueRouter({
  // Uncomment to remove hashtag from app URL
  // This needs tweaking on the server side to use properly
  // mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
