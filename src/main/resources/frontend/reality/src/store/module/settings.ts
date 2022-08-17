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
      id: 0 as number,
      key: '' as string,
      value: '' as string,
      length: 0 as number,
      type: '' as string,
      desc: '' as string,
      default_value: '' as string,
      valid_values: '' as string,
      added_by: '' as string,
      last_changed_by: '' as string,
      last_changed_date: '' as string
    }
  }
}

const getDefaultBackendState = () => {
  return {
    backend: []
  }
}

const getDefaultFrontendState = () => {
  return {
    frontend: []
  }
}

const getDefaultSwitchableState = () => {
  return {
    switchable: []
  }
}

const state = getDefaultSettingsState()

const getters = {
  getBackendList: (state: any) => state.backend,
  getFrontendList: (state: any) => state.frontend,
  getSwitchableList: (state: any) => state.switchable,
  getSetting: (state: any) => state.item
}

const actions = {
  async GetBackendList ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    commit('resetBackendState')
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
    commit('resetFrontendState')
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
    commit('resetSwitchableState')
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
  },

  async PutSetting ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    await axios.put('symphony/settings/setting', form, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      commit('setSetting', result.data)
    }).catch((error) => {
      console.log(error.response)
      commit('clearError')
      commit('setError', error.response.data)
    })
  },

  async SyncSettings ({ commit, dispatch }: { commit: Commit, dispatch: any }, type: any) {
    switch (type) {
      case 'B':
        dispatch('GetBackendList')
        break;
      case 'F':
        dispatch('GetFrontendList')
        break;
      case 'SW':
        dispatch('GetSwitchableList')
        break;
      default:
        console.log('Is it real, or is it not?')
        break;
    }
  }
}

const mutations = {
  resetSettingsState (state: any) {
    Object.assign(state, getDefaultSettingsState())
  },

  resetBackendState (state: any) {
    Object.assign(state, getDefaultBackendState())
  },

  resetFrontendState (state: any) {
    Object.assign(state, getDefaultFrontendState())
  },

  resetSwitchableState (state: any) {
    Object.assign(state, getDefaultSwitchableState())
  },

  addBackend (state: any, setting: any) {
    state.backend.push(setting)
  },

	addFrontend (state: any, setting: any) {
    state.frontend.push(setting)
  },

	addSwitchable (state: any, setting: any) {
    state.switchable.push(setting)
  },

  setSetting (state: any, setting: any) {
    state.item = setting
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
