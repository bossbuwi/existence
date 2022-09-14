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
            id="releaseForm"
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
                v-if="mode === 'update' || releaseForm.newName !== ''"
              >
                <validation-provider
                  v-slot="{ errors }"
                  name="Name"
                  rules="required"
                >
                  <v-text-field
                    v-model="releaseForm.newName"
                    @input="releaseForm.newName = releaseForm.newName.toUpperCase()"
                    label="New Name*"
                    type="text"
                    :error-messages="errors"
                  ></v-text-field>
                </validation-provider>
              </v-col>
              <v-col
                cols="12"
                sm="6"
                md="4"
                v-if="mode !== 'update' && releaseForm.newName === ''"
              >
                <validation-provider
                  v-slot="{ errors }"
                  name="Name"
                  rules="required"
                >
                  <v-text-field
                    v-model="releaseForm.name"
                    @input="releaseForm.name = releaseForm.name.toUpperCase()"
                    label="Name*"
                    type="text"
                    :error-messages="errors"
                  ></v-text-field>
                </validation-provider>
              </v-col>
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
  name: 'ReleaseForm',

  components: {
    ValidationObserver, ValidationProvider
  },

  props: [
    'mode', 'title', 'titleBarColor'
  ],

  data () {
    return {
      submitLoading: false,
      releaseForm: {
        id: 0,
        name: '',
        newName: ''
      }
    }
  },

  computed: {
    ...mapGetters({
      errorStatus: 'getErrorStatus',
      release: 'getRelease'
    }),

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
      'PostRelease', 'PutMachine', 'DeleteMachine'
    ]),

    close () {
      this.$emit('close-form')
    },

    mapObject () {
      this.releaseForm.name = this.release.name
      this.releaseForm.newName = this.release.name
    },

    async submit () {
      this.formLoading()

      const releaseData = {}
      if (this.mode !== 'create') {
        releaseData.id = this.release.id
        releaseData.name = this.release.name
        releaseData.new_name = this.releaseForm.newName
      }

      releaseData.name = this.releaseForm.name

      switch (this.mode) {
        case 'create':
          await this.PostRelease(releaseData)
          this.formSubmitted()
          break
        case 'update':
          // await this.PutMachine(releaseData)
          this.formSubmitted()
          break
        case 'delete':
          // await this.DeleteMachine(releaseData.id)
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

  mounted () {
    if (this.mode === 'enquire') this.mapObject()
    if (this.mode === 'update') this.mapObject()
    if (this.mode === 'delete') this.mapObject()
  }
})
</script>
