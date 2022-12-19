<template>
  <v-container fluid>
    <form v-if="inputEnabled">
      <v-radio-group
        v-model="recordType"
        label="Select which records to backup:"
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
    <!-- Alert -->
    <v-alert
      v-if="exporting || success"
      border="left"
      colored-border
      :type="alertType"
      elevation="2"
    >
      {{ alertMessage }}
      <v-progress-linear
        v-if="exporting && !success"
        indeterminate
      ></v-progress-linear>
    </v-alert>
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
            <v-list-item-subtitle>Microsoft Office Excel File Format</v-list-item-subtitle>
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
      buttonDisabled: true,
      alertType: '',
      alertMessage: '',
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
      exportComplete: 'exportComplete',
      fileUpload: 'getFile',
      exportResponse: 'getExportResponse'
    })
  },

  methods: {
    ...mapActions([
      'GetExportEvent'
    ]),

    optionSelected () {
      this.buttonDisabled = false
    },

    startExport () {
      this.inputEnabled = false
      this.exporting = true
      this.alertType = 'info'
      this.alertMessage = 'Exporting records..'
      this.$emit('process-ongoing')
    },

    endExport () {
      this.exporting = false
      this.success = true
      this.alertType = 'success'
      this.alertMessage = 'Records exported!'
      this.$emit('process-done')
    },

    exportError () {
      this.exporting = false
      this.success = false
      this.inputEnabled = true
      this.$emit('process-error')
    },

    async exportRecords () {
      this.startExport()
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
          await this.GetExportEvent('')
          if (this.exportComplete) {
            this.endExport()
          } else {
            this.exportError()
          }
          break
        case 4:
          console.log('Not implemented yet.')
          break
        default:
          break
      }
    }
  }
})
</script>
