<template>
  <v-container fluid>
    <form v-if="!uploadComplete">
      <v-file-input
        :loading="isLoading"
        :disabled="isLoading"
        show-size
        label="Select spreadsheet for upload.."
        accept=".xlsx,.xls"
        chips
        v-model="file"
        @change="fileChanged"
      ></v-file-input>
      <v-btn
        :disabled="buttonDisabled"
        :loading="isLoading"
        @click="submitFile"
      >
        Upload
      </v-btn>
    </form>
    <v-alert
      v-if="uploadComplete"
      border="left"
      colored-border
      type="success"
      elevation="2"
    >
      Upload complete!
    </v-alert>
    <v-card v-if="uploadComplete">
      <v-card-title>File Details</v-card-title>
      <v-card-text>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>Name</v-list-item-title>
            <v-list-item-subtitle>{{ fileUpload.filename }}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>Extension</v-list-item-title>
            <v-list-item-subtitle>{{ fileUpload.extension }}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>MIME Type</v-list-item-title>
            <v-list-item-subtitle>{{ fileUpload.type }}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>Size (in bytes)</v-list-item-title>
            <v-list-item-subtitle>{{ fileUpload.size }}</v-list-item-subtitle>
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
  name: 'RestoreStepTwo',

  data () {
    return {
      isLoading: false,
      buttonDisabled: true,
      file: null
    }
  },

  computed: {
    ...mapGetters({
      uploadComplete: 'uploadComplete',
      fileUpload: 'getFile'
    })
  },

  methods: {
    ...mapActions([
      'PostFileUpload'
    ]),

    fileChanged () {
      if (this.file === null) {
        this.buttonDisabled = true
      } else {
        this.buttonDisabled = false
      }
    },

    async submitFile () {
      this.isLoading = true
      this.$emit('process-ongoing')
      await this.PostFileUpload(this.file)
      this.$emit('process-done')
    }
  }
})
</script>
