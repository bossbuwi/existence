<template>
  <v-card>
    <v-card-actions
      class="text-h5 font-weight-black"
    >
      Coblogs List
      <v-spacer></v-spacer>
      <v-btn
        v-if="isAuth"
        color="primary"
        @click.stop="startCob"
      >
        <v-icon left>
          mdi-playlist-plus
        </v-icon>
        New COB
      </v-btn>
    </v-card-actions>
    <v-divider class="mx-8 mt-4"></v-divider>
    <v-card-text>
      <v-data-table
        :headers="headers"
        :items="coblogsList"
        :loading="loading"
        loading-text="Fetching data, please wait."
        :items-per-page="10"
        sort-by="id"
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
  name: 'CoblogList',

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
          text: 'System Details',
          align: 'start',
          sortable: true,
          value: '_sysdetails'
        },
        {
          text: 'Status',
          align: 'start',
          sortable: true,
          value: 'conclusion'
        },
        {
          text: 'No. of Items',
          align: 'start',
          sortable: false,
          value: 'number_of_components'
        },
        {
          text: 'Created By',
          align: 'start',
          sortable: true,
          value: 'created_by'
        }
      ]
    }
  },

  computed: {
    ...mapGetters({
      isAuth: 'isAuthenticated',
      user: 'getUserState',
      coblogsList: 'getCoblogList'
    })
  },

  methods: {
    ...mapActions([
      'GetCoblogList'
    ]),

    async getCoblogs () {
      try {
        this.loading = true
        await this.GetCoblogList()
        this.loading = false
      } catch (error) {

      }
    },

    startCob () {
      this.$emit('start-cob')
    }
  },

  async mounted () {
    await this.getCoblogs()
  }
})
</script>
