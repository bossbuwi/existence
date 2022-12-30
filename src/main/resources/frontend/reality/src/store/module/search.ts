/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultSearchState = () => {
  return {
    searchBody: {},
    results: [

    ],
    hasResults: false
  }
}

const state = getDefaultSearchState()

const getters = {
  getSearchBody: (state: any) => state.searchBody,
  getSearchResults: (state: any) => state.results,
  hasResults: (state: any) => state.hasResults
}

const actions = {
  SetSearchBody ({ commit }: { commit: Commit }, body: any) {
    commit('resetSearchState')
    commit('setSearchBody', body)
  },

  async GetSearchEvents ({ commit }: { commit: Commit }, keyword: string) {
    await axios.get('eligius/con/search/events', {
      params: {
        keyword: keyword
      }
    }).then((result) => {
      const resArr = result.data as any[]
      resArr.forEach((element: any) => {
        commit('addResultToList', element)
      })
      commit('changeResultState', true)
    }).catch((error) => {
      console.log(error.response.data)
      commit('clearError')
      commit('setError', error.response.data)
    })
  },

  async PostFilterEvents ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, filters: any) {
    const token = rootGetters.getToken
    await axios.post('eligius/con/filters/events', state.searchBody, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      commit('resetSearchState')
      const resArr = result.data as any[]
      resArr.forEach((element: any) => {
        commit('addResultToList', element)
      })
      commit('changeResultState', true)
    }).catch((error) => {
      console.log(error.response.data)
      commit('clearError')
      commit('setError', error.response.data)
    })
  },
  
  ClearSearchResults ({ commit }: { commit: Commit }) {
    commit('resetSearchState')
  }
}

const mutations = {
  resetSearchState (state: any) {
    Object.assign(state, getDefaultSearchState())
  },

  setSearchBody (state: any, body: any) {
    state.searchBody = body
  },

  addResultToList (state: any, item: any) {
    state.results.push(item)
  },

  changeResultState (state: any, hasResults: boolean) {
    state.hasResults = hasResults
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
