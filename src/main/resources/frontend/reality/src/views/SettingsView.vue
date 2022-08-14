<template>
  <v-container fluid>
    <v-tabs
      @change="recreateTabs"
    >
      <v-tabs-slider color="primary"></v-tabs-slider>
      <v-tab>Backend Features</v-tab>
      <v-tab>Frontend Features</v-tab>
      <v-tab>Switchable Features</v-tab>

      <v-tab-item :eager="preload">
        <backend-list
          :key="recreateBackendList"
          @item-clicked="itemClicked"
        >
        </backend-list>
      </v-tab-item>

      <v-tab-item :eager="preload">
        <frontend-list
          :key="recreateFrontendList"
        >
        </frontend-list>
      </v-tab-item>

      <v-tab-item :eager="preload">
        <switchable-list
          :key="recreateSwitchableList"
        >
        </switchable-list>
      </v-tab-item>
    </v-tabs>
    <v-dialog
      v-model="settingDialog"
      max-width="600px"
      persistent
      eager
    >
      <setting-dialog
        :settingItem="selectedItem"
        :title="selectedItem.key"
        @close-popup="closeDialog"
      >
      </setting-dialog>
    </v-dialog>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions } from 'vuex'
import BackendList from '@/components/settings/BackendList.vue'
import FrontendList from '@/components/settings/FrontendList.vue'
import SwitchableList from '@/components/settings/SwitchableList.vue'
import SettingDialog from '@/components/settings/SettingDialog.vue'

export default Vue.extend({
  name: 'SettingsView',

  components: {
    BackendList, FrontendList, SwitchableList, SettingDialog
  },

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Settings'
    }
  },

  data () {
    return {
      viewMode: '',
      preload: false,
      recreateBackendList: 0,
      recreateFrontendList: 0,
      recreateSwitchableList: 0,
      settingDialog: false,
      selectedItem: {
        id: 0,
        key: '',
        value: '',
        length: 0,
        type: '',
        desc: '',
        default_value: '',
        valid_values: '',
        added_by: '',
        last_changed_by: '',
        last_changed_date: ''
      }
    }
  },

  methods: {
    ...mapActions([
      'GetBackendList', 'GetFrontendList', 'GetSwitchableList'
    ]),

    async fetchBackendList () {
      await this.GetBackendList()
    },

    async fetchFrontendList () {
      await this.GetFrontendList()
    },

    async fetchSwitchableList () {
      await this.GetSwitchableList()
    },

    recreateTabs () {
      this.recreateBackendList += 1
      this.recreateFrontendList += 1
      this.recreateSwitchableList += 1
    },

    // eslint-disable-next-line
    itemClicked (args: any) {
      this.selectedItem = args
      this.settingDialog = true
    },

    closeDialog () {
      this.settingDialog = false
    }
  },

  async mounted () {
    await Promise.all([
      this.fetchBackendList(),
      this.fetchFrontendList(),
      this.fetchSwitchableList()
    ])
  }
})
</script>
