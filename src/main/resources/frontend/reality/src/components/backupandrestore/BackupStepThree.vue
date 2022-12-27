<template>
  <v-container fluid>
    <v-card v-if="exportComplete">
      <v-card-title>File Details</v-card-title>
      <v-card-text>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>Name</v-list-item-title>
            <v-list-item-subtitle>{{ exportResponse.filename }}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>File Type</v-list-item-title>
            <v-list-item-subtitle>{{ exportResponse.type }}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>Size (in bytes)</v-list-item-title>
            <v-list-item-subtitle>{{ exportResponse.size }}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
      </v-card-text>
    </v-card>
    <v-btn
      class="mt-8"
      @click="downloadFile"
    >
      Download Backup
    </v-btn>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'RestoreStepTwo',

  data () {
    return {
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
      'GetFileDownload'
    ]),

    async downloadFile () {
      this.processStart()
      await this.GetFileDownload(this.exportResponse.filename)
      this.processEnd()
    },

    processStart () {
      this.$emit('process-ongoing')
    },

    processEnd () {
      this.$emit('process-done')
    }
  }
})
</script>
