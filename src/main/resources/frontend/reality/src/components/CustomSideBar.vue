<template>
  <v-navigation-drawer
    app
    permanent
    clipped
  >
    <v-list
      nav
    >
      <v-list-item
        v-for="item in filteredList"
        :key="item.title"
        :to="item.route"
        color="blue darken-4"
        link
      >
      <!-- <v-list-item
        v-for="item in items"
        :key="item.title"
        :to="item.route"
        color="blue darken-4"
        link
      > -->
        <v-list-item-icon>
          <v-icon>{{ item.icon }}</v-icon>
        </v-list-item-icon>

        <v-list-item-content>
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </v-navigation-drawer>
</template>

<script>
import Vue from 'vue'
import { mapGetters } from 'vuex'

export default Vue.extend({
  name: 'CustomSideBar',

  data: () => ({
    items: [
      { title: 'Dashboard', icon: 'mdi-view-dashboard-outline', route: '/' },
      { title: 'Systems', icon: 'mdi-web', route: '/systems' },
      { title: 'Calendar', icon: 'mdi-calendar-outline', route: '/calendar' },
      { title: 'Events', icon: 'mdi-alarm', route: '/events' },
      { title: 'Coblogs', icon: 'mdi-list-status', route: '/coblogs', feature: 'RQM001' },
      { title: 'Search', icon: 'mdi-magnify', route: '/search' },
      { title: 'User Profile', icon: 'mdi-account-outline', route: '/user', online: 'yes' },
      { title: 'Settings', icon: 'mdi-cog-outline', route: '/settings', online: 'yes', restricted: 'yes' },
      { title: 'About', icon: 'mdi-application-brackets-outline', route: '/about' }
    ]
  }),

  computed: {
    ...mapGetters({
      isAuth: 'isAuthenticated',
      getUser: 'getUserState',
      frontendFeatures: 'getFrontendList',
      disabledSwitchableFeatures: 'getDisabledSwitchableList'
    }),

    filteredList () {
      let navList = []
      const disabledList = this.disabledSwitchableFeatures

      disabledList.forEach(element => {
        navList = this.items.filter(x => x.feature !== element.key)
      })

      if (this.isAuth > 0) {
        const admin = this.getUser.roles.find(x => x === 'ROLE_ADMIN')

        if (admin === 'ROLE_ADMIN') {
          return navList
        }

        return navList.filter(x => x.restricted !== 'yes')
      }

      return navList.filter(x => x.online !== 'yes')
    }
  }
})
</script>
