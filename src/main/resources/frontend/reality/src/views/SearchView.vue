<template>
  <v-container fluid>
    <v-row>
      <v-col>
        <search-panel></search-panel>
      </v-col>
    </v-row>

    <!-- Search Results List -->
    <v-row>
      <v-col>
        <search-list
          v-if="hasResults"
          @item-clicked="itemClicked"
          @edit-item="editItem"
          @delete-item="deleteItem"
        ></search-list>
      </v-col>
    </v-row>

    <!-- Event Full Screen Dialog -->
    <v-dialog
      v-model="dialog"
      persistent
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
import SearchPanel from '@/components/search/SearchPanel.vue'
import SearchList from '@/components/search/SearchList.vue'
import EventForm from '../components/EventForm.vue'

export default Vue.extend({
  name: 'SearchView',

  components: {
    SearchPanel, SearchList, EventForm
  },

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Search'
    }
  },

  data () {
    return {
      dialog: false,
      recreate: 0,
      formTitle: '',
      formTitleBarColor: '',
      formMode: ''
    }
  },

  computed: {
    ...mapGetters({
      selectedEvent: 'getEvent',
      hasResults: 'hasResults',
      savedBody: 'getSearchBody'
    })
  },

  methods: {
    ...mapActions([
      'SetEvent', 'ClearSearchResults', 'PostFilterEvents'
    ]),

    closeForm () {
      this.dialog = false
      this.PostFilterEvents()
    },

    openForm () {
      this.recreate += this.recreate
      this.dialog = true
    },

    formEnquireMode () {
      this.formTitle = 'Displaying Event #' + this.selectedEvent.id
      this.formTitleBarColor = 'secondary'
      this.formMode = 'enquire'
    },

    formUpdateMode () {
      this.formTitle = 'Update Event #' + this.selectedEvent.id
      this.formTitleBarColor = 'accent'
      this.formMode = 'update'
    },

    formDeleteMode () {
      this.formTitle = 'Delete Event #' + this.selectedEvent.id
      this.formTitleBarColor = 'warning'
      this.formMode = 'delete'
    },

    formSuccess (param: string) {
      if (param === 'success') {
        this.formComplete()
      } else if (param === 'error') {
        this.formError()
      }
    },

    formComplete () {
      switch (this.formMode) {
        case 'create':
          this.formTitle = 'Event #' + this.selectedEvent.id + ' added!'
          break
        case 'update':
          this.formTitle = 'Event #' + this.selectedEvent.id + ' updated!'
          break
        case 'delete':
          this.formTitle = 'Event deleted!'
          break
        default:
          break
      }
      this.formTitleBarColor = 'success'
      this.formMode = 'complete'
    },

    formError () {
      this.formTitle = 'Event error!'
      this.formTitleBarColor = 'error'
    },

    // eslint-disable-next-line
    itemClicked (args: any) {
      this.SetEvent(args)
      this.formEnquireMode()
      this.openForm()
    },

    // eslint-disable-next-line
    editItem (args: any) {
      this.SetEvent(args)
      this.formUpdateMode()
      this.openForm()
    },

    deleteItem (args: any) {
      this.SetEvent(args)
      this.formDeleteMode()
      this.openForm()
    }
  },

  mounted () {
    this.ClearSearchResults()
  }
})
</script>
