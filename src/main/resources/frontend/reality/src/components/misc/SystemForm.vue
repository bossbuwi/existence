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
            id="systemForm"
            :disabled="formDisabled"
          >
            <v-row>
              <span class="text-h6">General Details</span>
            </v-row>
            <v-row>
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <validation-provider
                  v-slot="{ errors }"
                  name="Machine"
                  rules="required"
                >
                  <v-select
                    v-model="systemForm.machine"
                    :items="machines"
                    :loading="machineLoading"
                    :disabled="machineDisabled"
                    label="Machine*"
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
                  name="Global Prefix"
                  rules="required"
                >
                  <v-text-field
                    v-model="systemForm.globalPrefix"
                    @input="systemForm.globalPrefix = systemForm.globalPrefix.toUpperCase()"
                    label="Global Prefix*"
                    type="text"
                    :error-messages="errors"
                  ></v-text-field>
                </validation-provider>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <validation-provider
                  v-slot="{ errors }"
                  name="Release"
                  rules="required"
                >
                  <!-- <v-text-field
                    v-model="systemForm.release"
                    label="Release*"
                    type="text"
                    :error-messages="errors"
                  ></v-text-field> -->
                  <v-select
                    v-model="systemForm.release"
                    :items="releases"
                    :loading="releaseLoading"
                    :disabled="releaseDisabled"
                    label="Release*"
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
                <validation-provider
                  v-slot="{ errors }"
                  name="URL"
                >
                  <v-text-field
                    v-model="systemForm.url"
                    label="URL"
                    type="text"
                    :error-messages="errors"
                  ></v-text-field>
                </validation-provider>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="6"
              >
                <validation-provider
                  v-slot="{ errors }"
                  name="Description"
                >
                  <v-text-field
                    v-model="systemForm.description"
                    label="Description"
                    type="text"
                    :error-messages="errors"
                  ></v-text-field>
                </validation-provider>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="6"
              >
                <validation-provider
                  v-slot="{ errors }"
                  name="Owner"
                >
                  <v-text-field
                    v-model="systemForm.owners"
                    label="Owner"
                    type="text"
                    :error-messages="errors"
                  ></v-text-field>
                </validation-provider>
              </v-col>
            </v-row>
            <v-row>
              <span class="text-h6">Zones</span>
              <v-spacer></v-spacer>
              <v-btn
                fab
                small
                color="primary"
                v-if="!formDisabled"
                @click="addZone"
              >
                <v-icon>
                  mdi-playlist-plus
                </v-icon>
              </v-btn>
            </v-row>
            <v-row>
              <span class="text-subtitle2">Currently has {{ zoneFields.length }}.</span>
            </v-row>
            <v-row
              v-for="(zoneItem, i) in zoneFields"
              :key="i"
              :id="'group' + i"
            >
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <validation-provider
                  v-slot="{ errors }"
                  name="Zone Prefix"
                  rules="required"
                >
                  <v-text-field
                    v-model="zoneItem.prefix"
                    label="Zone Prefix*"
                    type="text"
                    :error-messages="errors"
                  ></v-text-field>
                </validation-provider>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="4"
              >
                <validation-provider
                  v-slot="{ errors }"
                  name="Zone Name"
                  rules="required"
                >
                  <v-text-field
                    v-model="zoneItem.name"
                    label="Zone Name*"
                    type="text"
                    :error-messages="errors"
                  ></v-text-field>
                </validation-provider>
              </v-col>
              <v-btn
                small
                color="primary"
                v-if="!formDisabled"
                @click="removeZone(i)"
              >
                <v-icon>
                  mdi-delete-outline
                </v-icon>
              </v-btn>
            </v-row>
          </v-form>
          <v-row class="my-4">
            <small v-if="!formDisabled">*indicates required field</small>
          </v-row>
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
import { ValidationObserver, ValidationProvider } from 'vee-validate'

export default Vue.extend({
  name: 'SystemForm',

  components: {
    ValidationObserver, ValidationProvider
  },

  props: [
    'mode', 'title', 'titleBarColor'
  ],

  data () {
    return {
      submitLoading: false,
      machines: [],
      machineLoading: false,
      machineDisabled: false,
      releases: [],
      releaseLoading: false,
      releaseDisabled: false,
      systemForm: {
        machine: '',
        globalPrefix: '',
        release: '',
        url: '',
        description: '',
        owners: ''
      },
      zoneFields: [

      ]
    }
  },

  computed: {
    ...mapGetters({
      machineList: 'getMachineList',
      releaseList: 'getReleasesList',
      errorStatus: 'getErrorStatus'
    }),
    submitEnabled: {
      get () {
        if (this.mode === 'complete') return false
        if (this.mode === 'create') return true
        if (this.mode === 'update') return true
        if (this.mode === 'delete') return true
        return false
        // return true
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
      'GetMachinesList', 'GetReleasesList', 'PostFullSystem'
    ]),

    async getMachineList () {
      this.machineLoading = true
      await this.GetMachinesList()
      this.machineLoading = false
    },

    async getReleasesList () {
      this.releaseLoading = true
      await this.GetReleasesList()
      this.releaseLoading = false
    },

    close () {
      this.$emit('close-form')
    },

    addZone () {
      this.zoneFields.push({
        prefix: '',
        name: ''
      })
    },

    removeZone (parm) {
      this.zoneFields.splice(parm, 1)
    },

    async submit () {
      this.formLoading()

      const zonesData = []
      this.zoneFields.forEach(element => {
        const zone = {
          zonal_prefix: '',
          zone_name: ''
        }
        zone.zonal_prefix = element.prefix
        zone.zone_name = element.name
        zonesData.push(zone)
      })

      const fullSystemData = {}
      fullSystemData.machine = this.systemForm.machine.name
      fullSystemData.global_prefix = this.systemForm.globalPrefix
      fullSystemData.release_id = this.systemForm.release.id
      fullSystemData.url = this.systemForm.url
      fullSystemData.description = this.systemForm.description
      fullSystemData.owners = this.systemForm.owners
      fullSystemData.zones = zonesData

      switch (this.mode) {
        case 'create':
          await this.PostFullSystem(fullSystemData)
          this.formSubmitted()
          break
        case 'update':
          // await put model
          this.formSubmitted()
          break
        case 'delete':
          this.formDisabled = false
          // await delete model
          this.formSubmitted()
          break
        default:
          break
      }
    },

    formLoading () {
      this.submitLoading = true
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

    formError () {
      this.submitLoading = false
      this.$emit('form-submit', 'error')
    }
  },

  async mounted () {
    await Promise.all([
      this.getMachineList(),
      this.getReleasesList()
    ])

    this.machineLoading = true
    const machineArr = this.machineList
    machineArr.forEach(element => {
      const item = {
        text: element.name,
        value: element
      }
      this.machines.push(item)
    })
    this.machineLoading = false

    this.releaseLoading = true
    const releaseArr = this.releaseList
    releaseArr.forEach(element => {
      const item = {
        text: element.name,
        value: element
      }
      this.releases.push(item)
    })
    this.releaseLoading = false
  }
})
</script>
