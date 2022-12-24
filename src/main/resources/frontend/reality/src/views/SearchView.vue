<template>
  <v-container fluid>
    <v-row>
      <v-col>
        <search-panel></search-panel>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <search-list v-if="hasResults"></search-list>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapGetters } from 'vuex'
import SearchPanel from '@/components/search/SearchPanel.vue'
import SearchList from '@/components/search/SearchList.vue'

export default Vue.extend({
  name: 'SearchView',

  components: {
    SearchPanel, SearchList
  },

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Search'
    }
  },

  data () {
    return {
    }
  },

  computed: {
    ...mapGetters({
      hasResults: 'hasResults'
    })
  }
})
</script>
