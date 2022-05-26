<template>
  <v-container fluid>
    <v-card>
      <v-card-actions
        class="text-h5 font-weight-black"
      >
        Systems List
        <v-spacer></v-spacer>
        <v-btn
          v-if="newBtnShown"
          color="primary"
          @click.stop="openFormDialog"
        >
          <v-icon left>
            mdi-playlist-plus
          </v-icon>
          New System
        </v-btn>
      </v-card-actions>
      <v-divider class="mx-8 mt-4"></v-divider>
      <v-card-text>
        <v-data-table
          :headers="headers"
          :items="systemsList"
          :items-per-page="5"
          item-key="id"
          :loading="loading"
          sort-by="global_prefix"
          loading-text="Fetching data, please wait."
          @click:row="itemClicked"
          @contextmenu:row.prevent="itemRightClicked"
        >
        </v-data-table>
      </v-card-text>
    </v-card>
    <!-- New System Dialog -->
    <v-dialog
      v-model="newFormDialog"
      persistent
      eager
      fullscreen
    >
      <system-form
        :titleBarColor="formColor"
        :title="formTitle"
        @close-popup="closeFormDialog"
      >
      </system-form>
    </v-dialog>
    <!-- System Details Dialog -->
    <v-dialog
      v-model="systemDialog"
      max-width="600px"
      persistent
      eager
    >
      <system-popup
        :systemItem="selectedSystem"
        @close-popup="closePopup"
      >
      </system-popup>
    </v-dialog>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'
import SystemPopup from '../components/SystemPopup.vue'
import SystemForm from '../components/SystemForm.vue'

export default Vue.extend({
  name: 'SystemsView',

  components: {
    SystemPopup, SystemForm
  },

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Systems'
    }
  },

  data () {
    return {
      formTitle: '',
      formColor: '',
      systemDialog: false,
      newBtnShown: true,
      newFormDialog: false,
      loading: false,
      headers: [
        {
          text: 'System Prefix',
          align: 'start',
          sortable: true,
          value: 'global_prefix'
        },
        {
          text: 'Machine',
          align: 'start',
          sortable: true,
          value: 'machine'
        },
        {
          text: 'Zones',
          align: 'start',
          sortable: false,
          value: 'zones'
        },
        {
          text: 'Release',
          align: 'start',
          sortable: true,
          value: 'release'
        }
      ],
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

  computed: {
    ...mapGetters({
      systemsList: 'getSystemsList',
      user: 'getUserState'
    })
  },

  methods: {
    ...mapActions([
      'GetSystemsList'
    ]),

    async getSystems () {
      try {
        this.loading = true
        await this.GetSystemsList()
        this.loading = false
      } catch (error) {

      }
    },

    openFormDialog () {
      this.formTitle = 'New System'
      this.formColor = 'primary'
      this.newFormDialog = true
    },

    closeFormDialog () {
      this.newFormDialog = false
    },

    itemClicked (args: any, { item }: { item: any }) {
      console.log(item)
      this.selectedSystem = item
      this.systemDialog = true
    },

    itemRightClicked (event: any, { item }: { item: any}) {
      console.log(item)
    },

    closePopup () {
      this.systemDialog = false
    }
  },

  async mounted () {
    const role = this.user.roles.find((x: string) =>
      x === 'ROLE_SUPERUSER'
    )

    if (role === undefined) {
      this.newBtnShown = false
    }

    await this.getSystems()
  }
})
</script>
