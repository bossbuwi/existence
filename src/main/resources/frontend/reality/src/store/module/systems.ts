/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'
import { System } from 'src/models/system'

const getDefaultSystemState = () => {
  return {
    systems: {
      count: 0
    },
    list: [

    ],
    system: {
      id: 0 as number,
      global_prefix: '' as string,
      release: '' as string,
      description: '' as string,
      url: '' as string,
      owners: '' as string,
      machine: '' as string,
      zones: [] as string[],
      zone_names: [] as string[],
      zone_prefixes: [] as string[],
      creation_date: '' as string,
      last_changed_date: '' as string
    }
  }
}

const state = getDefaultSystemState()

const getters = {
  getSystemCount: (state: any) => state.systems.count,
  getSystemsList: (state: any) => state.list,
  getSystem: (state: any) => state.system
}

const actions = {
  async GetSystemCount ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    const token = rootGetters.getToken
    let count = 0
    await axios.get('sonata/con/systems/count', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      count = result.data
      commit('setSystemCount', count)
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

  async GetSystemsList ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    commit('resetSystemState')
    const token = rootGetters.getToken
    await axios.get('sonata/con/systems/index', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      const systemArr = result.data as System[]
      systemArr.forEach((element: System) => {
        commit('addSystemToList', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

  async PostFullSystem ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    await axios.post('sonata/systems/system/full', form, {
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

  async PutFullSystem ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    await axios.put('sonata/systems/system/full', form, {
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

  async DeleteSystem ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, id: any) {
    const token = rootGetters.getToken
    await axios.delete('sonata/systems/system/' + id, {
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

  SetSystem ({ commit }: { commit: Commit }, args: any) {
    commit('resetSystemState')
    commit('setSystem', args)
  }
}

const mutations = {
  resetSystemState (state: any) {
    Object.assign(state, getDefaultSystemState())
  },

  setSystemCount (state: any, count: number) {
    state.systems.count = count
  },

  addSystemToList (state: any, system: System) {
    state.list.push(system)
  },

  setSystem (state: any, system: any) {
    state.system = system
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
