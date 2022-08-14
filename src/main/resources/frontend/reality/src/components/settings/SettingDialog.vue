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
    </v-toolbar>
    <v-card-text>
      <v-list-item three-line>
        <v-list-item-content>
          <v-list-item-title>Description</v-list-item-title>
          <v-list-item-subtitle>{{ settingItem.desc }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Current Value</v-list-item-title>
          <v-list-item-subtitle>{{ settingItem.value }}</v-list-item-subtitle>
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

export default Vue.extend({
  name: 'SettingDialog',

  props: [
    'settingItem',
    'title'
  ],

  data () {
    return {
    }
  },

  methods: {
    close () {
      this.$emit('close-popup')
    }
  }
})
</script>
