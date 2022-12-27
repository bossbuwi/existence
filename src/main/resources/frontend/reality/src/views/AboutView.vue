<template>
  <v-container fluid>
    <v-row>
      <v-col cols="5">
        <v-skeleton-loader
          v-if="!aboutLoaded"
          type="card"
          transition="scale-transition"
        >
        </v-skeleton-loader>
        <v-card v-if="aboutLoaded">
          <v-card-title>
            <v-row
              align="center"
            >
              <v-col cols="auto">
                <span>{{ appName }}</span>
              </v-col>
            </v-row>
          </v-card-title>
          <v-card-subtitle>
            {{ appSubtitle }}
          </v-card-subtitle>
          <v-card-text>
            <v-divider inset></v-divider>
            <v-list subheader two-line>
              <v-subheader inset>Application</v-subheader>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>Version</v-list-item-title>
                  <v-list-item-subtitle>{{ appDetails.version_number + appDetails.named_release}}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>Release Date</v-list-item-title>
                  <v-list-item-subtitle>{{ appDetails.release_date}}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>Developer</v-list-item-title>
                  <v-list-item-subtitle>{{ appDetails.developer }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-list>
            <v-divider inset></v-divider>
            <v-list subheader two-line>
              <v-subheader inset>Backend</v-subheader>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>Language</v-list-item-title>
                  <v-list-item-subtitle>{{ appDetails.backend_language }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>Framework</v-list-item-title>
                  <v-list-item-subtitle>{{ appDetails.backend_framework }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>Database</v-list-item-title>
                  <v-list-item-subtitle>{{ appDetails.database }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-list>
            <v-divider inset></v-divider>
            <v-list subheader two-line>
              <v-subheader inset>Frontend</v-subheader>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>Language</v-list-item-title>
                  <v-list-item-subtitle>{{ appDetails.frontend_language }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>Framework</v-list-item-title>
                  <v-list-item-subtitle>{{ appDetails.frontend_framework }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>Theme</v-list-item-title>
                  <v-list-item-subtitle>{{ appDetails.frontend_theme }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts">
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'AboutView',

  components: {},

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — About'
    }
  },

  data () {
    return {
      appName: process.env.VUE_APP_NAME,
      appSubtitle: process.env.VUE_APP_SUBTITLE,
      aboutLoaded: false
    }
  },

  computed: {
    ...mapGetters({
      appDetails: 'getAppDetails'
    })
  },

  methods: {
    ...mapActions([
      'GetAppDetails'
    ]),

    async getAppDetails () {
      await this.GetAppDetails()
    }
  },

  async mounted () {
    this.aboutLoaded = false
    await this.getAppDetails()
    this.aboutLoaded = true
  }
})
</script>
