/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultAuthState = () => {
  return {
    user: {
      id: 0 as number,
      username: '' as string,
      roles: [] as string[],
      token: '' as string,
      created: {} as Date
    }
  }
}

const state = getDefaultAuthState()

const getters = {
  isAuthenticated: (state: any) => state.user.id > 0,
  getUserState: (state: any) => state.user,
  getToken: (state: any) => state.user.token
}

const actions = {
  async Login ({ commit }: { commit: Commit }, form: any) {
    let user = null
    await axios.post('concerto/con/login', form).then((result) => {
      user = result.data
      const dateNow = new Date()
      user.created = dateNow
    })
    commit('setUser', user)
  },

  async Logout ({ commit }: { commit: Commit }) {
    commit('resetAuthState')
  }
}

const mutations = {
  resetAuthState (state: any) {
    Object.assign(state, getDefaultAuthState())
  },

  setUser (state: any, user: any) {
    state.user = user
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
