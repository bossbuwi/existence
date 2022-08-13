/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultEventTypeState = () => {
  return {
    list: [

    ]
  }
}

const state = getDefaultEventTypeState()

const getters = {
  getEventTypesList: (state: any) => state.list
}

const actions = {
  async GetEventTypesList ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    commit('resetEventTypeState')
    const token = rootGetters.getToken
    await axios.get('sonata/con/event-types/index', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      const typesArr = result.data as any[]
      typesArr.forEach((element: any) => {
        commit('addEventTypeToList', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  }
}

const mutations = {
  resetEventTypeState (state: any) {
    Object.assign(state, getDefaultEventTypeState())
  },

  addEventTypeToList (state: any, eventtype: any) {
    state.list.push(eventtype)
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
