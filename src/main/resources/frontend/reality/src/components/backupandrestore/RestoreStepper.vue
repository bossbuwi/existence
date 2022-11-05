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
        :complete="chapter > 3"
        step="3"
      >
        Select Records
      </v-stepper-step>
    </v-stepper-header>
    <v-stepper-items>
      <v-stepper-content step="1">
        <v-container fluid>
          <restore-step-one></restore-step-one>
        </v-container>
        <v-btn
          color="primary"
          @click="nextChapter"
        >
          Next
        </v-btn>
        <v-btn text
          @click="previousChapter"
        >
          Cancel
        </v-btn>
      </v-stepper-content>

      <v-stepper-content step="2">
        <v-container fluid>
          <restore-step-two></restore-step-two>
        </v-container>
        <v-btn
          v-if="nextEnabled || uploadComplete"
          color="primary"
          @click="nextChapter"
        >
          Next
        </v-btn>
        <v-btn text
          v-if="previousEnabled"
          @click="previousChapter"
        >
          Previous
        </v-btn>
      </v-stepper-content>

      <v-stepper-content step="3">
        <v-container fluid>
          <restore-step-three></restore-step-three>
        </v-container>
        <v-btn
          v-if="nextEnabled"
          color="primary"
          @click="stepperComplete"
        >
          Finish
        </v-btn>
        <v-btn text
          v-if="previousEnabled"
          @click="previousChapter"
        >
          Previous
        </v-btn>
      </v-stepper-content>
    </v-stepper-items>
  </v-stepper>
</template>

<script lang="ts">
/* eslint-disable */
import Vue from 'vue'
import { mapActions, mapGetters, mapMutations } from 'vuex'
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
      previousEnabled: true
    }
  },

  computed: {
    ...mapGetters({
      uploadComplete: 'isSuccess',
      ongoingBackupAndRestore: 'isOngoing'
    })
  },

  props: [

  ],

  methods: {
    nextChapter () {
      this.chapter += 1
      this.nextEnabled = false

      switch (this.chapter) {
        case 2:
          if (this.uploadComplete) this.nextEnabled = true
          break
        case 3:
          if (this.ongoingBackupAndRestore) this.previousEnabled = false
        default:
          break
      }
    },

  previousChapter () {
    this.chapter -= 1
    this.nextEnabled = true
  },

  stepperComplete () {
    console.log('complete')
  },

  //   ...mapMutations([
  //     'resetCoblogState'
  //   ]),

  //   stepperComplete () {
  //     this.dateStatus = true
  //     if (this.verifyDates()) {
  //       this.$emit('stepper-complete')
  //     }
  //   },

  //   cancelStepper () {
  //     this.resetCoblogState()
  //     this.$emit('stepper-cancel')
  //   },

  //   submitSystem () {
  //     this.systemStatus = true
  //     if (this.verifySystem()) {
  //       this.chapter = 3
  //     }
  //   },

  //   verifySystem () {
  //     if (this.chapter !== 2) {
  //       this.systemHasError = false
  //       this.systemErrorText = ''
  //       return true
  //     }
  //     if (this.chapter === 2 && this.systemStatus === true) {
  //       if (this.cobSystem.id > 0) {
  //         this.systemHasError = false
  //         this.systemErrorText = ''
  //         return true
  //       } else {
  //         this.systemHasError = true
  //         this.systemErrorText = 'Please select a sytem.'
  //         return false
  //       }
  //     }
  //     if (this.chapter === 2 && this.systemStatus === false) {
  //       this.systemHasError = false
  //       this.systemErrorText = ''
  //       return true
  //     }
  //   },

  //   verifyDates () {
  //     if (this.chapter !== 3) {
  //       this.dateHasError = false
  //       this.dateErrorText = ''
  //       return true
  //     }
  //     if (this.chapter === 3 && this.dateStatus === true) {
  //       if (this.runday === '' && this.nextRunday === '') {
  //         this.dateHasError = true
  //         this.dateErrorText = 'Please pick the run day and the next run day.'
  //         return false
  //       }
  //       if (this.runday === '') {
  //         this.dateHasError = true
  //         this.dateErrorText = 'Please pick the run day.'
  //         return false
  //       }
  //       if (this.nextRunday === '') {
  //         this.dateHasError = true
  //         this.dateErrorText = 'Please pick the next run day.'
  //         return false
  //       }
  //       if (this.runday > this.nextRunday || this.runday === this.nextRunday) {
  //         this.dateHasError = true
  //         this.dateErrorText = 'The next run day must be later than the run day.'
  //         return false
  //       } else {
  //         this.dateHasError = false
  //         this.dateErrorText = ''
  //         return true
  //       }
  //     }
  //     if (this.chapter === 3 && this.dateStatus !== true) {
  //       this.dateHasError = false
  //       this.dateErrorText = ''
  //       return true
  //     }
  //   },

  //   finishStepper () {
  //     this.dateStatus = true
  //   },

  //   previousChapter () {
  //     switch (this.chapter) {
  //       case 1:
  //         this.cancelStepper()
  //         break
  //       case 2:
  //         this.systemStatus = false
  //         this.systemHasError = false
  //         this.systemErrorText = ''
  //         this.chapter = 1
  //         break
  //       case 3:
  //         this.dateStatus = false
  //         this.chapter = 2
  //         break
  //       default:
  //         break
  //     }
  //   }
  }
})
</script>
