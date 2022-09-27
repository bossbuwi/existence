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
    disabled: [

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

const getDefaultDisabledSwitchableState = () => {
  return {
    disabled: []
  }
}

const state = getDefaultSettingsState()

const getters = {
  getBackendList: (state: any) => state.backend,
  getFrontendList: (state: any) => state.frontend,
  getSwitchableList: (state: any) => state.switchable,
  getDisabledSwitchableList: (state: any) => state.disabled,
  getSetting: (state: any) => state.item
}

const actions = {
  async GetBackendList ({ commit }: { commit: Commit }) {
    await axios.get('symphony/con/settings/setting/backend').then((result) => {
      commit('resetBackendState')
      const backendArr = result.data
      backendArr.forEach((element: any) => {
        commit('addBackend', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

	async GetFrontendList ({ commit }: { commit: Commit }) {
    await axios.get('symphony/con/settings/setting/frontend').then((result) => {
      commit('resetFrontendState')
      const frontendArr = result.data
      frontendArr.forEach((element: any) => {
        commit('addFrontend', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

	async GetSwitchableList ({ commit }: { commit: Commit }) {
    await axios.get('symphony/con/settings/setting/switchable').then((result) => {
      commit('resetSwitchableState')
      const switchableArr = result.data
      switchableArr.forEach((element: any) => {
        commit('addSwitchable', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

  async GetDisabledSwitchableFeatures ({ commit }: { commit: Commit }) {
    await axios.get('symphony/con/settings/setting/switchable/disabled').then((result) => {
      commit('resetDisabledSwitchableState')
      const switchableArr = result.data
      switchableArr.forEach((element: any) => {
        commit('addDisabledSwitchable', element)
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
  },

  SetDisabledSwitchableFeatures ({ commit }: { commit: Commit }, list: any) {
    list.forEach((element: any) => {
      commit('addDisabledSwitchable', element)
    })
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

  resetDisabledSwitchableState (state: any) {
    Object.assign(state, getDefaultDisabledSwitchableState())
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

  addDisabledSwitchable (state: any, setting: any) {
    state.disabled.push(setting)
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
