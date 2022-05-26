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
                  <v-text-field
                    v-model="systemForm.release"
                    label="Release*"
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
                    v-model="systemForm.owner"
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
              <!-- <v-btn
                small
                color="primary"
                @click="removeZone(i)"
              >
                <v-icon>
                  mdi-delete-outline
                </v-icon>
              </v-btn> -->
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
import { ValidationObserver, ValidationProvider } from 'vee-validate'

export default Vue.extend({
  name: 'SystemForm',

  components: {
    ValidationObserver,
    ValidationProvider
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
      systemForm: {
        machine: '',
        globalPrefix: '',
        release: '',
        url: '',
        description: ''
      },
      zoneFields: [

      ]
    }
  },

  computed: {
    ...mapGetters({
      machineList: 'getMachineList'
    }),
    submitEnabled: {
      get () {
        if (this.mode === 'complete') return false
        if (this.mode === 'create') return true
        if (this.mode === 'update') return true
        if (this.mode === 'delete') return true
        // return false
        return true
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
      'GetMachinesList'
    ]),

    async getMachineList () {
      this.machineLoading = true
      await this.GetMachinesList()
      this.machineLoading = false
    },

    close () {
      this.$emit('close-popup')
    },

    addZone () {
      this.zoneFields.push({
        prefix: '',
        name: ''
      })
    },

    removeZone (parm) {
      console.log(parm)
    }
  },

  async mounted () {
    await Promise.all([
      this.getMachineList()
    ])

    const machineArr = this.machineList
    machineArr.forEach(element => {
      const item = {
        text: element.name,
        value: element
      }
      this.machines.push(item)
    })
  }
})
</script>
