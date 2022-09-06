<template>
  <v-container fluid>
    <v-tabs
      @change="recreateTabs"
      v-model="selectedTab"
    >
      <v-tabs-slider color="primary"></v-tabs-slider>
      <v-tab>Machines</v-tab>
      <v-tab>Systems</v-tab>
      <v-tab>Zones</v-tab>
      <v-tab>Releases</v-tab>
      <v-tab>Rules</v-tab>
      <v-tab>Users</v-tab>

      <v-tab-item :eager="preload">
        <machine-list
          :key="machineTabKey"
          @open-form="openForm"
          @item-clicked="itemClicked"
          @edit-item="editItem"
          @delete-item="deleteItem"
        >
        </machine-list>
      </v-tab-item>

      <v-tab-item :eager="preload">
        <system-list
          :key="systemTabKey"
          @open-form="openForm"
          @item-clicked="itemClicked"
          @edit-item="editItem"
          @delete-item="deleteItem"
        >
        </system-list>
      </v-tab-item>

      <v-tab-item>
        <empty-list></empty-list>
      </v-tab-item>

      <v-tab-item>
        <empty-list></empty-list>
      </v-tab-item>

      <v-tab-item>
        <empty-list></empty-list>
      </v-tab-item>

      <v-tab-item>
        <empty-list></empty-list>
      </v-tab-item>
    </v-tabs>
    <!-- New Form -->
    <v-dialog
      v-model="newForm"
      :key="newFormKey"
      persistent
      fullscreen
    >
      <machine-form
        v-if="selectedTab == 0"
        :mode="formMode"
        :titleBarColor="formColor"
        :title="formTitle"
        @close-form="closeForm"
        @form-submit="formSubmitted"
      >
      </machine-form>
      <system-form
        v-if="selectedTab == 1"
        :mode="formMode"
        :titleBarColor="formColor"
        :title="formTitle"
        @close-form="closeForm"
        @form-submit="formSubmitted"
      >
      </system-form>
    </v-dialog>

    <!-- Item Details Popup -->
    <v-dialog
      v-model="itemPopup"
      :key="itemPopupKey"
      max-width="600px"
      persistent
    >
      <machine-popup
        v-if="selectedTab == 0"
        :machineItem="selectedMachine"
        @close-popup="closePopup"
      >
      </machine-popup>

      <system-popup
        v-if="selectedTab == 1"
        :systemItem="selectedSystem"
        @close-popup="closePopup"
      >
      </system-popup>
    </v-dialog>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions } from 'vuex'
import EmptyList from '@/components/EmptyList.vue'
import SystemList from '@/components/misc/SystemList.vue'
import SystemForm from '@/components/misc/SystemForm.vue'
import SystemPopup from '@/components/misc/SystemPopup.vue'
import MachineList from '@/components/misc/MachineList.vue'
import MachineForm from '@/components/misc/MachineForm.vue'
import MachinePopup from '@/components/misc/MachinePopup.vue'

export default Vue.extend({
  name: 'MiscView',

  components: {
    EmptyList, SystemList, SystemForm, SystemPopup, MachineList, MachineForm, MachinePopup
  },

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Miscellaneous'
    }
  },

  props: [
    'tabNumber'
  ],

  data () {
    return {
      selectedTab: 0,
      preload: false,
      machineTabKey: 0,
      systemTabKey: 0,
      formTitle: '',
      formColor: '',
      formMode: '',
      itemPopup: false,
      itemPopupKey: 100,
      newBtnShown: true,
      newForm: false,
      newFormKey: 0,
      selectedMachine: {
        name: '',
        system_count: 0,
        systems: ''
      },
      selectedSystem: {
        global_prefix: '',
        release: '',
        url: '',
        machine: '',
        zones: '',
        owners: ''
      }
    }
  },

  methods: {
    /* Vuex Methods */
    ...mapActions([
      'SetMachine', 'SetSystem'
    ]),

    /* Tab Methods */
    recreateTabs (param: number) {
      switch (param) {
        case 0:
          this.machineTabKey += 1
          break
        case 1:
          this.systemTabKey += 1
          break
        case 2:
          break
        case 3:
          break
        case 4:
          break
        default:
          break
      }
    },

    /* Form Methods */
    openForm () {
      switch (this.selectedTab) {
        case 0:
          this.newMachine()
          break
        case 1:
          this.newSystem()
          break
        default:
          break
      }
    },

    closeForm () {
      this.newForm = false
      this.newFormKey += 1
      this.recreateTabs(this.selectedTab)
    },

    formSubmitted (param: string) {
      if (param === 'success') {
        this.completeMode()
      } else if (param === 'error') {
        this.errorMode()
      }
    },

    /* Form Modes */
    async createMode () {
      this.formTitle = 'New System'
      this.formColor = 'primary'
      this.newFormKey += 1
      this.formMode = 'create'
    },

    // eslint-disable-next-line
    async updateMode (param: any) {
      this.formMode = 'update'
      this.formColor = 'accent'
      this.newFormKey += 1

      switch (this.selectedTab) {
        case 0:
          this.editMachine(param)
          break
        case 1:
          this.newSystem()
          break
        default:
          break
      }

      this.newForm = true
    },

    // eslint-disable-next-line
    async deleteMode (param: any) {
      this.formMode = 'delete'
      this.formColor = 'warning'
      this.newFormKey += 1

      switch (this.selectedTab) {
        case 0:
          await this.deleteMachine(param)
          break
        case 1:
          await this.deleteSystem(param)
          break
        default:
          break
      }

      this.newForm = true
    },

    completeMode () {
      switch (this.selectedTab) {
        case 0:
          this.successMachine()
          break
        case 1:
          this.successSystem()
          break
        default:
          break
      }
    },

    errorMode () {
      console.log('error mode')
    },

    /* Form Specific Methods */
    newMachine () {
      this.newFormKey += 1
      this.newForm = true
      this.formMode = 'create'
      this.formColor = 'primary'
      this.formTitle = 'New Machine'
    },

    // eslint-disable-next-line
    editMachine (param: any) {
      this.formTitle = 'Update Machine'
      this.SetMachine(param)
    },

    // eslint-disable-next-line
    async deleteMachine (param: any) {
      this.formTitle = 'Delete Machine'
      await this.SetMachine(param)
    },

    successMachine () {
      switch (this.formMode) {
        case 'create':
          this.formTitle = 'New machine created!'
          break
        case 'update':
          this.formTitle = 'Machine updated!'
          break
        case 'delete':
          this.formTitle = 'Machine deleted!'
          break
        default:
          break
      }
      this.formColor = 'success'
      this.formMode = 'complete'
    },

    newSystem () {
      this.newFormKey += 1
      this.newForm = true
      this.formMode = 'create'
      this.formColor = 'primary'
      this.formTitle = 'New System'
    },

    // eslint-disable-next-line
    async deleteSystem (param: any) {
      this.formTitle = 'Delete System'
      await this.SetSystem(param)
    },

    successSystem () {
      switch (this.formMode) {
        case 'create':
          this.formTitle = 'New system created!'
          break
        case 'update':
          this.formTitle = 'System updated!'
          break
        case 'delete':
          this.formTitle = 'System deleted!'
          break
        default:
          break
      }
      this.formColor = 'success'
      this.formMode = 'complete'
    },

    /* Popup Methods */
    closePopup () {
      this.itemPopup = false
      this.itemPopupKey += 1
      this.recreateTabs(this.selectedTab)
    },

    // eslint-disable-next-line
    machinePopup (args: any) {
      this.itemPopup = true
      this.itemPopupKey += 1
      this.selectedMachine = args
    },

    // eslint-disable-next-line
    systemPopup (args: any) {
      this.itemPopup = true
      this.itemPopupKey += 1
      this.selectedSystem = args
    },

    /* Click Methods */
    // eslint-disable-next-line
    itemClicked (args: any) {
      switch (this.selectedTab) {
        case 0:
          this.machinePopup(args)
          break
        case 1:
          this.systemPopup(args)
          break
        case 2:
          break
        case 3:
          break
        case 4:
          break
        default:
          break
      }
    },

    // eslint-disable-next-line
    itemRightClicked (event: any, { item }: { item: any}) {
      console.log(item)
    },

    // eslint-disable-next-line
    editItem (param: any) {
      this.updateMode(param)
    },

    // eslint-disable-next-line
    deleteItem (param: any) {
      this.deleteMode(param)
    }
  }
})
</script>