<template>
    <!--查看容器运行日志抽屉-->
    <el-drawer v-model="drawerVisible" size="80%" :with-header="false" @close="stopLog" :show-close="false">
        <div>
            <el-tag :type="status" style="margin-bottom: 10px;margin-right: 10px">{{logTitle}}</el-tag>
            <el-button size="small" @click="suspend=true">暂停</el-button>
            <el-button size="small" type="primary" @click="suspend=false">继续</el-button>
        </div>
        <div style="height: calc(100vh - 75px)">
            <el-scrollbar ref="scrollbarRef" style="background-color: black;padding: 3px">
                <div id="logContext">
                    <div v-for="item in logList" :key="item">
                        <span style="font-size: 14px;font-family: 宋体;color: white">{{item}}</span>
                    </div>
                </div>
            </el-scrollbar>
        </div>
    </el-drawer>
</template>

<script setup>
import {ref, nextTick} from 'vue'

const drawerVisible=ref(false)
const logTitle=ref()
const status=ref('success')
const logList=ref([])
const scrollbarRef=ref()
const suspend=ref(false)

defineExpose({
    openLog
})

let ws
//打开日志输出
function openLog(ip,containerId,title){
    logTitle.value=title
    drawerVisible.value=true
    ws=new WebSocket(import.meta.env.VITE_WS_BASE_URL+
            '/dockerLog/'+ip+'/'+containerId)
    ws.onopen=()=>{
        console.log("打开日志输出")
    }
    ws.onmessage=(m)=>{
        if(suspend.value){
            return
        }
        logList.value=logList.value.concat(m.data)
        nextTick(()=>{
            //滚动到底部最新一行
            const height=document.getElementById("logContext").scrollHeight
            scrollbarRef.value.setScrollTop(height)
        })
    }
    ws.onclose=()=>{
        console.log("关闭日志输出")
    }
    ws.onerror=(e)=>{
        status.value='info'
        console.log(e)
    }
}

//关闭日志输出
function stopLog(){
    ws.close()
    logTitle.value=""
    logList.value=[]
}

</script>

<style scoped>

</style>
