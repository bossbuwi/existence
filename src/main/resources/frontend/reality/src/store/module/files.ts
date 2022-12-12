/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'
import { saveAs } from 'file-saver'

const getDefaultFileState = () => {
  return {
    uploadComplete: false,
    file: {},
    restoredItems: [],
    exportResponse: {}
  }
}

const state = getDefaultFileState()

const getters = {
  uploadComplete: (state: any) => state.uploadComplete,
  getFile: (state: any) => state.file,
  getRestoredItems: (state: any) => state.restoredItems,
  getExportResponse: (state: any) => state.exportResponse
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
  },

  async PostExportEvent ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, params: any) {
    const token = rootGetters.getToken

    await axios.post('eligius/files/backup/event', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      console.log(result.data)
    }).catch((error) => {
      console.log(error.response.data)
      commit('clearError')
      commit('setError', error.response.data)
    })
  },

  async GetFileDownload ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, filename: string) {
    const token = rootGetters.getToken

    await axios.get('eligius/files/backup/download', {
      headers: {
        Authorization: 'Bearer ' + token
      },
      params: {
        filename: filename
      },
      responseType: 'blob'
    }).then((response) => {
      saveAs(response.data, filename)
    }).catch((error) => {
      console.log(error.response.data)
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
  },

  setExportResponse (stateL any, response: any) {
    state.exportResponse = response
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
