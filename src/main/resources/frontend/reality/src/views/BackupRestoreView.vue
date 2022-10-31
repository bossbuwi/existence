<template>
  <v-container fluid>
    <v-expansion-panels>
      <v-expansion-panel>
        <v-expansion-panel-header
          class="text-h5 font-weight-bold"
          ripple
        >
          <v-col cols="1">
            <v-icon
              large
              color="blue darken-2"
            >
              mdi-cloud-download-outline
            </v-icon>
          </v-col>
            Backup
        </v-expansion-panel-header>
        <v-expansion-panel-content>
          Hello world
        </v-expansion-panel-content>
      </v-expansion-panel>
      <v-expansion-panel>
        <v-expansion-panel-header
          class="text-h5 font-weight-bold"
          ripple
        >
          <v-col cols="1">
            <v-icon
              large
              color="blue darken-2"
            >
              mdi-cloud-upload-outline
            </v-icon>
          </v-col>
            Restore
        </v-expansion-panel-header>
        <v-expansion-panel-content>
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
            type="info"
            elevation="2"
          >
            Upload complete!
          </v-alert>
          <form v-if="uploadComplete">
            <v-radio-group
              label="Select which records to restore:"
              dense
            >
              <v-radio
                v-for="item in contents"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </v-radio>
            </v-radio-group>
            <v-divider class="mb-4 mx-8"></v-divider>
            <v-btn
              class="mr-4"
            >
              OK
            </v-btn>
            <v-btn>
              Cancel
            </v-btn>
          </form>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'BackupRestoreView',

  components: {

  },

  data () {
    return {
      isLoading: false,
      file: null,
      contents: [
        { label: 'Machines (not implemented yet)', value: 1 },
        { label: 'Systems (not implemented yet)', value: 2 },
        { label: 'Events', value: 3 },
        { label: 'Coblogs (not implemented yet)', value: 4 }
      ]
    }
  },

  computed: {
    ...mapGetters({
      isAuth: 'isAuthenticated',
      user: 'getUserState',
      switchableList: 'getSwitchableList',
      uploadComplete: 'isSuccess'
    })
  },

  methods: {
    ...mapActions([
      'GetSwitchableList', 'PostFileUpload'
    ]),

    async submitFile () {
      this.isLoading = true
      await this.PostFileUpload(this.file)
      console.log(this.uploadComplete)
      this.isLoading = false
    }
  }
})
</script>
