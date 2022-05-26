<template>

    <v-card
      hover
      :loading="loading"
    >
      <v-card-title>
        <v-row>
          <v-col
            cols="4"
          >
            <v-icon
              large
              color="blue darken-2"
            >
             {{ cardIcon }}
            </v-icon>
          </v-col>
          <v-spacer></v-spacer>
          <v-col
            cols="auto"
          >
            {{ cardName }}
          </v-col>
        </v-row>
      </v-card-title>

      <v-row>
        <v-spacer></v-spacer>
        <v-col
          cols="auto"
        >
          <v-card-text
            class="text-h5"
          >
            {{ cardContent }}
          </v-card-text>
        </v-col>
      </v-row>

      <v-divider
        class="mx-8"
      >
      </v-divider>

      <v-card-actions>
        <v-btn
          text
          class="text-capitalize"
          @click="refreshData"
        >
          <v-icon right>
             mdi-refresh
          </v-icon>
          &nbsp;Refresh
        </v-btn>
      </v-card-actions>
    </v-card>
</template>

<script lang="ts">
import Vue from 'vue'

export default Vue.extend({
  name: 'CustomCardBasic',

  props: [
    'cardIcon',
    'cardName',
    'cardContent',
    'loading'
  ],

  data () {
    return {

    }
  },

  methods: {
    refreshData () {
      this.$emit('refresh-data')
    }
  }
})
</script>
