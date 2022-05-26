<template>
  <v-card
    color="grey lighten-4"
    min-width="350px"
    flat
  >
    <v-toolbar
      :color="color"
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
      <!-- <v-btn icon>
        <v-icon>mdi-dots-vertical</v-icon>
      </v-btn> -->
    </v-toolbar>
    <v-card-text>
      <v-simple-table dense>
        <template v-slot:default>
          <tbody>
            <tr>
              <td>
                System
              </td>
              <td>
                {{ eventItem.system }}
              </td>
            </tr>
            <tr>
              <td>
                Zones
              </td>
              <td>
                {{ eventItem.zones }}
              </td>
            </tr>
            <tr>
              <td>
                Event Types
              </td>
              <td>
                {{ eventItem.event_types }}
              </td>
            </tr>
            <tr>
              <td>
                Start Date
              </td>
              <td>
                {{ eventItem.start_date }}
              </td>
            </tr>
            <tr>
              <td>
                End Date
              </td>
              <td>
                {{ eventItem.end_date }}
              </td>
            </tr>
            <tr>
              <td>
                JIRA Case
              </td>
              <td>
                {{ eventItem.jira_case }}
              </td>
            </tr>
            <tr>
              <td>
                Features On
              </td>
              <td>
                {{ eventItem.features_on }}
              </td>
            </tr>
            <tr>
              <td>
                Features Off
              </td>
              <td>
                {{ eventItem.features_off }}
              </td>
            </tr>
            <tr>
              <td>
                Compiled Sources
              </td>
              <td>
                {{ eventItem.compiled_sources }}
              </td>
            </tr>
            <tr>
              <td>
                API Used
              </td>
              <td>
                {{ eventItem.api_used }}
              </td>
            </tr>
            <tr>
              <td>
                Created By
              </td>
              <td>
                {{ eventItem.created_by }}
              </td>
            </tr>
            <tr>
              <td>
                Creation Date
              </td>
              <td>
                {{ eventItem.creation_date }}
              </td>
            </tr>
          </tbody>
        </template>
      </v-simple-table>
    </v-card-text>
  </v-card>
</template>
<script lang="ts">
import Vue from 'vue'

export default Vue.extend({
  name: 'EventPopup',

  props: [
    'title',
    'color',
    'eventItem'
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
