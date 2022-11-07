/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultFileState = () => {
  return {
    uploadComplete: false,
    file: {},
    restoredItems: []
  }
}

const state = getDefaultFileState()

const getters = {
  uploadComplete: (state: any) => state.uploadComplete,
  getFile: (state: any) => state.file,
  getRestoredItems: (state: any) => state.restoredItems
}

const actions = {
  async PostFileUpload ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    commit('updateUploadStatus', false)

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
      commit('updateUploadStatus', true)
    }).catch((error) => {
      console.log(error.response.data)
      commit('clearError')
      commit('setError', error.response.data)
    })
  },

  async PostImportEvents ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, param: any) {
    const token = rootGetters.getToken

    await axios.post('eligius/files/restore/event', null, {
      headers: {
        Authorization: 'Bearer ' + token
      },
      params: {
        filename: param
      }
    }).then((result) => {
      console.log(result.data)
      commit('setRestoredItems', result.data)
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

  updateUploadStatus (state: any, uploadComplete: boolean) {
    state.uploadComplete = uploadComplete
  },

  setFile (state: any, file: any) {
    state.file = file

    switch (file.extension) {
      case 'xlsx':
        state.file.type = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        break
      case 'xls':
        state.file.type = 'application/vnd.ms-excel'
        break
      default:
        break
    }
  },

  setRestoredItems (state: any, items: any) {
    state.restoredItems = items
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
