<template>
  <v-app-bar
    app
    flat
    dense
    clipped-left
  >
    <v-toolbar-title>{{ appTitle }}</v-toolbar-title>
    <v-spacer></v-spacer>
    <v-menu
      transition="scroll-y-transition"
      :close-on-content-click="false"
      :nudge-bottom="8"
      :nudge-width="200"
      offset-y
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          icon
          v-bind="attrs"
          v-on="on"
        >
          <v-icon>mdi-dots-vertical</v-icon>
        </v-btn>
      </template>
      <login-form />
    </v-menu>
  </v-app-bar>
</template>

<script lang="ts">
import Vue from 'vue'
import LoginForm from './LoginForm.vue'

export default Vue.extend({
  components: { LoginForm },
  name: 'CustomAppBar',

  data () {
    return {
      appTitle: process.env.VUE_APP_NAME
    }
  }
})
</script>
