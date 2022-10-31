/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultFileState = () => {
  return {
    success: false
  }
}

const state = getDefaultFileState()

const getters = {
  isSuccess: (state: any) => state.success
}

const actions = {
  async PostFileUpload ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    commit('updateFileState', false)

    const formData = new FormData()
    formData.append('file', form)
    
    await axios.post('eligius/files/upload', formData, {
      headers: {
        'Content-type': 'multipart/form-data',
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      console.log(result.data)
      commit('updateFileState', true)
    }).catch((error) => {
      console.log(error.response.data)
      commit('clearError')
      commit('setError', error.response.data)
    })
  }
}

const mutations = {
  resetFileState (state: any) {
    Object.assign(state, getDefaultFileState())
  },

  updateFileState (state: any, isSuccess: boolean) {
    state.success = isSuccess
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
