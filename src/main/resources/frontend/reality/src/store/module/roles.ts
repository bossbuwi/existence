/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultRoleState = () => {
  return {
    roles: [

    ]
  }
}

const state = getDefaultRoleState()

const getters = {
  getRoles: (state: any) => state.roles
}

const actions = {
  async GetRoles ({ commit }: { commit: Commit }) {
    commit('resetRoleState')
    await axios.get('symphony/con/roles/index')
    .then((result) => {
      const rolesArr = result.data
      rolesArr.forEach((element: any) => {
        commit('addRole', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  }
}

const mutations = {
  resetRoleState (state: any) {
    Object.assign(state, getDefaultRoleState())
  },

  addRole (state: any, role: any) {
    state.roles.push(role)
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
