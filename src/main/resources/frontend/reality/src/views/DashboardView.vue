<template>
  <v-container fluid>
    <v-row>
      <v-col
        cols="12"
      >
        <rules-panel
          :rules="ruleList"
        >
        </rules-panel>
      </v-col>
    </v-row>
    <v-row>
      <v-col
        cols="12"
      >
        <v-skeleton-loader
          v-if="!eventCardLoaded"
          type="card"
          transition="scale-transition"
        >
        </v-skeleton-loader>
        <custom-card-event
          v-if="eventCardLoaded"
          @refresh-data="getLatestEvent"
          :shown="eventShown"
          :globalPrefix="latestEvent.system"
          :zones="latestEvent.zones"
          :eventTypes="latestEvent.event_types"
          :startDate="latestEvent.start_date"
          :endDate="latestEvent.end_date"
          :lastModifiedBy="latestEvent.last_changed_by"
          :loading="eventCardReload"
        >
        </custom-card-event>
      </v-col>
    </v-row>

    <v-row>
      <v-col
        cols="4"
      >
        <v-skeleton-loader
          :max-height="smallCardHeight"
          v-if="!systemsLoaded"
          type="card"
          transition="scale-transition"
        >
        </v-skeleton-loader>
        <!-- <router-link to="/misc"> -->
        <custom-card-basic
          v-if="systemsLoaded"
          @refresh-data="getSystemCount"
          :cardIcon="systems.icon"
          :cardName="systems.name"
          :cardContent="systemCount"
          :loading="systemReload"
          link="misc"
          tabNumber=1
        >
        </custom-card-basic>
        <!-- </router-link> -->
      </v-col>

      <v-col
        cols="4"
      >
        <v-skeleton-loader
          :max-height="smallCardHeight"
          v-if="!eventsLoaded"
          type="card"
          transition="scale-transition"
        >
        </v-skeleton-loader>
        <custom-card-basic
          v-if="eventsLoaded"
          @refresh-data="getEventCount"
          :cardIcon="events.icon"
          :cardName="events.name"
          :cardContent="eventCount"
          :loading="eventReload"
          link="events"
          tabNumber=0
        >
        </custom-card-basic>
      </v-col>

      <v-col
        cols="4"
      >
        <v-skeleton-loader
          :max-height="smallCardHeight"
          v-if="!usersLoaded"
          type="card"
          transition="scale-transition"
        >
        </v-skeleton-loader>
        <custom-card-basic
          v-if="usersLoaded"
          @refresh-data="getUserCount"
          :cardIcon="users.icon"
          :cardName="users.name"
          :cardContent="userCount"
          :loading="userReload"
          link="misc"
          tabNumber=5
        >
        </custom-card-basic>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'
import CustomCardBasic from '@/components/CustomCardBasic.vue'
import CustomCardEvent from '@/components/CustomCardEvent.vue'
import RulesPanel from '@/components/RulesPanel.vue'

export default Vue.extend({
  name: 'DashboardView',

  components: {
    CustomCardBasic,
    CustomCardEvent,
    RulesPanel
  },

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Dashboard'
    }
  },

  data () {
    return {
      eventCardLoaded: false,
      systemsLoaded: false,
      eventsLoaded: false,
      usersLoaded: false,
      eventShown: false,
      smallCardHeight: 185,
      eventCardReload: false,
      systemReload: false,
      eventReload: false,
      userReload: false,
      systems: {
        name: 'Systems',
        icon: 'mdi-web',
        content: '2'
      },
      events: {
        name: 'Events',
        icon: 'mdi-calendar',
        content: '20'
      },
      users: {
        name: 'Users',
        icon: 'mdi-heart-outline'
      },
      event: {
        globalPrefix: 'OS',
        zones: 'O4, O5',
        eventTypes: 'SYSMAINT',
        startDate: '2022-04-12',
        endDate: '2022-04-15',
        lastModifiedBy: 'admin'
      }
    }
  },

  computed: {
    ...mapGetters({
      systemCount: 'getSystemCount',
      eventCount: 'getEventCount',
      userCount: 'getUserCount',
      latestEvent: 'getLatestEvent',
      ruleList: 'getRules'
    })
  },

  methods: {
    ...mapActions([
      'GetSystemCount',
      'GetEventCount',
      'GetUserCount',
      'GetLatestEvent',
      'GetRules'
    ]),

    async getSystemCount () {
      try {
        this.systemReload = true
        await this.GetSystemCount()
        this.systemReload = false
      } catch (error) {

      }
    },

    async getEventCount () {
      try {
        this.eventReload = true
        await this.GetEventCount()
        this.eventReload = false
      } catch (error) {

      }
    },

    async getUserCount () {
      try {
        this.userReload = true
        await this.GetUserCount()
        this.userReload = false
      } catch (error) {

      }
    },

    async getLatestEvent () {
      try {
        this.eventCardReload = true
        await this.GetLatestEvent()
        this.eventCardReload = false
      } catch (error) {

      }
    },

    async getRules () {
      await this.GetRules()
    }
  },

  async mounted () {
    await Promise.all([
      this.getRules(),
      this.getSystemCount(),
      this.getEventCount(),
      this.getUserCount()
    ])
    this.systemsLoaded = true
    this.eventsLoaded = true
    this.usersLoaded = true

    if (this.eventCount === 0) {
      this.eventCardLoaded = true
      this.eventShown = false
    } else {
      await this.getLatestEvent()
      this.eventCardLoaded = true
      this.eventShown = true
    }
  }
})
</script>
