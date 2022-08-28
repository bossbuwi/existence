<template>
  <v-card>
    <v-card-text>
      <v-form>
        <v-row>
          <v-col cols="6">
            <v-menu
              v-model="rundayMenu"
              :close-on-content-click="false"
              :nudge-right="40"
              transition="scale-transition"
              offset-y
              min-width="auto"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="cobLog.runday"
                  label="Run Day*"
                  prepend-icon="mdi-calendar"
                  readonly
                  v-bind="attrs"
                  v-on="on"
                ></v-text-field>
              </template>
                <v-date-picker
                  no-title
                  v-model="cobLog.runday"
                  @input="rundayPicked"
                ></v-date-picker>
            </v-menu>
          </v-col>
          <v-col cols="6">
            <v-menu
              v-model="nextRundayMenu"
              :close-on-content-click="false"
              :nudge-right="40"
              transition="scale-transition"
              offset-y
              min-width="auto"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="cobLog.nextRunday"
                  label="Next Run Day*"
                  prepend-icon="mdi-calendar"
                  readonly
                  v-bind="attrs"
                  v-on="on"
                ></v-text-field>
              </template>
                <v-date-picker
                  no-title
                  v-model="cobLog.nextRunday"
                  @input="nextRundayPicked"
                ></v-date-picker>
            </v-menu>
          </v-col>
        </v-row>
      </v-form>
    </v-card-text>
  </v-card>
</template>

<script lang="ts">
/* eslint-disable */
import Vue from 'vue'
import { mapActions, mapGetters, mapMutations } from 'vuex'

export default Vue.extend({
  name: 'CoblogStepThree',

  data () {
    return {
      rundayMenu: false,
      nextRundayMenu: false,
      cobLog: {
        runday: '',
        nextRunday: ''
      }
    }
  },

  computed: {
    ...mapGetters({
      runday: 'getRunday',
      nextRunday: 'getNextRunday'
    })
  },

  methods: {
    ...mapMutations([
      'setCobRunday', 'setCobNextRunday'
    ]),

    rundayPicked () {
      this.rundayMenu = !this.rundayMenu
      this.setCobRunday(this.cobLog.runday)
    },

    nextRundayPicked () {
      this.nextRundayMenu = !this.nextRundayMenu
      this.setCobNextRunday(this.cobLog.nextRunday)
    }
  }
})
</script>
