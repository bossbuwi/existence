<template>
    <v-container fluid>
      <v-card>
        <v-card-title>Generate Template for Restoring Records</v-card-title>
        <v-card-subtitle>This creates a template for restoring records that can be downloaded and be used for restoring records.</v-card-subtitle>
        <v-card-text>
          <v-form
            :disabled="formDisabled"
            v-if="formVisible"
          >
            <v-row>
              <v-col
                cols="4"
              >
                <v-select
                  :items="models"
                  label="Model to Generate"
                  v-model="selected"
                  @change="itemChanged"
                ></v-select>
              </v-col>
            </v-row>
          </v-form>
          <v-alert
            v-if="!formVisible"
            border="left"
            colored-border
            type="success"
            elevation="2"
          >
            Template generated!
          </v-alert>
          <v-card v-if="!formVisible">
            <v-card-title>File Details</v-card-title>
            <v-card-text>
              <v-list-item two-line>
                <v-list-item-content>
                  <v-list-item-title>Name</v-list-item-title>
                  <v-list-item-subtitle>{{ fileResponse.filename }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item two-line>
                <v-list-item-content>
                  <v-list-item-title>Extension</v-list-item-title>
                  <v-list-item-subtitle>{{ fileResponse.extension }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <!-- <v-list-item two-line>
                <v-list-item-content>
                  <v-list-item-title>MIME Type</v-list-item-title>
                  <v-list-item-subtitle>{{ fileResponse.type }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item> -->
              <v-list-item two-line>
                <v-list-item-content>
                  <v-list-item-title>Size (in bytes)</v-list-item-title>
                  <v-list-item-subtitle>{{ fileResponse.size }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-card-text>
          </v-card>
        </v-card-text>
        <v-card-actions>
          <v-btn
            color="primary"
            text
            :disabled="generateDisabled"
            v-if="!downloadVisible"
            @click="generateTemplate"
          >
            Generate
          </v-btn>
          <v-btn
            color="primary"
            text
            v-if="downloadVisible"
          >
            Download
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'HousekeepingTab',

  data () {
    return {
      models: [
        'Machine', 'System', 'Event'
      ],
      selected: '',
      generateDisabled: true,
      downloadVisible: false,
      formDisabled: false,
      formVisible: true
    }
  },

  computed: {
    ...mapGetters({
      fileResponse: 'getFileResponse',
      processStatus: 'getProcessStatus'
    })
  },

  methods: {
    ...mapActions([
      'PostGenerateTemplate'
    ]),

    itemChanged () {
      this.generateDisabled = false
    },

    async generateTemplate () {
      this.formDisabled = true
      await this.PostGenerateTemplate(this.selected)

      if (this.processStatus) {
        this.downloadVisible = true
        this.formDisabled = false
        this.formVisible = false
      } else {
        this.formDisabled = false
      }
    }
  }
})
</script>
