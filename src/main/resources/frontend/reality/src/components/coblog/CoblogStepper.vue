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
        Pre-COB Checklist
      </v-stepper-step>
      <v-divider></v-divider>
      <v-stepper-step
        :error-icon="errorIcon"
        :complete="chapter > 2"
        :rules="[verifySystem]"
        step="2"
      >
        System Selection
        <small v-if="systemHasError">{{ systemErrorText }}</small>
      </v-stepper-step>
      <v-divider></v-divider>
      <v-stepper-step
        :error-icon="errorIcon"
        :rules="[verifyDates]"
        :complete="chapter > 3"
        step="3"
      >
        Runday Input
        <small v-if="dateHasError">{{ dateErrorText }}</small>
      </v-stepper-step>
    </v-stepper-header>
    <v-stepper-items>
      <v-stepper-content step="1">
        <v-container fluid>
          <coblog-step-one></coblog-step-one>
        </v-container>
        <v-btn
          color="primary"
          @click="chapter = 2"
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
          <coblog-step-two></coblog-step-two>
        </v-container>
        <v-btn
          color="primary"
          @click="submitSystem"
        >
          Next
        </v-btn>
        <v-btn text
          @click="previousChapter"
        >
          Previous
        </v-btn>
      </v-stepper-content>

      <v-stepper-content step="3">
        <v-container fluid>
          <coblog-step-three></coblog-step-three>
        </v-container>
        <v-btn
          color="primary"
          @click="stepperComplete"
        >
          Finish
        </v-btn>
        <v-btn text
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
import CoblogStepOne from '@/components/coblog/CoblogStepOne.vue'
import CoblogStepTwo from '@/components/coblog/CoblogStepTwo.vue'
import CoblogStepThree from '@/components/coblog/CoblogStepThree.vue'

export default Vue.extend({
  name: 'CoblogStepper',

  components: {
    CoblogStepOne, CoblogStepTwo, CoblogStepThree
  },

  data () {
    return {
      errorIcon: 'mdi-alert-circle',
      chapter: 1,
      systemStatus: false,
      systemHasError: false,
      systemErrorText: '',
      dateStatus: false,
      dateHasError: false,
      dateErrorText: ''
    }
  },

  computed: {
    ...mapGetters({
      cobSystem: 'getCobSystem',
      runday: 'getRunday',
      nextRunday: 'getNextRunday'
    })
  },

  props: [

  ],

  methods: {
    ...mapMutations([
      'resetCoblogState'
    ]),

    stepperComplete () {
      this.dateStatus = true
      if (this.verifyDates()) {
        this.$emit('stepper-complete')
      }
    },

    cancelStepper () {
      this.resetCoblogState()
      this.$emit('stepper-cancel')
    },

    submitSystem () {
      this.systemStatus = true
      if (this.verifySystem()) {
        this.chapter = 3
      }
    },

    verifySystem () {
      if (this.chapter !== 2) {
        this.systemHasError = false
        this.systemErrorText = ''
        return true
      }
      if (this.chapter === 2 && this.systemStatus === true) {
        if (this.cobSystem.id > 0) {
          this.systemHasError = false
          this.systemErrorText = ''
          return true
        } else {
          this.systemHasError = true
          this.systemErrorText = 'Please select a sytem.'
          return false
        }
      }
      if (this.chapter === 2 && this.systemStatus === false) {
        this.systemHasError = false
        this.systemErrorText = ''
        return true
      }
    },

    verifyDates () {
      if (this.chapter !== 3) {
        this.dateHasError = false
        this.dateErrorText = ''
        return true
      }
      if (this.chapter === 3 && this.dateStatus === true) {
        if (this.runday === '' && this.nextRunday === '') {
          this.dateHasError = true
          this.dateErrorText = 'Please pick the run day and the next run day.'
          return false
        }
        if (this.runday === '') {
          this.dateHasError = true
          this.dateErrorText = 'Please pick the run day.'
          return false
        }
        if (this.nextRunday === '') {
          this.dateHasError = true
          this.dateErrorText = 'Please pick the next run day.'
          return false
        }
        if (this.runday > this.nextRunday || this.runday === this.nextRunday) {
          this.dateHasError = true
          this.dateErrorText = 'The next run day must be later than the run day.'
          return false
        } else {
          this.dateHasError = false
          this.dateErrorText = ''
          return true
        }
      }
      if (this.chapter === 3 && this.dateStatus !== true) {
        this.dateHasError = false
        this.dateErrorText = ''
        return true
      }
    },

    finishStepper () {
      this.dateStatus = true
    },

    previousChapter () {
      switch (this.chapter) {
        case 1:
          this.cancelStepper()
          break
        case 2:
          this.systemStatus = false
          this.systemHasError = false
          this.systemErrorText = ''
          this.chapter = 1
          break
        case 3:
          this.dateStatus = false
          this.chapter = 2
          break
        default:
          break
      }
    }
  }
})
</script>
