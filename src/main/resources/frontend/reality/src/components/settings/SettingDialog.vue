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
        outlined
        :loading="buttonLoading"
        @click="modifyToggle"
      >
        {{ buttonText }}
      </v-btn>
    </v-toolbar>
    <v-card-text>
      <v-row class="mt-5 px-6">
        <v-text-field
          :disabled="!modifyMode"
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

export default Vue.extend({
  name: 'SettingDialog',

  props: [
    'settingItem',
    'title',
    'settingType'
  ],

  data () {
    return {
      modifyMode: false,
      buttonText: 'Modify',
      buttonLoading: false,
      // setting: {
      //   id: 0,
      //   key: '',
      //   value: ''
      // }
      setting: Object.assign({}, this.settingItem)
    }
  },

  computed: {
    ...mapGetters({
      updatedSetting: 'getSetting'
    })
  },

  methods: {
    ...mapActions([
      'PutSetting', 'SyncSettings'
    ]),

    close () {
      this.$emit('close-popup')
    },

    modifyToggle () {
      this.modifyMode = !this.modifyMode
      // this.modifyMode ? this.buttonText = 'Save' : this.buttonText = 'Modify'
      if (this.modifyMode) {
        // this.buttonLoading = false
        this.buttonText = 'Save'
      } else {
        // this.buttonLoading = true
        this.save()
        this.buttonText = 'Modify'
      }
    },

    async save () {
      this.buttonLoading = true
      await this.PutSetting(this.setting)
      await this.SyncSettings(this.settingType)
      this.buttonLoading = false
    }
  }
})
</script>
