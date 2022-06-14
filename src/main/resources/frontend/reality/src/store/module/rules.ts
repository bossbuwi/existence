/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultRuleState = () => {
  return {
    rules: [

    ]
  }
}

const state = getDefaultRuleState()

const getters = {
  getRules: (state: any) => state.rules
}

const actions = {
  async GetRules ({ commit }: { commit: Commit }) {
    commit('resetRuleState')
    await axios.get('sonata/con/rules/index')
    .then((result) => {
      const rulesArr = result.data
      rulesArr.forEach((element: any) => {
        commit('addRule', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  }
}

const mutations = {
  resetRuleState (state: any) {
    Object.assign(state, getDefaultRuleState())
  },

  addRule (state: any, rule: any) {
    state.rules.push(rule)
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
