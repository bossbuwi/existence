<template>
  <v-container fluid>
    <v-tabs>
      <v-tabs-slider color="primary"></v-tabs-slider>
      <v-tab>Coblogs</v-tab>
      <v-tab>components</v-tab>
      <v-tab-item :eager="preload">
        <coblog-list
          :key="recreateCoblist"
          @start-cob="newCob"
        >
        </coblog-list>
      </v-tab-item>
      <v-tab-item :eager="preload">
        <component-list
          :key="recreateComponentlist"
        ></component-list>
      </v-tab-item>
    </v-tabs>
    <v-dialog
      v-model="coblogDialog"
      persistent
      eager
      fullscreen
    >
      <coblog-form
        :mode="cobFormMode"
        :key="recreateCob"
        @form-close="closeCob"
      >
      </coblog-form>
    </v-dialog>
  </v-container>
</template>

<script>
import Vue from 'vue'
import { mapActions, mapGetters, mapMutations } from 'vuex'
import CoblogList from '@/components/CoblogList.vue'
import ComponentList from '@/components/ComponentList.vue'
import CoblogForm from '@/components/CoblogForm.vue'

export default Vue.extend({
  name: 'CoblogView',

  components: {
    CoblogList, ComponentList, CoblogForm
  },

  metaInfo () {
    return {
      title: process.env.VUE_APP_NAME + ' — Coblog'
    }
  },

  data () {
    return {
      viewMode: '',
      preload: false,
      coblogDialog: false,
      cobFormMode: '',
      recreateCob: 0,
      recreateCoblist: 0,
      recreateComponentlist: 0
    }
  },

  computed: {
    ...mapGetters({
      isAuth: 'isAuthenticated',
      user: 'getUserState',
      isOngoing: 'getCobStatus'
    })
  },

  methods: {
    newCob () {
      if (this.isOngoing) {
        this.cobFormMode = 'ongoing'
      } else {
        this.cobFormMode = 'create'
      }
      this.recreateCob += 1
      this.coblogDialog = true
    },

    closeCob () {
      this.recreateCoblist += 1
      this.recreateComponentlist += 1
      this.coblogDialog = false
    }
  },

  mounted () {
    console.log('mounted')
  }
})
</script>
