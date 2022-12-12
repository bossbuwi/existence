<template>
  <v-container fluid>
    <v-card>
      <v-card-title>Backup Process</v-card-title>
      <v-card-text>
        <v-list-item>
          <v-list-item-content>
            <v-list-item-title>Step 1: Upload a spreadsheet</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item>
          <v-list-item-content>
            <v-list-item-title>Step 2: Select what type of record is contained on the spreadsheet.</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <v-list-item>
          <v-list-item-content>
            <v-list-item-title>Step 3: Profit!</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-card-text>
    </v-card>
    <v-card class="mt-4">
      <v-card-title>Notes</v-card-title>
      <v-card-text>
        <v-list-item three-line>
          <v-list-item-content>
            <v-list-item-title>The maximum size for a spreadsheet upload is 200MB by default.</v-list-item-title>
            <v-list-item-subtitle>If your file is larger than that, please consider moving some records into a new spreadsheet.</v-list-item-subtitle>
            <v-list-item-subtitle class="font-italic">Bigger does not always mean better.</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>Do not navigate to other menus once the restore process has been started.</v-list-item-title>
            <v-list-item-subtitle class="font-italic">Patience is a virtue.</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-list-item three-line>
          <v-list-item-content>
            <v-list-item-title>Records to be restored are not checked for uniqueness.</v-list-item-title>
            <v-list-item-subtitle>Any duplicates resulting from the restoration process may be manually deleted on their respective list views.</v-list-item-subtitle>
            <v-list-item-subtitle class="font-italic">Think before you click.</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-list-item three-line>
          <v-list-item-content>
            <v-list-item-title>Records contained on the spreadsheet must adhere to certain row and column formats for them to be properly restored.</v-list-item-title>
            <v-list-item-subtitle>Sample spreadsheets for every supported record types may be downloaded at the Housekeeping tab.</v-list-item-subtitle>
            <v-list-item-subtitle class="font-italic">Knowledge is power.</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'

export default Vue.extend({
  name: 'RestoreStepOne',

  data () {
    return {

    }
  }
})
</script>
