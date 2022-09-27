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
      <v-tab v-if="isAuth">Users</v-tab>

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
        <!-- Zones -->
        <empty-list></empty-list>
      </v-tab-item>

      <v-tab-item>
        <!-- Releases -->
        <release-list
          :key="releaseTabKey"
          @open-form="openForm"
          @item-clicked="itemClicked"
          @edit-item="editItem"
          @delete-item="deleteItem"
        >
        </release-list>
      </v-tab-item>

      <v-tab-item>
        <!-- Rules -->
        <empty-list></empty-list>
      </v-tab-item>

      <v-tab-item v-if="isAuth">
        <!-- Users -->
        <user-list
          :key="userTabKey"
          @open-form="openForm"
          @item-clicked="itemClicked"
          @edit-item="editItem"
          @delete-item="deleteItem"
        >
        </user-list>
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
      <release-form
        v-if="selectedTab == 3"
        :mode="formMode"
        :titleBarColor="formColor"
        :title="formTitle"
        @close-form="closeForm"
        @form-submit="formSubmitted"
      >
      </release-form>
      <user-form
        v-if="selectedTab == 5"
        :mode="formMode"
        :titleBarColor="formColor"
        :title="formTitle"
        @close-form="closeForm"
        @form-submit="formSubmitted"
      >
      </user-form>
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

      <release-popup
        v-if="selectedTab == 3"
        :releaseItem="selectedRelease"
        @close-popup="closePopup"
      >
      </release-popup>

      <user-popup
        v-if="selectedTab == 5"
        :userItem="selectedUser"
        @close-popup="closePopup"
      >
      </user-popup>
    </v-dialog>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'
import EmptyList from '@/components/EmptyList.vue'
import SystemList from '@/components/misc/SystemList.vue'
import SystemForm from '@/components/misc/SystemForm.vue'
import SystemPopup from '@/components/misc/SystemPopup.vue'
import MachineList from '@/components/misc/MachineList.vue'
import MachineForm from '@/components/misc/MachineForm.vue'
import MachinePopup from '@/components/misc/MachinePopup.vue'
import ReleaseList from '@/components/misc/ReleaseList.vue'
import ReleaseForm from '@/components/misc/ReleaseForm.vue'
import ReleasePopup from '@/components/misc/ReleasePopup.vue'
import UserList from '@/components/misc/UserList.vue'
import UserForm from '@/components/misc/UserForm.vue'
import UserPopup from '@/components/misc/UserPopup.vue'

export default Vue.extend({
  name: 'MiscView',

  components: {
    EmptyList, SystemList, SystemForm, SystemPopup, MachineList, MachineForm, MachinePopup, ReleaseList, ReleaseForm, ReleasePopup, UserList, UserForm, UserPopup
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
      releaseTabKey: 0,
      userTabKey: 0,
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
      },
      selectedRelease: {
        id: 0,
        name: '',
        system_count: 0
      },
      selectedUser: {
        username: '',
        roles: []
      }
    }
  },

  computed: {
    ...mapGetters({
      isAuth: 'isAuthenticated',
      getUser: 'getUserState',
      frontendFeatures: 'getFrontendList',
      disabledSwitchableFeatures: 'getDisabledSwitchableList'
    })
  },

  methods: {
    /* Vuex Methods */
    ...mapActions([
      'SetMachine', 'SetSystem', 'SetRelease', 'SetUserItem'
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
          this.releaseTabKey += 1
          break
        case 4:
          break
        case 5:
          this.userTabKey += 1
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
        case 3:
          this.newRelease()
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
          this.editSystem(param)
          break
        case 3:
          this.editRelease(param)
          break
        case 5:
          this.editUser(param)
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
        case 3:
          this.successRelease()
          break
        case 5:
          this.succesUser()
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
    editSystem (param: any) {
      this.formTitle = 'Update System'
      this.SetSystem(param)
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

    newRelease () {
      this.newFormKey += 1
      this.newForm = true
      this.formMode = 'create'
      this.formColor = 'primary'
      this.formTitle = 'New Release'
    },

    // eslint-disable-next-line
    editRelease (param: any) {
      this.formTitle = 'Update Release'
      this.SetRelease(param)
    },

    successRelease () {
      switch (this.formMode) {
        case 'create':
          this.formTitle = 'New release created!'
          break
        case 'update':
          this.formTitle = 'Release updated!'
          break
        case 'delete':
          this.formTitle = 'Release deleted!'
          break
        default:
          break
      }
      this.formColor = 'success'
      this.formMode = 'complete'
    },

    // eslint-disable-next-line
    editUser (param: any) {
      this.formTitle = 'Update User'
      this.SetUserItem(param)
    },

    succesUser () {
      switch (this.formMode) {
        case 'create':
          this.formTitle = 'New user created!'
          break
        case 'update':
          this.formTitle = 'User updated!'
          break
        case 'delete':
          this.formTitle = 'User deleted!'
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

    // eslint-disable-next-line
    releasePopup (args: any) {
      this.itemPopup = true
      this.itemPopupKey += 1
      this.selectedRelease = args
    },

    // eslint-disable-next-line
    userPopup (args: any) {
      this.itemPopup = true
      this.itemPopupKey += 1
      this.selectedUser = args
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
          this.releasePopup(args)
          break
        case 4:
          break
        case 5:
          this.userPopup(args)
          break
        default:
          break
      }
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
