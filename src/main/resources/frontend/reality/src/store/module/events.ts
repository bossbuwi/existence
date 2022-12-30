/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'

const getDefaultEventState = () => {
  return {
    events: {
      count: 0 as number
    },
    list: [

    ],
    latestEvent: {
      id: 0 as number,
      system: '' as string,
      zones: '' as string,
      event_types: '' as string,
      start_date: '' as string,
      end_date: '' as string,
      last_changed_by: '' as string
    },
    event: {
      id: 0 as number,
      machine: '' as string,
      system: '' as string,
      zones: [] as string[],
      event_types: [] as string[],
      start_date: '' as string,
      end_date: '' as string,
      jira_case: '' as string,
      features_on: '' as string,
      features_off: '' as string,
      compiled_sources: '' as string,
      api_used: '' as string,
      created_by: '' as string,
      creation_date: '' as string,
      last_changed_by: '' as string,
      last_changed_date: '' as string
    }
  }
}

const state = getDefaultEventState()

const getters = {
  getEventCount: (state: any) => state.events.count,
  getLatestEvent: (state:any) => state.latestEvent,
  getEventsList: (state: any) => state.list,
  getEvent: (state: any) => state.event
}

const actions = {
  async GetEventCount ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    const token = rootGetters.getToken
    let count = 0
    await axios.get('sonata/con/events/count', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      count = result.data
      commit('setEventCount', count)
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

  async GetLatestEvent ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    const token = rootGetters.getToken
    await axios.get('sonata/con/events/latest', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      commit('setLatestEvent', result.data)
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

  async GetEventsList ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }) {
    commit('resetEventState')
    const token = rootGetters.getToken
    await axios.get('sonata/con/events/index', {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      const eventArr = result.data as Event[]
      eventArr.forEach((element: Event) => {
        commit('addEventToList', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  },

  async PostEvent ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    await axios.post('sonata/events/event', form, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      commit('setEvent', result.data)
    }).catch((error) => {
      console.log(error.response.data)
      commit('clearError')
      commit('setError', error.response.data)
    })
  },

  async PutEvent ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    await axios.put('sonata/events/event', form, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      commit('setEvent', result.data)
    }).catch((error) => {
      console.log(error.response.data)
      commit('clearError')
      commit('setError', error.response.data)
    })
  },

  async DeleteEvent ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, form: any) {
    const token = rootGetters.getToken
    const url = 'sonata/events/event/' + form.id
    await axios.delete(url, {
      headers: {
        Authorization: 'Bearer ' + token
      }
    }).then((result) => {
      commit('setEvent', result.data)
    }).catch((error) => {
      console.log(error.response.data)
      commit('clearError')
      commit('setError', error.response.data)
    })
  },

  SetEvent ({ commit }: { commit: Commit }, args: any) {
    commit('resetEventState')
    commit('setEvent', args)
  }
}

const mutations = {
  resetEventState (state: any) {
    Object.assign(state, getDefaultEventState())
  },

  setEventCount (state: any, count: number) {
    state.events.count = count
  },

  addEventToList (state: any, event: Event) {
    state.list.push(event)
  },

  setLatestEvent (state: any, event: any) {
    state.latestEvent.id = event.id
    state.latestEvent.system = event.system
    state.latestEvent.start_date = event.start_date
    state.latestEvent.end_date = event.end_date
    state.latestEvent.last_changed_by = event.last_changed_by

    const zonesArr: string[] = event.zones
    state.latestEvent.zones = zonesArr.join(', ')

    const eventTypesArr: string[] = event.event_types
    state.latestEvent.event_types = eventTypesArr.join(', ')
  },

  setEvent (state: any, event: any) {
    state.event = event
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
