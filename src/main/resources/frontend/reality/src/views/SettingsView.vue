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
          @item-clicked="itemClicked"
        >
        </frontend-list>
      </v-tab-item>

      <v-tab-item :eager="preload">
        <switchable-list
          :key="recreateSwitchableList"
          @item-clicked="itemClicked"
        >
        </switchable-list>
      </v-tab-item>
    </v-tabs>
    <v-dialog
      v-model="settingDialog"
      max-width="600px"
      :key="dialogKey"
      persistent
    >
      <setting-dialog
        :settingItem="selectedItem"
        :title="selectedItem.key"
        :settingType="selectedItem.type"
        @close-popup="closeDialog"
      >
      </setting-dialog>
    </v-dialog>
  </v-container>
</template>

<script lang="ts">
/**
 * The lists need to be reworked.
 * They are basically the same.
 * The only difference between them is the type of setting that they display.
 * A reusable component should suffice.
 * The whole settings list could be fetched when this view is mounted and filtered
 * to display the relevant types on each of the three list.
 */
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
      dialogKey: 0,
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

    recreateTabs (param: number) {
      switch (param) {
        case 0:
          this.recreateBackendList += 1
          break
        case 1:
          this.recreateFrontendList += 1
          break
        case 2:
          this.recreateSwitchableList += 1
          break
        default:
          break
      }
    },

    // eslint-disable-next-line
    itemClicked (args: any) {
      this.selectedItem = args
      this.dialogKey += 1
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
