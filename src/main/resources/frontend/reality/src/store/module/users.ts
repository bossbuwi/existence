/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultUserState = () => {
  return {
    users: {
      count: 0
    },
    list: [

    ],
    details: {

    },
    userItem: {
      id: 0 as number,
      username: '' as string,
      roles: [] as string[]
    }
  }
}

const state = getDefaultUserState()

const getters = {
  getUserCount: (state: any) => state.users.count,
  getUserDetails: (state: any) => state.details,
  getUser: (state: any) => state.userItem,
  getUserList: (state: any) => state.list
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
  },

  async GetUserList ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    commit('resetUserState')
    const token = rootGetters.getToken
    const url = '/symphony/users/index'
    await axios.get(url, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      commit('resetUserState')
      const userArr = result.data as any[]
      userArr.forEach((element: any) => {
        commit('addUserToList', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

  async PutUserRoles ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    await axios.put('symphony/users/user/modify-roles', form, {
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

  SetUserItem ({ commit }: { commit: Commit }, args: any) {
    commit('resetUserState')
    commit('setUserItem', args)
  }
}

const mutations = {
  resetUserState (state: any) {
    Object.assign(state, getDefaultUserState())
  },

  setUserCount (state: any, count: number) {
    state.users.count = count
  },

  setUserDetails (state: any, details: any) {
    state.details = details
  },

  setUserItem (state: any, user: any) {
    state.userItem = user
  },

  addUserToList (state: any, user: any) {
    state.list.push(user)
  },
}

export default {
  state,
  getters,
  actions,
  mutations
}
