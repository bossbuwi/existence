<template>
  <v-container fluid>
    <v-card>
      <v-card-actions
        class="text-h5 font-weight-black"
      >
        Search Results
        <v-spacer></v-spacer>
      </v-card-actions>
      <v-divider class="mx-8 mt-4"></v-divider>
      <v-card-text>
        <v-data-table
          :headers="headers"
          :items="searchResults"
          :items-per-page="10"
          item-key="id"
          :loading="loading"
          sort-by="id"
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
import { mapGetters } from 'vuex'

export default Vue.extend({
  name: 'SearchList',

  data () {
    return {
      editAllowed: false,
      deleteAllowed: false,
      newBtnShown: true,
      loading: false,
      headers: [
        {
          text: 'ID',
          align: 'start',
          sortable: true,
          value: 'id'
        },
        {
          text: 'System',
          align: 'start',
          sortable: true,
          value: 'system'
        },
        {
          text: 'Zones',
          align: 'start',
          sortable: false,
          value: 'zones'
        },
        {
          text: 'Event Type',
          align: 'start',
          sortable: true,
          value: 'event_types'
        },
        {
          text: 'Start Date',
          align: 'start',
          sortable: true,
          value: 'start_date'
        },
        {
          text: 'End Date',
          align: 'start',
          sortable: true,
          value: 'end_date'
        },
        {
          text: 'Created By',
          align: 'start',
          sortable: true,
          value: 'created_by'
        }
      ],
      selectedItem: {

      },
      showMenu: false,
      x: 0,
      y: 0
    }
  },

  computed: {
    ...mapGetters({
      searchResults: 'getSearchResults',
      user: 'getUserState',
      isAuth: 'isAuthenticated'
    })
  },

  methods: {
    // eslint-disable-next-line
    itemClicked (args: any, { item }: { item: any }) {
      this.selectedItem = item
      this.$emit('item-clicked', this.selectedItem)
    },

    // eslint-disable-next-line
    itemRightClicked (event: any, { item }: { item: any}) {
      this.showMenu = false
      this.selectedItem = item
      this.x = event.clientX
      this.y = event.clientY
      this.$nextTick(() => {
        this.showMenu = true
      })
    },

    editItem () {
      this.$emit('edit-item', this.selectedItem)
    },

    deleteItem () {
      this.$emit('delete-item', this.selectedItem)
    }
  },

  async mounted () {
    if (this.isAuth) {
      this.editAllowed = true
      const superuser = this.user.roles.find((x: string) => x === 'ROLE_SUPERUSER')
      if (superuser !== undefined) {
        this.deleteAllowed = true
      }
    }
  }
})
</script>
