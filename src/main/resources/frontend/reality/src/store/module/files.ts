/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'
import { saveAs } from 'file-saver'

const getDefaultFileState = () => {
  return {
    fileResponse: {
      filename: '',
      extension: '',
      size: 0,
      saved: false
    },
    isProcessComplete: false,
    uploadComplete: false,
    file: {},
    restoredItems: [],
    exportResponse: {
      filename: '',
      extension: '',
      size: 0,
      saved: false
    }
  }
}

const state = getDefaultFileState()

const getters = {
  getFileResponse: (state: any) => state.fileResponse,
  getProcessStatus: (state: any) => state.isProcessComplete,
  uploadComplete: (state: any) => state.uploadComplete,
  exportComplete: (state: any) => state.exportResponse.saved,
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
      // commit('updateUploadStatus', true)
      commit('updateUploadStatus')
    }).catch((error) => {
      console.log(error.response.data)
      commit('clearError')
      commit('setError', error.response.data)
    })
  },

  async PostRestoreEvents ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, param: any) {
    const token = rootGetters.getToken

    await axios.post('eligius/records/restore/event', null, {
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
    commit('resetFileState')

    const token = rootGetters.getToken

    await axios.post('eligius/records/backup/event', null, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      console.log(result.data)
      commit('setExportResponse', result.data)
    }).catch((error) => {
      console.log(error.response.data)
      commit('clearError')
      commit('setError', error.response.data)
    })
  },

  async GetFileDownload ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, filename: string) {
    const token = rootGetters.getToken

    await axios.get('eligius/files/download', {
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
      commit('clearError')
      commit('setError', error.response.data)
    })
  },

  async PostGenerateTemplate ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, model: string) {
    const token = rootGetters.getToken

    commit('resetFileState')

    await axios.post('eligius/housekeeping/template/generate', null, {
      headers: {
        Authorization: 'Bearer ' + token
      },
      params: {
        model: model
      }
    }).then((result) => {
      console.log(result.data)
      commit('setFileResponse', result.data)
      commit('setProcessStatus', true)
    }).catch((error) => {
      console.log(error)
      commit('clearError')
      commit('setError', error.response.data)
    })
  }
}

const mutations = {
  resetFileState (state: any) {
    Object.assign(state, getDefaultFileState())
  },

  setFileResponse (state: any, response: any) {
    state.fileResponse = response
  },

  setProcessStatus (state: any, processStatus: boolean) {
    state.isProcessComplete = state.fileResponse.saved
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

  setExportResponse (state: any, response: any) {
    state.exportResponse = response
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
