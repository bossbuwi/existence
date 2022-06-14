<template>
  <v-card>
    <v-card-text>
      <v-form>
        <v-select
          v-model="cobLog.system"
          :items="systems"
          :loading="systemLoading"
          @input="systemSelected"
          label="System*"
        >
        </v-select>
      </v-form>
    </v-card-text>
  </v-card>
</template>

<script>
import Vue from 'vue'
import { mapActions, mapGetters, mapMutations } from 'vuex'

export default Vue.extend({
  name: 'CoblogStepTwo',

  data () {
    return {
      systemLoading: true,
      systems: [],
      cobLog: {
        system: ''
      }
    }
  },

  computed: {
    ...mapGetters({
      systemsList: 'getSystemsList',
      cobSystem: 'getCoblog'
    })
  },

  methods: {
    ...mapActions([
      'GetSystemsList'
    ]),

    ...mapMutations([
      'setCobSystem'
    ]),

    async getSystemsList () {
      try {
        await this.GetSystemsList()
      } catch (error) {

      }
    },

    systemSelected () {
      this.setCobSystem(this.cobLog.system)
    }
  },

  async mounted () {
    this.systemLoading = true

    await Promise.all([
      this.getSystemsList()
    ])

    this.systemLoading = false

    const systemArr = this.systemsList
    systemArr.forEach((element) => {
      const item = {
        text: element.machine + ' - ' + element.global_prefix,
        value: element
      }
      this.systems.push(item)
    })
  }
})
</script>
