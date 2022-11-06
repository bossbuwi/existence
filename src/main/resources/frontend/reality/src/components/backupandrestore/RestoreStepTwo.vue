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
      ></v-file-input>
      <v-btn
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
      file: null
    }
  },

  computed: {
    ...mapGetters({
      uploadComplete: 'isSuccess',
      fileUpload: 'getFile'
    })
  },

  methods: {
    ...mapActions([
      'PostFileUpload'
    ]),

    async submitFile () {
      this.isLoading = true
      await this.PostFileUpload(this.file)
    }
  }
})
</script>
