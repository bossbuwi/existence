/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultState = () => {
  return {
    about: {

    }
  }
}

const state = getDefaultState()

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
    Object.assign(state, getDefaultState())
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
