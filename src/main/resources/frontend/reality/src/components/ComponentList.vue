<template>
  <v-card>
    <v-card-actions
      class="text-h5 font-weight-black"
    >
      Coblogs List
      <v-spacer></v-spacer>
    </v-card-actions>
    <v-divider class="mx-8 mt-4"></v-divider>
    <v-card-text>
      <v-data-table
        :headers="headers"
        :items="componentsList"
        :loading="loading"
        loading-text="Fetching data, please wait."
        :items-per-page="10"
        sort-by="sorting"
        sort-desc
      >
      </v-data-table>
    </v-card-text>
  </v-card>
</template>

<script>
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'ComponentList',

  components: {

  },

  data () {
    return {
      loading: false,
      headers: [
        {
          text: 'ID',
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
          text: 'Sequence',
          align: 'start',
          sortable: false,
          value: 'sequence'
        },
        {
          text: 'Created By',
          align: 'start',
          sortable: true,
          value: 'created_by'
        }
      ],
      sorting: [
        'name', 'sequence'
      ]
    }
  },

  computed: {
    ...mapGetters({
      isAuth: 'isAuthenticated',
      user: 'getUserState',
      componentsList: 'getComponentsList'
    })
  },

  methods: {
    ...mapActions([
      'GetComponentList'
    ]),

    async getComponents () {
      try {
        this.loading = true
        await this.GetComponentList()
        this.loading = false
      } catch (error) {

      }
    }
  },

  async mounted () {
    await this.getComponents()
  }
})
</script>
