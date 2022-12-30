<template>
  <v-container fluid>
    <v-card>
      <v-card-title>
        Search
      </v-card-title>
      <v-card-subtitle class="font-italic">
        Seek and you shall find.
      </v-card-subtitle>
      <v-card-text>
        <v-scroll-x-reverse-transition>
          <v-form
            v-if="!show"
          >
            <v-row align="center">
              <v-col cols="10">
                <v-text-field
                  v-model="searchForm.keyword"
                  label="Search for something.."
                  clearable
                ></v-text-field>
              </v-col>
              <v-col cols="1">
                <v-btn
                  color="primary"
                  @click.stop="search"
                >
                  <v-icon left>
                    mdi-magnify
                  </v-icon>
                  Search
                </v-btn>
              </v-col>
            </v-row>
          </v-form>
        </v-scroll-x-reverse-transition>
      </v-card-text>
      <v-card-actions>
        <v-btn
          color="orange lighten-2"
          text
          @click="showFilters"
        >
          Filters
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn
          icon
          @click="show = !show"
        >
          <v-icon>{{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
        </v-btn>
      </v-card-actions>
      <v-scroll-x-reverse-transition>
        <div v-show="show">
          <v-divider></v-divider>
          <v-card-text>
            <v-form
              :disabled="isDisabled.form"
            >
              <v-row>
                <v-col>
                  <v-menu
                    v-model="sDateMenu"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    transition="scale-transition"
                    offset-y
                    min-width="auto"
                  >
                    <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="searchForm.start_date"
                          label="From Date"
                          prepend-icon="mdi-calendar"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                    </template>
                      <v-date-picker
                        no-title
                        v-model="searchForm.start_date"
                        @input="startDatePicked"
                      ></v-date-picker>
                  </v-menu>
                </v-col>
                <v-col>
                  <v-menu
                    v-model="eDateMenu"
                    :close-on-content-click="false"
                    :nudge-right="40"
                    transition="scale-transition"
                    offset-y
                    min-width="auto"
                  >
                    <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          v-model="searchForm.end_date"
                          label="To Date"
                          prepend-icon="mdi-calendar"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                    </template>
                      <v-date-picker
                        no-title
                        v-model="searchForm.end_date"
                        @input="endDatePicked"
                      ></v-date-picker>
                  </v-menu>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-select
                    v-model="searchForm.event_types"
                    :items="eventTypes"
                    :loading="isLoading.eventType"
                    :disabled="isDisabled.eventType"
                    label="Event Type"
                  ></v-select>
                </v-col>
                <v-col>
                  <v-select
                    v-model="searchForm.machine"
                    :items="machines"
                    :loading="isLoading.machines"
                    :disabled="isDisabled.machines"
                    label="Machine"
                    @input="machineSelected"
                  ></v-select>
                </v-col>
                <v-col>
                  <v-select
                    v-model="searchForm.global_prefix"
                    :items="systems"
                    :loading="isLoading.systems"
                    :disabled="isDisabled.systems"
                    label="System"
                    @input="systemSelected"
                  ></v-select>
                </v-col>
                <v-col>
                  <v-select
                    v-model="searchForm.zones"
                    :items="zones"
                    :loading="isLoading.zones"
                    :disabled="isDisabled.zones"
                    label="Zone"
                  ></v-select>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-text-field
                    v-model="searchForm.features_on"
                    label="Features On"
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    v-model="searchForm.features_off"
                    label="Features Off"
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    v-model="searchForm.compiled_sources"
                    label="Compiled Sources"
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    v-model="searchForm.api_used"
                    label="API Used"
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="3">
                  <v-text-field
                    v-model="searchForm.jira_case"
                    label="JIRA"
                  ></v-text-field>
                </v-col>
              </v-row>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="primary"
              @click.stop="filter"
            >
              <v-icon left>
                mdi-magnify
              </v-icon>
              Search
            </v-btn>
          </v-card-actions>
        </div>
      </v-scroll-x-reverse-transition>
    </v-card>
  </v-container>
</template>

<script>
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'SearchPanel',

  data () {
    return {
      show: false,
      sDateMenu: false,
      eDateMenu: false,
      isLoading: {
        eventType: false,
        machines: false,
        systems: false,
        zones: false
      },
      isDisabled: {
        form: false,
        eventType: false,
        machines: false,
        systems: false,
        zones: false
      },
      eventTypes: [],
      machines: [],
      systems: [],
      zones: [],
      searchForm: {
        keyword: '',
        machine: '',
        global_prefix: '',
        zones: '',
        event_types: '',
        start_date: '',
        end_date: '',
        jira_case: '',
        features_on: '',
        features_off: '',
        compiled_sources: '',
        api_used: ''
      },
      searchBody: {
        machine: '',
        global_prefix: '',
        zones: '',
        event_types: '',
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
      machinesList: 'getMachineList',
      systemsList: 'getSystemsList',
      eventTypesList: 'getEventTypesList',
      hasError: 'getErrorStatus'
    })
  },

  methods: {
    ...mapActions([
      'SetSearchBody', 'GetSearchEvents', 'PostFilterEvents', 'GetMachinesList', 'GetEventTypesList', 'GetSystemsOnMachine'
    ]),

    startDatePicked () {
      this.sDateMenu = !this.sDateMenu
    },

    endDatePicked () {
      this.eDateMenu = !this.eDateMenu
    },

    async showFilters () {
      this.show = !this.show
      this.isDisabled.form = true
      if (this.show) {
        await Promise.all([
          this.fetchMachines(),
          this.fetchEventTypes()
        ])
      } else {
        this.clearSearchForm()
        this.clearDropdownLists()
        this.clearSearchBody()
      }
      this.isDisabled.form = false
    },

    async search () {
      await this.GetSearchEvents(this.searchForm.keyword)
    },

    async filter () {
      // Initiate the process
      this.startProcess()
      // Clear any existing search body object
      this.clearSearchBody()
      // Build the body of the request to be sent to the backend
      this.buildSearchBody()
      // Send the completed body to the search handler
      await this.PostFilterEvents(this.searchBody)
      // Check for errors
      if (this.hasError) {
        this.processError()
      } else {
        this.endProcess()
      }
    },

    startProcess () {
      this.isDisabled.form = true
    },

    endProcess () {
      this.isDisabled.form = false
    },

    processError () {
      this.isDisabled.form = false
    },

    clearDropdownLists () {
      this.eventTypes = []
      this.machines = []
      this.systems = []
      this.zones = []
    },

    clearSearchForm () {
      this.searchForm.keyword = ''
      this.searchForm.machine = ''
      this.searchForm.global_prefix = ''
      this.searchForm.zones = ''
      this.searchForm.event_types = ''
      this.searchForm.start_date = ''
      this.searchForm.end_date = ''
      this.searchForm.jira_case = ''
      this.searchForm.features_on = ''
      this.searchForm.features_off = ''
      this.searchForm.compiled_sources = ''
      this.searchForm.api_used = ''
    },

    clearSearchBody () {
      this.searchBody.machine = ''
      this.searchBody.global_prefix = ''
      this.searchBody.zones = ''
      this.searchBody.event_types = ''
      this.searchBody.start_date = ''
      this.searchBody.end_date = ''
      this.searchBody.jira_case = ''
      this.searchBody.features_on = ''
      this.searchBody.features_off = ''
      this.searchBody.compiled_sources = ''
      this.searchBody.api_used = ''
    },

    buildSearchBody () {
      // Build the search body based on the contents of the search form
      this.searchBody.machine = this.searchForm.machine.name
      this.searchBody.global_prefix = this.searchForm.global_prefix.global_prefix
      this.searchBody.zones = this.searchForm.zones
      this.searchBody.event_types = this.searchForm.event_types.code
      this.searchBody.start_date = this.searchForm.start_date
      this.searchBody.end_date = this.searchForm.end_date
      this.searchBody.jira_case = this.searchForm.jira_case
      this.searchBody.features_on = this.searchForm.features_on
      this.searchBody.features_off = this.searchForm.features_off
      this.searchBody.compiled_sources = this.searchForm.compiled_sources
      this.searchBody.api_used = this.searchForm.api_used
      this.SetSearchBody(this.searchBody)
    },

    async fetchEventTypes () {
      this.isLoading.eventType = true
      this.eventTypes = []
      await this.GetEventTypesList()

      if (this.hasError) {
        console.log('Handle event type fetching error.')
      } else {
        this.eventTypesList.forEach((element) => {
          const item = {
            text: element.name,
            value: element
          }
          this.eventTypes.push(item)
        })
      }

      this.isLoading.eventType = false
    },

    async fetchMachines () {
      this.isLoading.machines = true
      this.machines = []
      await this.GetMachinesList()

      if (this.hasError) {
        console.log('Handle machine fetching error.')
      } else {
        this.machinesList.forEach((element) => {
          const item = {
            text: element.name,
            value: element
          }
          this.machines.push(item)
        })
      }

      this.isLoading.machines = false
    },

    async fetchSystems (args) {
      this.isLoading.systems = true
      this.isDisabled.systems = true
      await this.GetSystemsOnMachine(args)

      if (this.hasError) {
        console.log('Handle system fetching error.')
      } else {
        this.systems = []
        this.systemsList.forEach((element) => {
          const item = {
            text: element.global_prefix,
            value: element
          }
          this.systems.push(item)
        })
      }

      this.isLoading.systems = false
      this.isDisabled.systems = false
    },

    buildZones () {
      this.isLoading.zones = true
      this.isDisabled.zones = true

      this.zones = []

      const zPrefixArr = this.searchForm.global_prefix.zone_prefixes
      const zNameArr = this.searchForm.global_prefix.zone_names

      for (let i = 0; i < zPrefixArr.length; i++) {
        const item = {
          text: zPrefixArr[i] + ' - ' + zNameArr[i],
          value: zPrefixArr[i]
        }
        this.zones.push(item)
      }

      this.isLoading.zones = false
      this.isDisabled.zones = false
    },

    async machineSelected () {
      if (this.searchForm.machine !== null) {
        await this.fetchSystems(this.searchForm.machine.name)
      }
    },

    systemSelected () {
      this.buildZones()
    }
  }
})
</script>
