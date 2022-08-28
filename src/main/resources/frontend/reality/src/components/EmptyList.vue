<template>
  <v-card>
    <v-card-text>
      <v-list-item>
        <v-list-item-content>
          <span>If you are seeing this, it means that this section is not yet available.</span>
        </v-list-item-content>
      </v-list-item>
    </v-card-text>
  </v-card>
</template>

<script lang="ts">
import Vue from 'vue'

export default Vue.extend({
  name: 'EmptyList',

  data () {
    return {}
  }
})
</script>
