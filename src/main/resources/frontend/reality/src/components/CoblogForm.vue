<template>
  <v-card>
    <v-toolbar
      dark
      dense
      color="primary"
    >
      <v-btn
        icon
        dark
        @click="close"
      >
        <v-icon>mdi-close</v-icon>
      </v-btn>
    </v-toolbar>
    <v-card>
      <v-card-title>Initializing COB Log</v-card-title>
      <v-card-text v-if="stepperEnabled">
        <coblog-stepper
          @stepper-complete="stepperComplete"
          @stepper-cancel="stepperCancel"
        >
        </coblog-stepper>
      </v-card-text>
      <v-card-text v-if="!stepperEnabled">
        <v-row>
          <v-col cols="2">
            Runday
          </v-col>
          <v-col cols="auto">
            {{ runday }}
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="2">
            Next Runday
          </v-col>
          <v-col cols="auto">
            {{ nextRunday }}
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-card>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters, mapMutations } from 'vuex'
import CoblogStepper from '@/components/coblog/CoblogStepper.vue'
import ComponentForm from '@/components/ComponentForm.vue'

export default Vue.extend({
  name: 'CoblogForm',

  components: {
    CoblogStepper
  },

  data () {
    return {
      stepperEnabled: false
    }
  },

  props: [
    'mode'
  ],

  computed: {
    ...mapGetters({
      runday: 'getRunday',
      nextRunday: 'getNextRunday',
      system: 'getCobSystem'
    })
  },

  methods: {
    ...mapMutations([
      'setCobState', 'resetCoblogState'
    ]),

    close () {
      this.$emit('form-close')
    },

    stepperComplete () {
      this.stepperEnabled = false
      this.setCobState(true)
    },

    stepperCancel () {
      this.$emit('form-close')
    }
  },

  async mounted () {
    if (this.mode === 'create') {
      this.stepperEnabled = true
      this.resetCoblogState()
    }
  }
})
</script>
