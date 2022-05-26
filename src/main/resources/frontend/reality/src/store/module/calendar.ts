/* eslint-disable */
import axios from 'axios'
import { Commit } from 'vuex'
import { Event } from '@/models/event'

const getDefaultState = () => {
  return {
    range: [

    ]
  }
}

const state = getDefaultState()

const getters = {
  getEventsOnDateRange: (state: any) => state.range
}

const actions = {
  async GetEventsOnDateRange ({ commit, getters, rootGetters }: { commit: Commit, getters: any, rootGetters: any }, date: any) {
    commit('resetCalendarState')
    const token = rootGetters.getToken
    await axios.get('sonata/con/events/date/range', {
      headers: {
        Authorization: 'Bearer ' + token
      },
      params: {
        start: date.startDate,
        end: date.endDate
      }
    }).then((result) => {
      const eventArr = result.data as Event[]
      eventArr.forEach((element: Event) => {
        commit('addEventOnRange', element)
      })
    }).catch((error) => {
      console.log(error.response.data)
    })
  }
}

const mutations = {
  resetCalendarState (state: any) {
    Object.assign(state, getDefaultState())
  },

  addEventOnRange (state: any, event: Event) {
    state.range.push(event)
  }
}

export default {
  state,
  getters,
  actions,
  mutations
}
