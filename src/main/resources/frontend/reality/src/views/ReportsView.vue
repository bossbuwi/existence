<template>
  <v-container fluid>
    <v-row>
      <v-col>
        <search-panel>

        </search-panel>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import SearchPanel from '../components/SearchPanel.vue'

export default Vue.extend({
  name: 'ReportsView',

  components: {
    SearchPanel
  },

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Search'
    }
  }
})
</script>
