<template>
  <v-container fluid>
    <v-row>
      <v-col cols="5">
        <v-skeleton-loader
          v-if="!userLoaded"
          type="card"
          transition="scale-transition"
        >
        </v-skeleton-loader>
        <v-card>
          <v-card-title>
            <v-row
              align="center"
            >
              <v-col cols="auto">
                <v-avatar color="red">
                  <span class="white--text text-h5">{{ avatarIcon }}</span>
                </v-avatar>
              </v-col>
              <v-col cols="auto">
                <span>{{ userDetails.username }}</span>
              </v-col>
            </v-row>
          </v-card-title>
          <v-card-text>
            <v-list two-line>
              <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>Roles</v-list-item-title>
                  <v-list-item-subtitle>{{ roles }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <v-list-item>
                <v-list-item-content v-if="userDetails.events_added > 0">
                  <v-list-item-title>No. of events logged</v-list-item-title>
                  <v-list-item-subtitle>{{ userDetails.events_added }}</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
              <!-- <v-list-item>
                <v-list-item-content>
                  <v-list-item-title>Most used system</v-list-item-title>
                  <v-list-item-subtitle>OS</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item> -->
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
  name: 'UserProfileView',

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — User Profile'
    }
  },

  data () {
    return {
      show: false,
      avatarIcon: '',
      roles: '',
      userLoaded: false
    }
  },

  computed: {
    ...mapGetters({
      userDetails: 'getUserDetails'
    })
  },

  methods: {
    ...mapActions([
      'GetUserDetails'
    ]),

    async getUserDetails () {
      await this.GetUserDetails()
    }
  },

  async mounted () {
    this.userLoaded = false
    await this.getUserDetails()
    const details = this.userDetails
    this.avatarIcon = details.username.substr(0, 1).toUpperCase()
    const rolesArr = details.roles
    rolesArr.forEach((element: string) => {
      if (this.roles.length === 0) {
        this.roles = element.substr(5)
      } else {
        this.roles = this.roles + ', ' + element.substr(5)
      }
    })
    this.userLoaded = true
  }
})
</script>
