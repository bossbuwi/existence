<template>
  <v-expansion-panels
    v-model="panel"
  >
    <v-expansion-panel>
      <v-expansion-panel-header
        class="text-h5 font-weight-bold"
        ripple
      >
        <v-col cols="1">
          <v-icon
            large
            color="blue darken-2"
          >
            mdi-briefcase-check-outline
          </v-icon>
        </v-col>
        Rules
      </v-expansion-panel-header>
      <v-expansion-panel-content>
        <v-simple-table class="text-body1 font-weight-medium">
          <tbody>
            <tr
              v-for='item in rules'
              :key='item.id'
            >
              <td>{{ item.body }}</td>
            </tr>
          </tbody>
        </v-simple-table>
      </v-expansion-panel-content>
    </v-expansion-panel>
  </v-expansion-panels>
</template>
<script lang='ts'>
import Vue from 'vue'
export default Vue.extend({
  name: 'RulesPanel',

  data () {
    return {
      panel: 0
    }
  },

  props: [
    'rules'
  ]
})
</script>
