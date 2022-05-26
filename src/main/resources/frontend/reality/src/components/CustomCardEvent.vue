<template>
    <v-card
      hover
      :loading="loading"
    >
      <v-card-title>
        <v-row>
          <v-col
            cols="1"
          >
            <v-icon
              large
              color="blue darken-2"
            >
             mdi-calendar-text-outline
            </v-icon>
          </v-col>
          <v-spacer></v-spacer>
          <v-col
            cols="auto"
          >
            Latest Event
          </v-col>
        </v-row>
      </v-card-title>
      <v-row
        v-if="!shown"
        justify="center"
      >
        <v-col
          cols="2"
        >
          <v-divider
            class="mx-8"
          ></v-divider>
        </v-col>
      </v-row>
      <v-card-subtitle
        v-if="!shown"
      >
        No events found.
      </v-card-subtitle>
      <v-card-text
        v-if="shown"
        class="text-body-1"
      >
        <v-row>
          <v-col
            cols="auto"
          >
            System
          </v-col>
          <v-spacer></v-spacer>
          <v-col
            cols="auto"
          >
            {{ globalPrefix }}
          </v-col>
        </v-row>

        <v-row>
          <v-col
            cols="auto"
          >
            Zones
          </v-col>
          <v-spacer></v-spacer>
          <v-col
            cols="auto"
          >
            {{ zones }}
          </v-col>
        </v-row>

        <v-row>
          <v-col
            cols="auto"
          >
            Event Types
          </v-col>
          <v-spacer></v-spacer>
          <v-col
            cols="auto"
          >
            {{ eventTypes }}
          </v-col>
        </v-row>

        <v-row>
          <v-col
            cols="auto"
          >
            Start Date
          </v-col>
          <v-spacer></v-spacer>
          <v-col
            cols="auto"
          >
            {{ startDate }}
          </v-col>
        </v-row>

        <v-row>
          <v-col
            cols="auto"
          >
            End Date
          </v-col>
          <v-spacer></v-spacer>
          <v-col
            cols="auto"
          >
            {{ endDate }}
          </v-col>
        </v-row>

        <v-row>
          <v-col
            cols="auto"
          >
            Last Modified By
          </v-col>
          <v-spacer></v-spacer>
          <v-col
            cols="auto"
          >
            {{ lastModifiedBy }}
          </v-col>
        </v-row>
      </v-card-text>

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
  name: 'CustomCardEvent',

  props: [
    'globalPrefix',
    'zones',
    'eventTypes',
    'startDate',
    'endDate',
    'lastModifiedBy',
    'shown',
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
