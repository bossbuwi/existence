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
          <v-list-item-title>Name</v-list-item-title>
          <v-list-item-subtitle>{{ releaseItem.name }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line
        v-if="this.releaseItem.system_count > 0"
      >
        <v-list-item-content>
          <v-list-item-title>Number of Systems</v-list-item-title>
          <v-list-item-subtitle>{{ releaseItem.system_count }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <!-- <v-list-item three-line>
        <v-list-item-content>
          <v-list-item-title>Systems</v-list-item-title>
          <v-list-item-subtitle>{{ systems }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item> -->
    </v-card-text>
  </v-card>
</template>

<script lang="ts">
import Vue from 'vue'

export default Vue.extend({
  name: 'ReleasePopup',

  props: [
    'releaseItem',
    'title',
    'color'
  ],

  data () {
    return {}
  },

  // computed: {
  //   systems: {
  //     get () {
  //       let sysArr = [] as string[]
  //       if (this.releaseItem.systems === undefined) {
  //         return 'None'
  //       } else {
  //         sysArr = this.releaseItem.systems as string[]
  //         return sysArr.join(', ')
  //       }
  //     }
  //   }
  // },

  methods: {
    close () {
      this.$emit('close-popup')
    }
  }
})
</script>
