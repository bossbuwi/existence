<template>
  <v-container fluid>
    <form v-if="inputEnabled">
      <v-radio-group
        v-model="recordType"
        label="Select which records to restore:"
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
        @click="importRecords"
      >
        Select
      </v-btn>
    </form>
    <v-alert
      v-if="restoring || success"
      border="left"
      colored-border
      :type="alertType"
      elevation="2"
    >
      {{ alertMessage }}
      <v-progress-linear
        v-if="restoring && !success"
        indeterminate
      ></v-progress-linear>
    </v-alert>
    <v-card v-if="success">
      <v-card-title>Details</v-card-title>
      <v-card-text>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>Record type</v-list-item-title>
            <v-list-item-subtitle>Event</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>Number of records restored</v-list-item-title>
            <v-list-item-subtitle>{{ restoredItems.length }}</v-list-item-subtitle>
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
      restoring: false,
      success: false,
      buttonDisabled: true,
      alertType: '',
      alertMessage: '',
      contents: [
        // { label: 'Machines (not implemented yet)', value: 1 },
        // { label: 'Systems (not implemented yet)', value: 2 },
        { label: 'Events', value: 3 }
        // { label: 'Coblogs (not implemented yet)', value: 4 }
      ]
    }
  },

  computed: {
    ...mapGetters({
      fileUpload: 'getFileResponse',
      restoredItems: 'getRestoredItems',
      hasError: 'getErrorStatus'
    })
  },

  methods: {
    ...mapActions([
      'PostRestoreEvents'
    ]),

    optionSelected () {
      this.buttonDisabled = false
    },

    startProcess () {
      this.inputEnabled = false
      this.restoring = true
      this.alertType = 'info'
      this.alertMessage = 'Restoring records..'
      this.$emit('process-ongoing')
    },

    endProcess () {
      this.restoring = false
      this.success = true
      this.alertType = 'success'
      this.alertMessage = 'Records restored!'
      this.$emit('process-done')
    },

    processError () {
      this.inputEnabled = true
      this.restoring = false
      this.$emit('process-error')
    },

    async importRecords () {
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
          await this.PostRestoreEvents(this.fileUpload.filename)
          break
        case 4:
          console.log('Not implemented yet.')
          break
        default:
          break
      }

      if (this.hasError) {
        this.processError()
      } else {
        this.endProcess()
      }
    }
  }
})
</script>
