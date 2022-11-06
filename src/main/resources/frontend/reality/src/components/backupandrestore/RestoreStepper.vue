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
          break
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
    }
  }
})
</script>
