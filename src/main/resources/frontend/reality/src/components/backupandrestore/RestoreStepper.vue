<template>
  <v-stepper
    v-model="chapter"
    outlined
  >
    <v-stepper-header>
      <v-stepper-step
        :error-icon="errorIcon"
        :complete="chapter > 1"
        step="1"
      >
        Important Notes
      </v-stepper-step>
      <v-divider></v-divider>
      <v-stepper-step
        :error-icon="errorIcon"
        :complete="chapter > 2"
        step="2"
      >
        Upload File
      </v-stepper-step>
      <v-divider></v-divider>
      <v-stepper-step
        :error-icon="errorIcon"
        :complete="stepperComplete"
        step="3"
      >
        Select Records
      </v-stepper-step>
    </v-stepper-header>
    <v-stepper-items>

      <!-- Important Notes -->
      <v-stepper-content step="1">
        <v-container fluid>
          <restore-step-one></restore-step-one>
        </v-container>
        <v-btn
          class="mr-4"
          color="primary"
          @click="nextChapter"
        >
          Next
        </v-btn>
      </v-stepper-content>

      <!-- Upload File -->
      <v-stepper-content step="2">
        <v-container fluid>
          <restore-step-two
            @process-ongoing="processStart"
            @process-done="processStop"
          ></restore-step-two>
        </v-container>
        <v-btn
          class="mr-4"
          v-if="nextEnabled"
          color="primary"
          @click="nextChapter"
        >
          Next
        </v-btn>
        <v-btn text
          v-if="!uploadComplete"
          :disabled="buttonDisabled"
          @click="previousChapter"
        >
          Previous
        </v-btn>
      </v-stepper-content>

      <!-- Restore Events -->
      <v-stepper-content step="3">
        <v-container fluid>
          <restore-step-three
            @process-ongoing="processStart"
            @process-done="processStop"
          ></restore-step-three>
        </v-container>
        <v-btn text
          v-if="previousEnabled"
          :disabled="buttonDisabled"
          @click="previousChapter"
        >
          Previous
        </v-btn>
      </v-stepper-content>
    </v-stepper-items>
  </v-stepper>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapGetters } from 'vuex'
import RestoreStepOne from '@/components/backupandrestore/RestoreStepOne.vue'
import RestoreStepTwo from '@/components/backupandrestore/RestoreStepTwo.vue'
import RestoreStepThree from '@/components/backupandrestore/RestoreStepThree.vue'

export default Vue.extend({
  name: 'RestoreStepper',

  components: {
    RestoreStepOne, RestoreStepTwo, RestoreStepThree
  },

  data () {
    return {
      errorIcon: 'mdi-alert-circle',
      chapter: 1,
      nextEnabled: true,
      previousEnabled: true,
      buttonDisabled: false,
      stepperComplete: false
    }
  },

  computed: {
    ...mapGetters({
      uploadComplete: 'uploadComplete'
    })
  },

  props: [

  ],

  methods: {
    nextChapter () {
      this.chapter += 1
      this.nextEnabled = false
    },

    previousChapter () {
      this.chapter -= 1
      this.nextEnabled = true
    },

    processStart () {
      this.buttonDisabled = true
    },

    processStop () {
      this.buttonDisabled = false
      this.nextEnabled = true

      switch (this.chapter) {
        case 1:
          break
        case 2:
          break
        case 3:
          this.previousEnabled = false
          this.stepperComplete = true
          break
        default:
          break
      }
    }
  }
})
</script>
