const path = require('path')
const { defineConfig } = require('@vue/cli-service')
const mode = process.env.VUE_DEV_SERVER

module.exports = defineConfig({
  outputDir: path.resolve(__dirname, '../../static'),
  devServer: {
    proxy: mode
  },
  // devServer: {
  //   proxy: {
  //     '^/sonata': {
  //       target: 'http://localhost:8080',
  //       changeOrigin: true
  //     }
  //   }
  // },
  transpileDependencies: [
    'vuetify'
  ]
})
