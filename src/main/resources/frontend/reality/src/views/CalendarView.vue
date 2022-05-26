<template>
  <v-row class="fill-height">
    <v-col>
      <v-sheet height="64">
        <v-toolbar
          flat
        >
          <v-btn
            outlined
            class="mr-4"
            color="grey darken-2"
            @click="setToday"
          >
            Today
          </v-btn>
          <v-btn
            fab
            text
            small
            color="grey darken-2"
            @click="prev"
          >
            <v-icon small>
              mdi-chevron-left
            </v-icon>
          </v-btn>
          <v-btn
            fab
            text
            small
            color="grey darken-2"
            @click="next"
          >
            <v-icon small>
              mdi-chevron-right
            </v-icon>
          </v-btn>
          <v-toolbar-title v-if="$refs.calendar">
            {{ $refs.calendar.title }}
          </v-toolbar-title>
          <v-spacer></v-spacer>
          <v-menu
            bottom
            right
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                outlined
                color="grey darken-2"
                v-bind="attrs"
                v-on="on"
              >
                <span>{{ typeToLabel[type] }}</span>
                <v-icon right>
                  mdi-menu-down
                </v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item @click="type = 'day'">
                <v-list-item-title>Day</v-list-item-title>
              </v-list-item>
              <v-list-item @click="type = 'week'">
                <v-list-item-title>Week</v-list-item-title>
              </v-list-item>
              <v-list-item @click="type = 'month'">
                <v-list-item-title>Month</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </v-toolbar>
      </v-sheet>
      <v-sheet height="600">
        <v-calendar
          ref="calendar"
          v-model="focus"
          color="primary"
          :events="events"
          :event-color="getEventColor"
          :type="type"
          @click:event="showEvent"
          @click:more="viewDay"
          @click:date="viewDay"
          @change="updateRange"
        ></v-calendar>
        <!-- This is the popup that opens when an event is clicked -->
        <v-menu
          v-model="selectedOpen"
          :close-on-content-click="false"
          :activator="selectedElement"
          offset-x
        >
          <event-popup
            :title="popupTitle"
            :color="selectedEvent.color"
            :eventItem="selectedEvent"
            @close-popup="closePopup"
          >
          </event-popup>
        </v-menu>
      </v-sheet>
    </v-col>
  </v-row>
</template>
<!-- Typescript is not defined as the language to prevent unnecessary difficulties when setting types -->
<script>
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'
import EventPopup from '../components/EventPopup.vue'

export default Vue.extend({
  name: 'CalendarView',

  components: { EventPopup },

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Calendar'
    }
  },

  data: () => ({
    popupTitle: '',
    focus: '',
    type: 'month',
    typeToLabel: {
      month: 'Month',
      week: 'Week',
      day: 'Day'
    },
    selectedEvent: {},
    selectedElement: null,
    selectedOpen: false,
    events: [],
    colors: [
      'red', 'pink', 'purple', 'blue',
      'teal', 'green', 'lime', 'amber'
    ]
  }),

  computed: {
    ...mapGetters({
      eventsOnDateRange: 'getEventsOnDateRange'
    })
  },

  methods: {
    ...mapActions([
      'GetEventsOnDateRange'
    ]),

    async getEventsOnDateRange (dateRange) {
      try {
        await this.GetEventsOnDateRange(dateRange)
      } catch (error) {

      }
    },

    viewDay ({ date }) {
      this.focus = date
      this.type = 'day'
    },

    getEventColor (event) {
      return event.color
    },

    /* Used on the UI */
    setToday () {
      this.focus = ''
    },

    /* Used on the UI */
    prev () {
      this.$refs.calendar.prev()
    },

    /* Used on the UI */
    next () {
      this.$refs.calendar.next()
    },

    showEvent ({ nativeEvent, event }) {
      this.popupTitle = 'Event #' + event.id + ' by ' + event.created_by
      const open = () => {
        let zones = ''
        if (Array.isArray(event.zones)) {
          zones = event.zones.join(', ')
        } else {
          zones = event.zones
        }

        let eventTypes = ''
        if (Array.isArray(event.zones)) {
          eventTypes = event.event_types.join(', ')
        } else {
          eventTypes = event.event_types
        }

        const mEvent = event
        mEvent.zones = zones
        mEvent.event_types = eventTypes
        this.selectedEvent = mEvent
        this.selectedElement = nativeEvent.target
        requestAnimationFrame(() => requestAnimationFrame(() => { this.selectedOpen = true }))
      }
      if (this.selectedOpen) {
        this.selectedOpen = false
        requestAnimationFrame(() => requestAnimationFrame(() => open()))
      } else {
        open()
      }
      nativeEvent.stopPropagation()
    },

    closePopup () {
      this.selectedOpen = false
    },

    async updateRange ({ start, end }) {
      const range = {
        startDate: start.date,
        endDate: end.date
      }

      await this.getEventsOnDateRange(range)

      const events = []
      const eventCount = this.eventsOnDateRange.length

      for (let i = 0; i < eventCount; i++) {
        const eventItem = this.eventsOnDateRange[i]
        events.push({
          /* Event Model Properties */
          id: eventItem.id,
          system: eventItem.system,
          zones: eventItem.zones,
          event_types: eventItem.event_types,
          start_date: eventItem.start_date,
          end_date: eventItem.end_date,
          jira_case: eventItem.jira_case,
          features_on: eventItem.features_on,
          features_off: eventItem.features_off,
          compiled_sources: eventItem.compiled_sources,
          api_used: eventItem.api_used,
          created_by: eventItem.created_by,
          creation_date: eventItem.creation_date,
          last_changed_by: eventItem.last_changed_by,
          last_changed_date: eventItem.last_changed_date,
          /* Vuetify Default Properties */
          name: eventItem.created_by,
          start: Date.parse(eventItem.start_date),
          end: Date.parse(eventItem.end_date),
          color: this.colors[this.rnd(0, this.colors.length - 1)],
          timed: false
        })
      }

      this.events = events
    },

    rnd (a, b) {
      return Math.floor((b - a + 1) * Math.random()) + a
    }
  },

  async mounted () {
    this.$refs.calendar.checkChange()
  }
})
</script>
