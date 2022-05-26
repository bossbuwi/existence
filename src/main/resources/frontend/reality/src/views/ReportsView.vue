<template>
  <div class="reports">
    <h1>This is the reports page</h1>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'

export default Vue.extend({
  name: 'ReportsView',

  components: {},

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Reports'
    }
  }
})
</script>
