<template>
  <v-container fluid>
    <v-tabs>
      <v-tabs-slider color="primary"></v-tabs-slider>
      <v-tab>Backup</v-tab>
      <v-tab>Restore</v-tab>
      <v-tab>Housekeeping</v-tab>

      <v-tab-item>
        <v-container fluid>
          <backup-stepper></backup-stepper>
        </v-container>
      </v-tab-item>
      <v-tab-item>
        <v-container fluid>
          <restore-stepper></restore-stepper>
        </v-container>
      </v-tab-item>
      <v-tab-item>
        <empty-list></empty-list>
      </v-tab-item>
    </v-tabs>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'
import EmptyList from '@/components/EmptyList.vue'
import RestoreStepper from '@/components/backupandrestore/RestoreStepper.vue'
import BackupStepper from '@/components/backupandrestore/BackupStepper.vue'

export default Vue.extend({
  name: 'BackupRestoreView',

  components: {
    RestoreStepper, BackupStepper, EmptyList
  },

  data () {
    return {
      selectedTab: 0,
      isLoading: false,
      file: null,
      recordType: 0,
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
      uploadComplete: 'isSuccess',
      fileUpload: 'getFile'
    })
  },

  methods: {
    ...mapActions([
      'GetSwitchableList', 'PostFileUpload', 'PostRestoreEvents', 'GetFileDownload'
    ]),

    async submitFile () {
      this.isLoading = true
      await this.PostFileUpload(this.file)
      console.log(this.uploadComplete)
      this.isLoading = false
    },

    async restoreRecords () {
      console.log(this.recordType)
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
    },

    async test () {
      this.GetFileDownload('backup_20221212_1035.xlsx')
    }
  }
})
</script>
