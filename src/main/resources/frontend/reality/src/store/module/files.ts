/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultFileState = () => {
  return {
    success: false,
    ongoingBR: false
    file: {}
  }
}

const state = getDefaultFileState()

const getters = {
  isSuccess: (state: any) => state.success,
  isOngoing: (state: any) => state.ongoingBR,
  getFile: (state: any) => state.file
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
      commit('setFile', result.data)
      commit('updateFileState', true)
    }).catch((error) => {
      console.log(error.response.data)
      commit('clearError')
      commit('setError', error.response.data)
    })
  },

  async PostRestoreEvents ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, param: any) {
    commit('updateProgress', true)
    const token = rootGetters.getToken

    await axios.post('sonata/events/import', null, {
      headers: {
        Authorization: 'Bearer ' + token
      },
      params: {
        filename: param
      }
    }).then((result) => {
      console.log(result.data)
      commit('updateProgress', false)
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
  },

  updateProgress (state: any, isOngoing: boolean) {
    state.ongoingBR = isOngoing
  },

  setFile (state: any, file: any) {
    state.file = file
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
