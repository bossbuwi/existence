/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultAboutState = () => {
  return {
    about: {

    }
  }
}

const state = getDefaultAboutState()

const getters = {
  getAppDetails: (state: any) => state.about
}

const actions = {
  async GetAppDetails ({ commit }: { commit: Commit }) {
    await axios.get('drapes/dreams').then((result) => {
      commit('setAbout', result.data)
    })
  }
}

const mutations = {
  resetAboutState (state: any) {
    Object.assign(state, getDefaultAboutState())
  },

  setAbout (state: any, about: any) {
    state.about = about
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
