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
    <span v-if="uploadComplete">File details in a card must be here.</span>
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
