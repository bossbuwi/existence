<template>
  <v-card
    :loading="cardLoading"
    hover
  >
    <validation-observer
      ref="observer"
      v-slot="{ invalid }"
    >
      <v-form
        v-if="!isAuth"
        id="login"
        :disabled="disabled"
        @submit.prevent="submitForm"
      >
        <v-container>
          <validation-provider
            v-slot="{ errors }"
            name="Username"
            rules="required"
          >
            <v-text-field
              class="mb-4"
              v-model="user.username"
              :error-messages="errors"
              label="Username"
              prepend-inner-icon="mdi-account"
              required
            >
            </v-text-field>
          </validation-provider>
          <validation-provider
            v-slot="{ errors }"
            name="Password"
            rules="required"
          >
            <v-text-field
              class="mb-4"
              type="password"
              v-model="user.password"
              :error-messages="errors"
              label="Password"
              prepend-inner-icon="mdi-key"
              required
            >
            </v-text-field>
          </validation-provider>
          <v-btn
            type="submit"
            block
            :disabled="invalid"
            elevation="2"
            :loading="loading"
          >
            Login
          </v-btn>
        </v-container>
      </v-form>
      <v-list
        v-if="isAuth"
      >
        <v-list-item
          href="#"
          @click="logout"
        >
          <v-list-item-icon>
            <v-icon>mdi-exit-to-app</v-icon>
          </v-list-item-icon>
          Log out
        </v-list-item>
      </v-list>
    </validation-observer>
  </v-card>
</template>

<script lang="ts">
import Vue from 'vue'
import { extend, setInteractionMode, ValidationObserver, ValidationProvider } from 'vee-validate'
import { required } from 'vee-validate/dist/rules'
import { mapActions, mapGetters } from 'vuex'

setInteractionMode('aggressive')

extend('required', {
  ...required,
  message: '{_field_} cannot be empty.'
})

export default Vue.extend({
  name: 'LoginForm',

  components: {
    ValidationObserver,
    ValidationProvider
  },

  data () {
    return {
      user: {
        username: '',
        password: ''
      },
      loading: false,
      disabled: false,
      cardLoading: false
    }
  },

  computed: {
    ...mapGetters({ isAuth: 'isAuthenticated' })
  },

  methods: {
    ...mapActions(['Login', 'Logout']),

    async submitForm () {
      this.loading = true
      await this.Login(this.user)
      if (this.isAuth) {
        this.$router.go(0)
      } else {
        this.user.username = ''
        this.user.password = ''
        this.loading = false
      }
    },

    logout () {
      this.clearForm()
      this.disabled = true
      this.cardLoading = true
      this.Logout()
      this.$router.go(0)
    },

    clearForm () {
      this.user.username = ''
      this.user.password = ''
    }
  }
})
</script>
