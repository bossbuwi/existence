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
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Machine</v-list-item-title>
          <v-list-item-subtitle>{{ systemItem.machine }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Global Prefix</v-list-item-title>
          <v-list-item-subtitle>{{ systemItem.global_prefix }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Zones</v-list-item-title>
          <v-list-item-subtitle>{{ systemItem.zones }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Release</v-list-item-title>
          <v-list-item-subtitle>{{ systemItem.release }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>URL</v-list-item-title>
          <v-list-item-subtitle>{{ systemItem.url }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Description</v-list-item-title>
          <v-list-item-subtitle>{{ systemItem.description }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line>
        <v-list-item-content>
          <v-list-item-title>Owners</v-list-item-title>
          <v-list-item-subtitle>{{ systemItem.owners }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-card-text>
  </v-card>
</template>
<script lang="ts">
import Vue from 'vue'

export default Vue.extend({
  name: 'SystemPopup',

  props: [
    'systemItem',
    'title',
    'color'
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
