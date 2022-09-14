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
          <v-list-item-title>Username</v-list-item-title>
          <v-list-item-subtitle>{{ userItem.username }}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item two-line
        v-if="this.userItem.roles.length > 0"
      >
        <v-list-item-content>
          <v-list-item-title>Roles</v-list-item-title>
          <v-list-item-subtitle>{{ roles }}</v-list-item-subtitle>
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
  name: 'UserPopup',

  props: [
    'userItem',
    'title',
    'color'
  ],

  data () {
    return {}
  },

  computed: {
    roles: {
      get () {
        let rolesArr = [] as string[]
        if (this.userItem.roles === undefined) {
          return 'None'
        } else {
          rolesArr = this.userItem.roles as string[]
          return rolesArr.join(', ')
        }
      }
    }
  },

  methods: {
    close () {
      this.$emit('close-popup')
    }
  }
})
</script>
