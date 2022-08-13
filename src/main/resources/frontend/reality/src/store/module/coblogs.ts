/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultCoblogState = (fullReset: boolean) => {
  if (fullReset) {
    return {
      list: [

      ],
      coblog: {
        runday: '',
        nextRunday: '',
        system: [

        ]
      },
      ongoingCob: false
    }
  } else {
    return {
      list: [

      ]
    }
  }

}

const state = getDefaultCoblogState(true)

const getters = {
  getCoblogList: (state: any) => state.list,
  getCoblog: (state: any) => state.coblog,
  getRunday: (state: any) => state.coblog.runday,
  getNextRunday: (state: any) => state.coblog.nextRunday,
  getCobSystem: (state: any) => state.coblog.system,
  getCobStatus: (state: any) => state.ongoingCob
}

const actions = {
  async GetCoblogList ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    commit('resetCoblogList')
    const token = rootGetters.getToken
    await axios.get('requiem/con/coblogs/index', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      const coblogArr = result.data as any[]
      coblogArr.forEach((element: any) => {
        commit('addCoblogToList', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  }
}

const mutations = {
  resetCoblogState (state: any) {
    Object.assign(state, getDefaultCoblogState(true))
  },

  resetCoblogList (state: any) {
    Object.assign(state, getDefaultCoblogState(false))
  },

  addCoblogToList (state: any, coblog: any) {
    state.list.push(coblog)
  },

  setCobState (state: any, status: boolean) {
    state.ongoingCob = status
  },

  setCobSystem (state: any, system: any) {
    state.coblog.system = system
  },

  setCobRunday (state: any, runday: any) {
    state.coblog.runday = runday
  },

  setCobNextRunday (state: any, nextRunday: any) {
    state.coblog.nextRunday = nextRunday
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
