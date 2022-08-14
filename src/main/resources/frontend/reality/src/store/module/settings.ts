/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultSettingsState = () => {
  return {
    backend: [

    ],
		frontend: [

		],
		switchable: [

		],
    item: {
      
    }
  }
}

const state = getDefaultSettingsState()

const getters = {
  getBackendList: (state: any) => state.backend,
  getFrontendList: (state: any) => state.frontend,
  getSwitchableList: (state: any) => state.switchable
}

const actions = {
  async GetBackendList ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    commit('resetSettingsState')
		const token = rootGetters.getToken
    await axios.get('symphony/settings/setting/backend', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      const backendArr = result.data
      backendArr.forEach((element: any) => {
        commit('addBackend', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

	async GetFrontendList ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    commit('resetSettingsState')
		const token = rootGetters.getToken
    await axios.get('symphony/settings/setting/frontend', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      const frontendArr = result.data
      frontendArr.forEach((element: any) => {
        commit('addFrontend', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

	async GetSwitchableList ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    commit('resetSettingsState')
		const token = rootGetters.getToken
    await axios.get('symphony/settings/setting/switchable', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      const switchableArr = result.data
      switchableArr.forEach((element: any) => {
        commit('addSwitchable', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  }
}

const mutations = {
  resetSettingsState (state: any) {
    Object.assign(state, getDefaultSettingsState())
  },

  addBackend (state: any, setting: any) {
    state.backend.push(setting)
  },

	addFrontend (state: any, setting: any) {
    state.frontend.push(setting)
  },

	addSwitchable (state: any, setting: any) {
    state.switchable.push(setting)
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
