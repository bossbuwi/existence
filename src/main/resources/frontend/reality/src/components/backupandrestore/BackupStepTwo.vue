<template>
  <v-container fluid>
    <!-- Alert -->
    <v-alert
      class="mb-8"
      v-if="exporting || success || error"
      border="left"
      colored-border
      :type="alertType"
      elevation="2"
    >
      {{ alertMessage }}<br/>
      {{ subMessage }}
      <v-progress-linear
        v-if="exporting"
        indeterminate
      ></v-progress-linear>
    </v-alert>

    <form v-if="inputEnabled">
      <v-radio-group
        v-model="recordType"
        label="Select which records to backup:"
        :error="error"
        @change="optionSelected"
      >
        <v-radio
          v-for="item in contents"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </v-radio>
      </v-radio-group>
      <v-divider
        class="mb-4 ml-8"
      ></v-divider>
      <v-btn
        class="mr-4"
        :disabled="buttonDisabled"
        @click="exportRecords"
      >
        Select
      </v-btn>
    </form>

    <v-card v-if="success">
      <v-card-title>Details</v-card-title>
      <v-card-text>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>File Name</v-list-item-title>
            <v-list-item-subtitle>{{ exportResponse.filename}}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>File Type</v-list-item-title>
            <v-list-item-subtitle>{{ exportResponse.type}}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>Size (in bytes)</v-list-item-title>
            <v-list-item-subtitle>{{ exportResponse.size}}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'RestoreStepThree',

  data () {
    return {
      recordType: 0,
      inputEnabled: true,
      exporting: false,
      success: false,
      error: false,
      buttonDisabled: true,
      alertType: '',
      alertMessage: '',
      subMessage: '',
      contents: [
        // { label: 'Machines', value: 1 },
        // { label: 'Systems', value: 2 },
        { label: 'Events', value: 3 }
        // { label: 'Coblogs', value: 4 }
      ]
    }
  },

  computed: {
    ...mapGetters({
      exportComplete: 'getProcessStatus',
      exportResponse: 'getFileResponse',
      hasError: 'getErrorStatus'
    })
  },

  methods: {
    ...mapActions([
      'PostExportEvent'
    ]),

    optionSelected () {
      this.buttonDisabled = false
    },

    startProcess () {
      this.inputEnabled = false
      this.error = false
      this.exporting = true
      this.alertType = 'info'
      this.alertMessage = 'Exporting records..'
      this.subMessage = ''
      this.$emit('process-ongoing')
    },

    endProcess () {
      this.exporting = false
      this.success = true
      this.alertType = 'success'
      this.alertMessage = 'Records exported!'
      this.subMessage = ''
      this.$emit('process-done')
    },

    processError () {
      this.error = true
      this.exporting = false
      this.success = false
      this.inputEnabled = true
      this.alertType = 'error'
      this.alertMessage = 'Export error!'
      this.subMessage = 'Please wait for a moment and try again. If the error persists, contact an admin.'
      this.$emit('process-error')
    },

    async exportRecords () {
      this.startProcess()
      switch (this.recordType) {
        case 0:
          console.log('This should really never be selected.')
          break
        case 1:
          console.log('Not implemented yet.')
          break
        case 2:
          console.log('Not implemented yet.')
          break
        case 3:
          await this.PostExportEvent('')
          break
        case 4:
          console.log('Not implemented yet.')
          break
        default:
          break
      }

      if (this.exportComplete) {
        this.endProcess()
      } else {
        this.processError()
      }
    }
  }
})
</script>
