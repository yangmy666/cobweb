<template>
    <div>
        <div style="height: calc(100vh - 75px)">
            <el-scrollbar ref="scrollbarRef" style="background-color: black;padding: 3px">
                <div id="printContext">
                    <div v-for="item in lineList" :key="item">
                        <span style="font-size: 14px;font-family: 宋体;color: white">{{item}}</span>
                    </div>
                </div>
            </el-scrollbar>
        </div>
    </div>
</template>

<script setup>
import {ref, nextTick} from 'vue'
import {ElMessage} from "element-plus";

const lineList=ref([])
const scrollbarRef=ref()

defineExpose({
    exec,
    stop
})

let ws
//接入任务输出
function exec(taskId){
    ws=new WebSocket(import.meta.env.VITE_WS_BASE_URL+
            '/execRelease/'+taskId)
    ws.onopen=()=>{
        console.log("执行任务")
    }
    ws.onmessage=(m)=>{
        if(m.data==='{over}'){
            ElMessage.success('发布任务执行完毕！')
            return
        }
        lineList.value=lineList.value.concat(m.data)
        nextTick(()=>{
            //滚动到底部最新一行
            const height=document.getElementById("printContext").scrollHeight
            scrollbarRef.value.setScrollTop(height)
        })
    }
    ws.onclose=()=>{
        console.log("停止任务")
    }
    ws.onerror=(e)=>{
        console.log(e)
    }
}

//断开任务输出
function stop(){
    ws.close()
    lineList.value=[]
}

</script>

<style scoped>

</style>
