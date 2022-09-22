<template>
  <v-container fluid>
    <v-card>
      <v-card-actions
        class="text-h5 font-weight-black"
      >
        Machines List
        <v-spacer></v-spacer>
        <v-btn
          v-if="newBtnShown"
          color="primary"
          @click.stop="newMachine"
        >
          <v-icon left>
            mdi-playlist-plus
          </v-icon>
          New Machine
        </v-btn>
      </v-card-actions>
      <v-divider class="mx-8 mt-4"></v-divider>
      <v-card-text>
        <v-data-table
          :headers="headers"
          :items="machineList"
          :items-per-page="5"
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
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'MachineList',

  data () {
    return {
      editAllowed: false,
      deleteAllowed: false,
      newBtnShown: true,
      loading: false,
      headers: [
        {
          text: 'Record ID',
          align: 'start',
          sortable: true,
          value: 'id'
        },
        {
          text: 'Name',
          align: 'start',
          sortable: true,
          value: 'name'
        },
        {
          text: 'No. of Systems',
          align: 'start',
          sortable: false,
          value: 'system_count'
        }
      ],
      selectedMachine: {
        name: '',
        systems: [],
        system_count: ''
      },
      showMenu: false,
      x: 0,
      y: 0
    }
  },

  computed: {
    ...mapGetters({
      machineList: 'getMachineList',
      user: 'getUserState',
      isAuth: 'isAuthenticated'
    })
  },

  methods: {
    ...mapActions([
      'GetMachinesList'
    ]),

    async fetchMachines () {
      try {
        this.loading = true
        await this.GetMachinesList()
        this.loading = false
      } catch (error) {

      }
    },

    newMachine () {
      this.$emit('open-form')
    },

    // eslint-disable-next-line
    itemClicked (args: any, { item }: { item: any }) {
      this.selectedMachine = item
      this.$emit('item-clicked', this.selectedMachine)
    },

    // eslint-disable-next-line
    itemRightClicked (event: any, { item }: { item: any}) {
      this.showMenu = false
      this.selectedMachine = item
      this.x = event.clientX
      this.y = event.clientY
      this.$nextTick(() => {
        this.showMenu = true
      })
    },

    editItem () {
      this.$emit('edit-item', this.selectedMachine)
    },

    deleteItem () {
      this.$emit('delete-item', this.selectedMachine)
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

    await this.fetchMachines()
  }
})
</script>
