<template>
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
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'MachineList',

  data () {
    return {
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
      }
    }
  },

  computed: {
    ...mapGetters({
      machineList: 'getMachineList',
      user: 'getUserState'
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

    await this.fetchMachines()
  }
})
</script>
