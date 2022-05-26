<template>
  <div class="settings">
    <h1>This is the settings page</h1>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'

export default Vue.extend({
  name: 'SettingsView',

  components: {},

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Settings'
    }
  }
})
</script>
