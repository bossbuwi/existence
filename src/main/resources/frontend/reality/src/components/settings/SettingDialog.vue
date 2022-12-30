<template>
    <v-card
      color="grey lighten-4"
      min-width="350px"
      flat
    >
    <v-toolbar
      color="primary"
      dark
      dense
    >
      <v-btn
        icon
        @click="close"
      >
        <v-icon>mdi-close</v-icon>
      </v-btn>
      <v-toolbar-title>{{ title }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn
        v-if="modifyButtonEnabled"
        outlined
        @click="changeFormMode"
      >MODIFY</v-btn>
      <v-btn
        v-if="!modifyButtonEnabled"
        outlined
        :loading="buttonLoading"
        @click="save()"
      >SAVE</v-btn>
    </v-toolbar>
    <v-card-text>
      <v-row class="mt-5 px-6">
        <v-text-field
          :disabled="modifyButtonEnabled"
          :error="buttonError"
          :error-messages="buttonMessage"
          label="Current Value"
          v-model="setting.value"
          outlined
        >
        </v-text-field>
      </v-row>
      <v-list-item three-line>
        <v-list-item-content>
          <v-list-item-title>Description</v-list-item-title>
          <v-list-item-subtitle>{{ settingItem.desc }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Valid Values</v-list-item-title>
          <v-list-item-subtitle>{{ settingItem.valid_values }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Default Values</v-list-item-title>
          <v-list-item-subtitle>{{ settingItem.default_value }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Max Value Length</v-list-item-title>
          <v-list-item-subtitle>{{ settingItem.length }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Added By</v-list-item-title>
          <v-list-item-subtitle>{{ settingItem.added_by }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Last Changed By</v-list-item-title>
          <v-list-item-subtitle>{{ settingItem.last_changed_by }}</v-list-item-subtitle>
          <v-list-item-subtitle>{{ settingItem.last_changed_date }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-card-text>
  </v-card>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'
import { ValidValues } from '@/models/validvalues'
import { FormMode } from '@/models/formmode'

export default Vue.extend({
  name: 'SettingDialog',

  props: [
    'settingItem',
    'title',
    'settingType'
  ],

  data () {
    return {
      formMode: FormMode.ENQUIRE,
      modifyButtonEnabled: true,
      buttonText: 'Modify',
      buttonLoading: false,
      buttonError: false,
      buttonMessage: '',
      setting: Object.assign({}, this.settingItem)
    }
  },

  computed: {
    ...mapGetters({
      updatedSetting: 'getSetting',
      hasError: 'getErrorStatus'
    })
  },

  methods: {
    ...mapActions([
      'PutSetting', 'SyncSettings', 'SetError'
    ]),

    close () {
      this.$emit('close-popup')
    },

    changeFormMode () {
      if (this.formMode === FormMode.ENQUIRE) {
        this.modifyButtonEnabled = false
        this.formMode = FormMode.UPDATE
      } else if (this.formMode === FormMode.UPDATE) {
        this.modifyButtonEnabled = true
        this.formMode = FormMode.ENQUIRE
      }
    },

    validateInput () {
      const validValues = this.settingItem.valid_values
      const inputValue = this.setting.value.trim()

      if (!inputValue) {
        this.SetError('Invalid input. The value must not be empty or blank spaces.')
      } else if (inputValue.length > this.settingItem.length) {
        this.SetError('Invalid input. The value exceeds the maximum length.')
      } else {
        switch (validValues) {
          case ValidValues.ALPHA:
            if (this.isAlpha(inputValue)) {
              this.save()
            } else {
              this.SetError('Invalid input. The value must be of type alpha.')
            }
            break
          case ValidValues.NUMERIC:
            if (this.isNumeric(inputValue)) {
              this.save()
            } else {
              this.SetError('Invalid input. The value must be of type numeric.')
            }
            break
          case ValidValues.ALPHANUMERIC:
            if (!this.isAlpha(inputValue) && !this.isNumeric(inputValue)) {
              this.save()
            } else {
              this.SetError('Invalid input. The value must be of type alphanumeric.')
            }
            break
          default: {
            const validValuesArr = validValues.split(',')
            if (validValuesArr.indexOf(inputValue) > -1) {
              this.save()
            } else {
              this.SetError('Invalid input. The value must be from the valid values.')
            }
            break
          }
        }
      }
    },

    isAlpha (params: string): boolean {
      for (let i = 0; i < params.length; i++) {
        const code = params.charCodeAt(i)
        if (!(code > 64 && code < 91) &&
        !(code > 96 && code < 123)) {
          return false
        }
      }

      return true
    },

    isNumeric (params: string): boolean {
      for (let i = 0; i < params.length; i++) {
        const code = params.charCodeAt(i)
        if (!(code > 47 && code < 58)) {
          return false
        }
      }

      return true
    },

    async save () {
      this.buttonLoading = true
      await this.PutSetting(this.setting)

      if (this.hasError) {
        this.buttonLoading = false
        this.buttonError = true
        this.buttonMessage = 'Invalid input.'
      } else {
        this.buttonMessage = ''
        this.buttonError = false
        await this.SyncSettings(this.settingType)
        this.buttonLoading = false
        this.changeFormMode()
      }
    }
  }
})
</script>
