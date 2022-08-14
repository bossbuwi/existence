<template>
  <v-card>
    <v-card-actions
      class="text-h5 font-weight-black"
    >
      Frontend Features List
    </v-card-actions>
    <v-divider class="mx-8 mt-4"></v-divider>
    <v-card-text>
      <v-data-table
        :headers="headers"
        :items="frontendList"
        :loading="loading"
        loading-text="Fetching data, please wait."
        :items-per-page="10"
        sort-by="id"
        sort-asc
      >
        <template v-slot:[`item.key`]="{ item }">
          <div class="font-weight-black">
            {{ item.key }}
          </div>
        </template>
        <template v-slot:[`item.value`]="{ item }">
          <div class="font-italic">
            {{ item.value }}
          </div>
        </template>
      </v-data-table>
    </v-card-text>
  </v-card>
</template>

<script>
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'FrontendList',

  components: {

  },

  data () {
    return {
      loading: false,
      headers: [
        {
          text: 'Key',
          align: 'start',
          sortable: true,
          value: 'key'
        },
        {
          text: 'Current Value',
          align: 'start',
          sortable: false,
          value: 'value'
        },
        {
          text: 'Description',
          align: 'start',
          sortable: false,
          value: 'desc'
        },
        {
          text: 'Last Changed By',
          align: 'start',
          sortable: false,
          value: 'last_changed_by'
        }
      ]
    }
  },

  computed: {
    ...mapGetters({
      isAuth: 'isAuthenticated',
      user: 'getUserState',
      frontendList: 'getFrontendList'
    })
  },

  methods: {
    ...mapActions([
      'GetFrontendList'
    ]),

    async getFrontendList () {
      try {
        this.loading = true
        await this.GetFrontendList()
        this.loading = false
      } catch (error) {

      }
    }
  }

  // async mounted () {
  //   await this.getFrontendList()
  // }
})
</script>
