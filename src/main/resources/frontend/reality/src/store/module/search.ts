/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultSearchState = () => {
  return {
    results: [

    ],
    hasResults: false
  }
}

const state = getDefaultSearchState()

const getters = {
  getSearchResults: (state: any) => state.about,
  hasResults: (state: any) => state.hasResults
}

const actions = {
  async GetSearchEvents({ commit }: { commit: Commit }, keyword: string) {
    await axios.get('eligius/con/search/events', {
      params: {
        keyword: keyword
      }
    }).then((result) => {
      console.log(result.data)
      commit('resetSearchState')
      const resArr = result.data as any[]
      resArr.forEach((element: any) => {
        commit('addResultToList', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  }
}

const mutations = {
  resetSearchState (state: any) {
    Object.assign(state, getDefaultSearchState())
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
