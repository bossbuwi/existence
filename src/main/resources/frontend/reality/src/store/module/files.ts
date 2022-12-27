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
      saved: false,
      type: ''
    },
    isProcessComplete: false,
    restoredItems: [],
  }
}

const state = getDefaultFileState()

const getters = {
  getFileResponse: (state: any) => state.fileResponse,
  getProcessStatus: (state: any) => state.isProcessComplete,
  getRestoredItems: (state: any) => state.restoredItems
}

const actions = {
  async PostFileUpload ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    commit('resetFileState')

    const formData = new FormData()
    formData.append('file', form)

    await axios.post('eligius/files/upload', formData, {
      headers: {
        'Content-type': 'multipart/form-data',
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      console.log(result.data)
      commit('setFileResponse', result.data)
      commit('setProcessStatus')
      commit('setFileType')
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
      commit('setFileResponse', result.data)
      commit('setProcessStatus')
      commit('setFileType')
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
      // Read the blob response as JSON
      // Create a new filereader instance
      const reader = new FileReader()
      // Set a function to execute when the reader loads
      reader.onload = () => {
        // Parse the response as JSON
        const err = JSON.parse(reader.result as string)
        // If the error's status is 400, modify the message because the server side
        // message for error code 400 is not useful for the typical end user
        if (err.status === 400) {
          err.message = 'There is a problem downloading the file. Please wait a moment and try again. ' +
            'If the error persists, contact an admin.'
        }
        commit('clearError')
        commit('setError', err)
      }
      // Call the reader to read the error response
      reader.readAsText(error.response.data)
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

  setFileResponse (state: any, response: any) {
    state.fileResponse = response
  },

  setProcessStatus (state: any) {
    state.isProcessComplete = state.fileResponse.saved
  },

  setFileType (state: any) {
    switch (state.fileResponse.extension) {
      case 'xlsx':
        state.fileResponse.type = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        break
      case 'xls':
        state.fileResponse.type = 'application/vnd.ms-excel'
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
