/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultReleaseState = () => {
  return {
    releases: [

    ],
    release: {
      id: 0 as number,
      name: '' as string,
      system_count: 0 as number
    }
  }
}

const state = getDefaultReleaseState()

const getters = {
  getReleasesList: (state: any) => state.releases,
  getRelease: (state: any) => state.release
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
  },

  async PostRelease ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    await axios.post('sonata/releases/release', form, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      console.log(result.data)
    }).catch((error) => {
      console.log(error.response.data)
      commit('clearError')
      commit('setError', error.response.data)
    })
  },

  SetRelease ({ commit }: { commit: Commit }, args: any) {
    commit('resetReleaseState')
    commit('setRelease', args)
  }
}

const mutations = {
  resetReleaseState (state: any) {
    Object.assign(state, getDefaultReleaseState())
  },

  addReleaseToList (state: any, release: any) {
    state.releases.push(release)
  },

  setRelease (state: any, release: any) {
    state.release = release
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
