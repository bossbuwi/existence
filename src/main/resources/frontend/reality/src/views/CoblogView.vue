<template>
  <div class="settings">
    <h1>This is the coblogs page</h1>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'

export default Vue.extend({
  name: 'CoblogView',

  components: {},

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Coblog'
    }
  }
})
</script>
