/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultReleaseState = () => {
  return {
    releases: [

    ]
  }
}

const state = getDefaultReleaseState()

const getters = {
  getReleasesList: (state: any) => state.releases
}

const actions = {
  async GetReleasesList ({ commit }: { commit: Commit }) {
    await axios.get('sonata/con/releases/index').then((result) => {
      commit('resetReleaseState')
      const releaseArr = result.data as any[]
      releaseArr.forEach((element: any) => {
        commit('addReleaseToList', element)
      })
    })
  }
}

const mutations = {
  resetReleaseState (state: any) {
    Object.assign(state, getDefaultReleaseState())
  },

  addReleaseToList (state: any, release: any) {
    state.releases.push(release)
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
