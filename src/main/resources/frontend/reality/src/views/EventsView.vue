<template>
  <v-container fluid>
    <v-card>
      <v-card-actions
        class="text-h5 font-weight-black"
      >
        Events List
        <v-spacer></v-spacer>
        <v-btn
          v-if="editAllowed"
          color="primary"
          @click.stop="openForm"
        >
          <v-icon left>
            mdi-playlist-plus
          </v-icon>
          New Event
        </v-btn>
      </v-card-actions>
      <v-divider class="mx-8 mt-4"></v-divider>
      <v-card-text>
        <v-data-table
          :headers="headers"
          :items="eventsList"
          item-key="id"
          :items-per-page="10"
          :loading="loading"
          loading-text="Fetching data, please wait."
          sort-by="id"
          sort-desc
          @click:row="itemClicked"
          @contextmenu:row.prevent="itemRightClicked"
        >
        </v-data-table>
      </v-card-text>
    </v-card>

    <!-- Item Context Menu -->
    <v-menu
      v-if="editAllowed"
      v-model="showMenu"
      rounded="lg"
      :position-x="x"
      :position-y="y"
      absolute
      offset-y
    >
      <v-list>
        <v-list-item
          link
          @click="editItem"
        >
          <v-list-item-title>Edit</v-list-item-title>
        </v-list-item>
        <v-list-item
          v-if="deleteAllowed"
          link
          @click="deleteItem"
        >
          <v-list-item-title>Delete</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>

    <!-- Event Full Screen Dialog -->
    <v-dialog
      v-model="dialog"
      persistent
      eager
      fullscreen
    >
      <event-form
        @form-close="closeForm"
        @form-submit="formSuccess"
        :title="formTitle"
        :titleBarColor="formTitleBarColor"
        :mode="formMode"
        :key="recreate"
      >
      </event-form>
    </v-dialog>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'
import EventForm from '../components/EventForm.vue'

export default Vue.extend({
  name: 'EventsView',

  components: {
    EventForm
  },

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Event'
    }
  },

  data () {
    return {
      recreate: 0,
      editAllowed: false,
      deleteAllowed: false,
      dialog: false,
      formTitle: '',
      formTitleBarColor: '',
      formMode: '',
      showMenu: false,
      x: 0,
      y: 0,
      loading: false,
      headers: [
        {
          text: 'ID',
          align: 'start',
          sortable: true,
          value: 'id'
        },
        {
          text: 'System',
          align: 'start',
          sortable: true,
          value: 'system'
        },
        {
          text: 'Zones',
          align: 'start',
          sortable: false,
          value: 'zones'
        },
        {
          text: 'Event Type',
          align: 'start',
          sortable: true,
          value: 'event_types'
        },
        {
          text: 'Start Date',
          align: 'start',
          sortable: true,
          value: 'start_date'
        },
        {
          text: 'End Date',
          align: 'start',
          sortable: true,
          value: 'end_date'
        },
        {
          text: 'Created By',
          align: 'start',
          sortable: true,
          value: 'created_by'
        }
      ],
      selectedEvent: {
        id: 0,
        machine: '',
        system: '',
        zones: [],
        event_types: [],
        start_date: '',
        end_date: '',
        jira_case: '',
        features_on: '',
        features_off: '',
        compiled_sources: '',
        api_used: ''
      }
    }
  },

  computed: {
    ...mapGetters({
      eventsList: 'getEventsList',
      event: 'getEvent',
      isAuth: 'isAuthenticated',
      user: 'getUserState'
    })
  },

  methods: {
    ...mapActions([
      'GetEventsList', 'SetEvent'
    ]),

    async getEvents () {
      try {
        this.loading = true
        await this.GetEventsList()
        this.loading = false
      } catch (error) {

      }
    },

    formSuccess (param: string) {
      if (param === 'success') {
        this.completeMode()
      } else if (param === 'error') {
        this.errorMode()
      }
    },

    openForm () {
      this.dialog = true
      this.createMode()
    },

    closeForm () {
      this.getEvents()
      this.dialog = false
    },

    async enquireMode () {
      await this.SetEvent(this.selectedEvent)
      this.recreate += 1
      this.formTitle = 'Displaying Event #' + this.selectedEvent.id
      this.formTitleBarColor = 'secondary'
      this.formMode = 'enquire'
    },

    async createMode () {
      await this.SetEvent('')
      this.recreate += 1
      this.formTitle = 'New Event'
      this.formTitleBarColor = 'primary'
      this.formMode = 'create'
    },

    async updateMode () {
      await this.SetEvent(this.selectedEvent)
      this.recreate += 1
      this.formTitle = 'Update Event #' + this.selectedEvent.id
      this.formTitleBarColor = 'accent'
      this.formMode = 'update'
    },

    async deleteMode () {
      await this.SetEvent(this.selectedEvent)
      this.recreate += 1
      this.formTitle = 'Delete Event #' + this.selectedEvent.id
      this.formTitleBarColor = 'warning'
      this.formMode = 'delete'
    },

    completeMode () {
      switch (this.formMode) {
        case 'create':
          this.formTitle = 'Event #' + this.event.id + ' added!'
          break
        case 'update':
          this.formTitle = 'Event #' + this.event.id + ' updated!'
          break
        case 'delete':
          this.formTitle = 'Event #' + this.event.id + ' deleted!'
          break
        default:
          break
      }
      this.formTitleBarColor = 'success'
      this.formMode = 'complete'
    },

    errorMode () {
      this.formTitle = 'Event error!'
      this.formTitleBarColor = 'error'
    },

    // eslint-disable-next-line
    itemRightClicked (event: any, { item }: { item: any}) {
      this.showMenu = false
      this.selectedEvent = item
      this.x = event.clientX
      this.y = event.clientY
      this.$nextTick(() => {
        this.showMenu = true
      })
    },

    // eslint-disable-next-line
    itemClicked (args: any, { item }: { item: any }) {
      this.selectedEvent = item
      this.enquireMode()
      this.dialog = true
    },

    editItem () {
      this.updateMode()
      this.dialog = true
    },

    deleteItem () {
      this.deleteMode()
      this.dialog = true
    }
  },

  async mounted () {
    await this.getEvents()
    if (this.isAuth) {
      this.editAllowed = true
      const superuser = this.user.roles.find((x: string) => x === 'ROLE_SUPERUSER')
      if (superuser !== undefined) {
        this.deleteAllowed = true
      }
    }
  }
})
</script>
