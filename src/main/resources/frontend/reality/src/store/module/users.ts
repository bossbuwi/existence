/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultState = () => {
  return {
    users: {
      count: 0
    },
    details: {

    }
  }
}

const state = getDefaultState()

const getters = {
  getUserCount: (state: any) => state.users.count,
  getUserDetails: (state: any) => state.details
}

const actions = {
  async GetUserCount ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    const token = rootGetters.getToken
    let count = 0
    await axios.get('symphony/con/users/count', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      count = result.data
      commit('setUserCount', count)
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

  async GetUserDetails ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    commit('resetUserState')
    const token = rootGetters.getToken
    const userId = rootGetters.getUserState.id
    const url = '/symphony/users/user/' + userId + '/details'
    await axios.get(url, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      commit('setUserDetails', result.data)
    }).catch((error) => {
      console.log(error.response.data)
    })
  }
}

const mutations = {
  resetUserState (state: any) {
    Object.assign(state, getDefaultState())
  },

  setUserCount (state: any, count: number) {
    state.users.count = count
  },

  setUserDetails (state: any, details: any) {
    state.details = details
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
