<template>
  <div class="TESTMIKE">
    <v-form>
      <vue-editor
        v-model="componentForm.content"
        placeholder="Input text here.."
        :editorToolbar="toolbar"
      ></vue-editor>
      <v-btn
        type="submit"
        @click="submit"
      >Submit</v-btn>
    </v-form>
    <v-card>
      <v-card-title>Test Content</v-card-title>
      <!-- <v-card-text v-html="componentDetail.details"> -->
      <v-card-text>
      </v-card-text>
    </v-card>
  </div>
</template>
<script>
import Vue from 'vue'
import { mapActions, mapGetters } from 'vuex'
import { VueEditor } from 'vue2-editor'

export default Vue.extend({
  name: 'ComponentForm',

  components: {
    VueEditor
  },

  data () {
    return {
      toolbar: [
        ['bold', 'italic', 'underline', 'strike'],
        [{ align: '' }, { align: 'center' }, { align: 'right' }, { align: 'justify' }],
        [{ list: 'ordered' }, { list: 'bullet' }],
        [{ color: [] }, { background: [] }],
        ['image'],
        ['clean']
      ],
      componentForm: {
        content: ''
      }
    }
  },

  methods: {
    submit () {
      console.log('bitch')
    }
  }
})
</script>
