import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  base: './',
  server: {
    port: 80,
    strictPort:true,
    open: true//启动后打开浏览器
  },
  build: {
    minify: 'terser',
    outDir: '../src/main/resources/static',
    emptyOutDir: true
  },

  resolve: {
    // 配置路径别名
    alias: [
        { find: '@', replacement: resolve(__dirname, 'src') }
    ],
    // 忽略后缀名的配置选项, 添加 .vue 选项时要记得原本默认忽略的选项也要手动写入
    extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
  },
  envDir:'./'
})
