<template>
  <v-card>
    <v-toolbar
      dark
      dense
      :color="titleBarColor"
    >
      <v-btn
        icon
        dark
        @click="close"
        @submit.prevent="submit"
      >
        <v-icon>mdi-close</v-icon>
      </v-btn>
      <v-toolbar-title>{{ title }}</v-toolbar-title>
      <v-spacer></v-spacer>
    </v-toolbar>
    <v-card-text>
      <v-container>
        <validation-observer
          ref="observer"
          v-slot="{ invalid }"
        >
          <v-form
            id="eventform"
            :disabled="formDisabled"
          >
            <v-row>
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <validation-provider
                  v-slot="{ errors }"
                  name="System"
                  rules="required"
                >
                  <v-select
                    v-model="eventForm.system"
                    :items="systems"
                    :loading="systemLoading"
                    :disabled="systemDisabled"
                    @input="systemSelected"
                    label="System*"
                    :error-messages="errors"
                    required
                  ></v-select>
                </validation-provider>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <validation-provider
                  v-slot="{ errors }"
                  name="Zone"
                  rules="required"
                >
                  <v-select
                    v-model="eventForm.zones"
                    :items="zones"
                    :loading="zonesLoading"
                    :disabled="zonesDisabled"
                    label="Zone*"
                    multiple
                    :error-messages="errors"
                    required
                  ></v-select>
                </validation-provider>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <validation-provider
                  v-slot="{ errors }"
                  name="Event Type"
                  rules="required"
                >
                  <v-select
                    v-model="eventForm.eventtypes"
                    :items="eventtypes"
                    :loading="typesLoading"
                    :disabled="typesDisabled"
                    label="Event Type*"
                    multiple
                    :error-messages="errors"
                    required
                  ></v-select>
                </validation-provider>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="6"
              >
                <v-menu
                  v-model="sDateMenu"
                  :close-on-content-click="false"
                  :nudge-right="40"
                  transition="scale-transition"
                  offset-y
                  min-width="auto"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <validation-provider
                      v-slot="{ errors }"
                      name="Start Date"
                      vid="datestart"
                      rules="required"
                    >
                      <v-text-field
                        v-model="eventForm.startDate"
                        label="Start Date*"
                        prepend-icon="mdi-calendar"
                        readonly
                        v-bind="attrs"
                        v-on="on"
                        :error-messages="errors"
                      ></v-text-field>
                    </validation-provider>
                  </template>
                    <v-date-picker
                      no-title
                      v-model="eventForm.startDate"
                      @input="startDatePicked"
                    ></v-date-picker>
                </v-menu>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="6"
              >
                <v-menu
                  v-model="eDateMenu"
                  :close-on-content-click="false"
                  :nudge-right="40"
                  transition="scale-transition"
                  offset-y
                  min-width="auto"
                >
                  <template v-slot:activator="{ on, attrs }">
                    <validation-provider
                      v-slot="{ errors }"
                      name="End Date"
                      vid="dateend"
                      rules="required|dates:@datestart"
                    >
                      <v-text-field
                        v-model="eventForm.endDate"
                        label="End Date*"
                        prepend-icon="mdi-calendar"
                        readonly
                        v-bind="attrs"
                        v-on="on"
                        :error-messages="errors"
                      ></v-text-field>
                    </validation-provider>
                  </template>
                    <v-date-picker
                      no-title
                      v-model="eventForm.endDate"
                      @input="endDatePicked"
                    ></v-date-picker>
                </v-menu>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <v-text-field
                  v-model="eventForm.jira"
                  label="Jira Reference"
                  type="text"
                ></v-text-field>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <v-text-field
                  v-model="eventForm.featuresOn"
                  label="Features Turned On"
                  hint="Please separate entries with a comma."
                  type="text"
                ></v-text-field>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <v-text-field
                  v-model="eventForm.featuresOff"
                  label="Features Turned Off"
                  hint="Please separate entries with a comma."
                  type="text"
                ></v-text-field>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="6"
              >
                <v-text-field
                  v-model="eventForm.compiledSources"
                  label="Compiled Sources"
                  hint="Please separate entries with a comma."
                  type="text"
                ></v-text-field>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="6"
              >
                <v-text-field
                  v-model="eventForm.apiUsed"
                  label="API Used"
                  hint="Please separate entries with a comma."
                  type="text"
                ></v-text-field>
              </v-col>
            </v-row>
          </v-form>
          <small v-if="!formDisabled">*indicates required field</small>
          <v-row
            class="my-4"
          >
            <v-spacer></v-spacer>
            <v-btn
              type="submit"
              v-if="submitEnabled"
              color="primary"
              :loading="submitLoading"
              :disabled="invalid"
              depressed
              @click="submit"
            >
              Submit
            </v-btn>
          </v-row>
        </validation-observer>
      </v-container>
    </v-card-text>
  </v-card>
</template>

<script>
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'
import { extend, setInteractionMode, ValidationObserver, ValidationProvider } from 'vee-validate'
import { required } from 'vee-validate/dist/rules'

setInteractionMode('aggressive')

extend('required', {
  ...required,
  message: '{_field_} cannot be empty.'
})

extend('dates', {
  params: ['target'],
  validate (value, { target }) {
    return value >= target
  },
  message: '{_field_} cannot be earlier than {target}.'
})

export default Vue.extend({
  name: 'EventForm',

  components: {
    ValidationObserver,
    ValidationProvider
  },

  data () {
    return {
      systemLoading: false,
      systemDisabled: false,
      zonesLoading: false,
      zonesDisabled: true,
      typesLoading: false,
      typesDisabled: false,
      submitLoading: false,
      sDateMenu: false,
      eDateMenu: false,
      systems: [],
      zones: [],
      eventtypes: [],
      eventForm: {
        id: 0,
        machine: '',
        system: '',
        zones: [],
        eventtypes: [],
        startDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
        endDate: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
        jira: '',
        featuresOn: '',
        featuresOff: '',
        compiledSources: '',
        apiUsed: ''
      }
    }
  },

  props: [
    'mode', 'title', 'titleBarColor'
  ],

  computed: {
    ...mapGetters({
      systemsList: 'getSystemsList',
      eventTypesList: 'getEventTypesList',
      errorStatus: 'getErrorStatus',
      event: 'getEvent'
    }),

    // can just use plain data don't you think? check coblogform.vue
    submitEnabled: {
      get () {
        if (this.mode === 'complete') return false
        if (this.mode === 'create') return true
        if (this.mode === 'update') return true
        if (this.mode === 'delete') return true
        return false
      }
    },
    formDisabled: {
      get () {
        if (this.mode === 'complete') return true
        if (this.mode === 'create') return false
        if (this.mode === 'update') return false
        if (this.mode === 'enquire') return true
        if (this.mode === 'delete') return true
        return false
      }
    }
  },

  methods: {
    ...mapActions([
      'GetSystemsList', 'GetEventTypesList', 'PostEvent',
      'PutEvent', 'DeleteEvent'
    ]),

    close () {
      this.$emit('form-close')
    },

    async submit () {
      this.formLoading()
      const types = []

      this.eventForm.eventtypes.forEach((element) => {
        types.push(element.code)
      })

      const event = {}
      if (this.mode !== 'create') {
        event.id = this.event.id
        event.global_prefix = this.event.system
        event.machine = this.event.machine
        event.zones = this.event.zones
        event.event_types = this.event.event_types
        event.start_date = this.event.start_date
        event.end_date = this.event.end_date
        event.jira_case = this.event.jira_case
        event.features_on = this.event.features_on
        event.features_off = this.event.features_off
        event.compiled_sources = this.event.compiled_sources
        event.api_used = this.event.api_used
      }

      event.global_prefix = this.eventForm.system.global_prefix
      event.machine = this.eventForm.system.machine
      event.zones = this.eventForm.zones
      event.event_types = types
      event.start_date = this.eventForm.startDate
      event.end_date = this.eventForm.endDate
      event.jira_case = this.eventForm.jira
      event.features_on = this.eventForm.featuresOn
      event.features_off = this.eventForm.featuresOff
      event.compiled_sources = this.eventForm.compiledSources
      event.api_used = this.eventForm.apiUsed

      switch (this.mode) {
        case 'create':
          await this.PostEvent(event)
          this.formSubmitted()
          break
        case 'update':
          await this.PutEvent(event)
          this.formSubmitted()
          break
        case 'delete':
          this.formDisabled = false
          await this.DeleteEvent(event)
          this.formSubmitted()
          break
        default:
          break
      }
    },

    async getSystemsList () {
      await this.GetSystemsList()
    },

    async getEventTypesList () {
      await this.GetEventTypesList()
    },

    systemSelected () {
      this.zonesDisabled = true
      this.zones = []
      this.eventForm.zones = []

      this.zonesLoading = true
      const system = this.eventForm.system
      const zonesArr = system.zone_prefixes
      zonesArr.forEach((element) => {
        const item = {
          text: element,
          value: element
        }
        this.zones.push(item)
      })
      this.zonesLoading = false
      this.zonesDisabled = false
    },

    startDatePicked () {
      this.sDateMenu = !this.sDateMenu
    },

    endDatePicked () {
      this.eDateMenu = !this.eDateMenu
    },

    formSubmitted () {
      if (!this.errorStatus) {
        this.formComplete()
      } else {
        this.formError()
      }
    },

    formComplete () {
      this.submitLoading = false
      this.$emit('form-submit', 'success')
    },

    formLoading () {
      this.submitLoading = true
    },

    formError () {
      this.submitLoading = false
      this.$emit('form-submit', 'error')
    },

    mapObject () {
      this.eventForm.id = this.event.id
      this.eventForm.startDate = this.event.start_date
      this.eventForm.endDate = this.event.end_date
      this.eventForm.jira = this.event.jira_case
      this.eventForm.featuresOn = this.event.features_on
      this.eventForm.featuresOff = this.event.features_off
      this.eventForm.compiledSources = this.event.compiled_sources
      this.eventForm.apiUsed = this.event.api_used

      const systemItem = this.systems.find(x =>
        x.text === this.event.machine + ' - ' + this.event.system
      )
      this.eventForm.system = systemItem.value

      this.systemSelected()
      this.eventForm.zones = this.event.zones

      const selEvntTypeArr = []
      const evntTypeArr = this.event.event_types

      evntTypeArr.forEach(element => {
        const evntTypeItem = this.eventtypes.find(x =>
          x.value.code === element
        )
        selEvntTypeArr.push(evntTypeItem.value)
      })

      this.eventForm.eventtypes = selEvntTypeArr
    }
  },

  async mounted () {
    this.systemLoading = true
    this.typesLoading = true

    await Promise.all([
      // this.getSystemsList(),
      // this.getEventTypesList()
      this.GetSystemsList(),
      this.GetEventTypesList()
    ])

    const systemArr = this.systemsList
    systemArr.forEach((element) => {
      const item = {
        text: element.machine + ' - ' + element.global_prefix,
        value: element
      }
      this.systems.push(item)
    })

    const typesArr = this.eventTypesList
    typesArr.forEach((element) => {
      const item = {
        text: element.name,
        value: element
      }
      this.eventtypes.push(item)
    })

    this.systemLoading = false
    this.typesLoading = false

    if (this.mode === 'enquire') this.mapObject()
    if (this.mode === 'update') this.mapObject()
    if (this.mode === 'delete') this.mapObject()
  }
})
</script>
