<template>
  <v-card>
    <v-card-actions
      class="text-h5 font-weight-black"
    >
      Systems List
      <v-spacer></v-spacer>
      <v-btn
        v-if="newBtnShown"
        color="primary"
        @click.stop="newSystem"
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
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'SystemList',

  data () {
    return {
      newBtnShown: true,
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
          value: 'zone_names'
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

    async fetchSystems () {
      try {
        this.loading = true
        await this.GetSystemsList()
        this.loading = false
      } catch (error) {

      }
    },

    newSystem () {
      this.$emit('open-form')
    },

    // eslint-disable-next-line
    itemClicked (args: any, { item }: { item: any }) {
      this.selectedSystem = item
      this.$emit('item-clicked', this.selectedSystem)
    },

    // eslint-disable-next-line
    itemRightClicked (event: any, { item }: { item: any}) {
      console.log(item)
    }
  },

  async mounted () {
    const role = this.user.roles.find((x: string) =>
      x === 'ROLE_ADMIN'
    )

    if (role === undefined) {
      this.newBtnShown = false
    }

    await this.fetchSystems()
  }
})
</script>
