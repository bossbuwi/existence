<template>
  <v-card>
    <v-toolbar
      dark
      dense
      :color="titleBarColor"
    >
      <v-btn
        icon
        dark
        @click="close"
      >
        <v-icon>mdi-close</v-icon>
      </v-btn>
      <v-toolbar-title>{{ title }}</v-toolbar-title>
      <v-spacer></v-spacer>
    </v-toolbar>
    <v-card-text>
      <v-container>
        <v-form
          id="userForm"
          :disabled="formDisabled"
        >
          <v-row>
            <span class="text-h6">General Details</span>
          </v-row>
          <v-row>
            <v-col
              cols="12"
              sm="6"
              md="4"
            >
              <v-text-field
                v-model="userForm.username"
                label="Username"
                type="text"
                disabled
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-select
                v-model="userForm.roles"
                :items="roles"
                :loading="rolesLoading"
                :disabled="rolesDisabled"
                label="Roles*"
                multiple
                required
              ></v-select>
            </v-col>
          </v-row>
        </v-form>
        <v-row class="my-4">
          <small v-if="!formDisabled">*indicates required field</small>
        </v-row>
        <v-row
          class="my-4"
        >
          <v-spacer></v-spacer>
          <v-btn
            type="submit"
            v-if="submitEnabled"
            color="primary"
            :loading="submitLoading"
            depressed
            @click="submit"
          >
            Submit
          </v-btn>
        </v-row>
      </v-container>
    </v-card-text>
  </v-card>
</template>

<script>
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'

export default Vue.extend({
  name: 'UserForm',

  props: [
    'mode', 'title', 'titleBarColor'
  ],

  data () {
    return {
      submitLoading: false,
      rolesLoading: false,
      rolesDisabled: false,
      userForm: {
        id: 0,
        username: '',
        roles: []
      },
      roles: []
    }
  },

  computed: {
    ...mapGetters({
      errorStatus: 'getErrorStatus',
      roleList: 'getRoles',
      user: 'getUser'
    }),

    submitEnabled: {
      get () {
        if (this.mode === 'complete') return false
        if (this.mode === 'create') return true
        if (this.mode === 'update') return true
        if (this.mode === 'delete') return true
        return false
      }
    },
    formDisabled: {
      get () {
        if (this.mode === 'complete') return true
        if (this.mode === 'create') return false
        if (this.mode === 'update') return false
        if (this.mode === 'enquire') return true
        if (this.mode === 'delete') return true
        return false
      }
    }
  },

  methods: {
    ...mapActions([
      'GetRoles'
    ]),

    close () {
      this.$emit('close-form')
    },

    mapObject () {
      console.log(this.user)
      this.userForm.username = this.user.username
    },

    async submit () {
      this.formLoading()

      const machineData = {}
      if (this.mode !== 'create') {
        machineData.id = this.machine.id
        machineData.name = this.machine.name
        machineData.new_name = this.userForm.newName
      }

      machineData.name = this.userForm.name

      switch (this.mode) {
        case 'create':
          await this.PostMachine(machineData)
          this.formSubmitted()
          break
        case 'update':
          await this.PutMachine(machineData)
          this.formSubmitted()
          break
        case 'delete':
          await this.DeleteMachine(machineData.id)
          this.formSubmitted()
          break
        default:
          break
      }
    },

    formLoading () {
      this.submitLoading = true
    },

    formSubmitted () {
      if (!this.errorStatus) {
        this.formComplete()
      } else {
        this.formError()
      }
    },

    formComplete () {
      this.submitLoading = false
      this.$emit('form-submit', 'success')
    },

    formError () {
      this.submitLoading = false
      this.$emit('form-submit', 'error')
    }
  },

  async mounted () {
    if (this.mode === 'enquire') this.mapObject()
    if (this.mode === 'update') this.mapObject()
    if (this.mode === 'delete') this.mapObject()

    await this.GetRoles()

    const rolesArr = this.roleList
    rolesArr.forEach((element) => {
      const item = {
        text: element.name,
        value: element
      }
      this.roles.push(item)
    })
  }
})
</script>
