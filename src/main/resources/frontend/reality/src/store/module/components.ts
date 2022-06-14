/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultComponentState = () => {
  return {
    list: [

    ],
    component: {

    }
  }
}

const state = getDefaultComponentState()

const getters = {
  getComponentsList: (state: any) => state.list,
  getComponent: (state: any) => state.component
}

const actions = {
  async GetComponentList ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    commit('resetComponentState')
    const token = rootGetters.getToken
    await axios.get('requiem/con/components/index', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      const componentArr = result.data as any[]
      componentArr.forEach((element: any) => {
        commit('addComponentToList', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

  async PostComponent ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    let count = 0
    await axios.post('requiem/components/component', form, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      commit('setComponent', result.data)
      console.log(result.data)
    })
  }
}

const mutations = {
  resetComponentState (state: any) {
    Object.assign(state, getDefaultComponentState())
  },

  setComponent (state: any, component: any) {
    state.component = component
  },

  addComponentToList (state: any, coblog: any) {
    state.list.push(coblog)
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
