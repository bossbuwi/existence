<template>
  <v-container fluid>
    <v-card>
      <v-card-title>
        Search
      </v-card-title>
      <v-card-subtitle class="font-italic">
        Seek and you shall find.
      </v-card-subtitle>
      <v-card-text>
        <v-scroll-x-reverse-transition>
          <form
            v-if="!show"
          >
            <v-row align="center">
              <v-col cols="10">
                <v-text-field
                  v-model="searchForm.keyword"
                  label="Search for something.."
                  clearable
                ></v-text-field>
              </v-col>
              <v-col cols="1">
                <v-btn
                  color="primary"
                  @click.stop="search"
                >
                  <v-icon left>
                    mdi-magnify
                  </v-icon>
                  Search
                </v-btn>
              </v-col>
            </v-row>
          </form>
        </v-scroll-x-reverse-transition>
      </v-card-text>
      <v-card-actions>
        <v-btn
          color="orange lighten-2"
          text
          @click="show = !show"
        >
          Filters
        </v-btn>
        <v-spacer></v-spacer>
        <v-btn
          icon
          @click="show = !show"
        >
          <v-icon>{{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
        </v-btn>
      </v-card-actions>
      <v-scroll-x-reverse-transition>
        <div v-show="show">
          <v-divider></v-divider>
          <v-card-text>
            <form>
              <v-row>
                <v-col>
                  <v-menu
                    :close-on-content-click="false"
                    :nudge-right="40"
                    transition="scale-transition"
                    offset-y
                    min-width="auto"
                  >
                    <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          label="From Date"
                          prepend-icon="mdi-calendar"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                    </template>
                      <v-date-picker
                        no-title
                      ></v-date-picker>
                  </v-menu>
                </v-col>
                <v-col>
                  <v-menu
                    :close-on-content-click="false"
                    :nudge-right="40"
                    transition="scale-transition"
                    offset-y
                    min-width="auto"
                  >
                    <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          label="To Date"
                          prepend-icon="mdi-calendar"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                    </template>
                      <v-date-picker
                        no-title
                      ></v-date-picker>
                  </v-menu>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-text-field
                    label="Machine"
                    clearable
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    label="System"
                    clearable
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    label="Zone"
                    clearable
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    label="JIRA"
                    clearable
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-text-field
                    label="Features On"
                    clearable
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    label="Features Off"
                    clearable
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    label="Zone"
                    clearable
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                    label="JIRA"
                    clearable
                  ></v-text-field>
                </v-col>
              </v-row>
            </form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
              color="primary"
            >
              <v-icon left>
                mdi-magnify
              </v-icon>
              Search
            </v-btn>
          </v-card-actions>
        </div>
      </v-scroll-x-reverse-transition>
    </v-card>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions } from 'vuex'

export default Vue.extend({
  name: 'SearchPanel',

  data () {
    return {
      show: false,
      searchForm: {
        keyword: ''
      }
    }
  },

  methods: {
    ...mapActions([
      'GetSearchEvents'
    ]),

    async search () {
      console.log(this.searchForm.keyword)
      await this.GetSearchEvents(this.searchForm.keyword)
    }
  }
})
</script>
