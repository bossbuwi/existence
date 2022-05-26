import Vue from 'vue'
import VueRouter, { RouteConfig } from 'vue-router'
import VueMeta from 'vue-meta'
import store from '../store/index'

import DashboardView from '../views/DashboardView.vue'
import SystemsView from '../views/SystemsView.vue'
import CalendarView from '../views/CalendarView.vue'
import EventsView from '../views/EventsView.vue'
import ReportsView from '../views/ReportsView.vue'
import UserProfileView from '../views/UserProfileView.vue'
import SettingsView from '../views/SettingsView.vue'
import AboutView from '../views/AboutView.vue'

Vue.use(VueRouter)
Vue.use(VueMeta)

const isAuth = store.getters.isAuthenticated
const user = store.getters.getUserState

const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'dashboard',
    component: DashboardView
  },
  {
    path: '/systems',
    name: 'system',
    component: SystemsView
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
    path: '/reports',
    name: 'reports',
    component: ReportsView,
    beforeEnter: (to, from, next) => {
      const admin = user.roles.find((x: string) => x === 'ROLE_ADMIN')
      console.log(user.roles)
      if (admin === undefined) {
        next('/')
      } else {
        next()
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
