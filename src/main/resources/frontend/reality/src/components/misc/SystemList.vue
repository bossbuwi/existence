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

    <!-- Item Context Menu -->
    <v-menu
      v-if="editAllowed"
      v-model="showMenu"
      rounded="lg"
      :position-x="x"
      :position-y="y"
      absolute
      offset-y
    >
      <v-list>
        <v-list-item
          link
          @click="editItem"
        >
          <v-list-item-title>Edit</v-list-item-title>
        </v-list-item>
        <v-list-item
          v-if="deleteAllowed"
          link
          @click="deleteItem"
        >
          <v-list-item-title>Delete</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'SystemList',

  data () {
    return {
      editAllowed: false,
      deleteAllowed: false,
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
          value: 'zone_prefixes'
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
      },
      showMenu: false,
      x: 0,
      y: 0
    }
  },

  computed: {
    ...mapGetters({
      systemsList: 'getSystemsList',
      user: 'getUserState',
      isAuth: 'isAuthenticated'
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
      this.showMenu = false
      this.selectedSystem = item
      this.x = event.clientX
      this.y = event.clientY
      this.$nextTick(() => {
        this.showMenu = true
      })
    },

    editItem () {
      this.$emit('edit-item', this.selectedSystem)
    },

    deleteItem () {
      this.$emit('delete-item', this.selectedSystem)
    }
  },

  async mounted () {
    const role = this.user.roles.find((x: string) =>
      x === 'ROLE_ADMIN'
    )

    if (this.isAuth) {
      this.editAllowed = true
      const superuser = this.user.roles.find((x: string) => x === 'ROLE_SUPERUSER')
      if (superuser !== undefined) {
        this.deleteAllowed = true
      }
    }

    if (role === undefined) {
      this.newBtnShown = false
    }

    await this.fetchSystems()
  }
})
</script>
