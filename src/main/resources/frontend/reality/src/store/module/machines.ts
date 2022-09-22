/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultMachineState = () => {
  return {
    machines: {
      count: 0
    },
    list: [

    ],
    machine: {
      id: 0 as number,
      name: '' as string,
      creation_date: '' as string,
      last_changed_date: '' as string,
      system_count: 0 as number,
      systems: [] as string[]
    }
  }
}

const state = getDefaultMachineState()

const getters = {
  getMachineCount: (state: any) => state.machines.count,
  getMachineList: (state: any) => state.list,
  getMachine: (state: any) => state.machine
}

const actions = {
  async GetMachineCount ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    const token = rootGetters.getToken
    let count = 0
    await axios.get('sonata/con/machines/count', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      count = result.data
      commit('setMachineCount', count)
    })
  },

  async GetMachinesList ({ commit }: { commit: Commit }) {
    await axios.get('sonata/con/machines/index').then((result) => {
      commit('resetMachineState')
      const machineArr = result.data as any[]
      machineArr.forEach((element: any) => {
        commit('addMachineToList', element)
      })
    })
  },

  async PostMachine ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    await axios.post('sonata/machines/machine', form, {
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

  async PutMachine ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    await axios.put('sonata/machines/machine', form, {
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

  async DeleteMachine ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, id: any) {
    const token = rootGetters.getToken
    await axios.delete('sonata/machines/machine/' + id, {
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

  SetMachine ({ commit }: { commit: Commit }, args: any) {
    commit('resetMachineState')
    commit('setMachine', args)
  }
}

const mutations = {
  resetMachineState (state: any) {
    Object.assign(state, getDefaultMachineState())
  },

  setMachineCount (state: any, count: number) {
    state.machines.count = count
  },

  addMachineToList (state: any, machine: any) {
    state.list.push(machine)
  },

  setMachine (state: any, machine: any) {
    state.machine = machine
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
