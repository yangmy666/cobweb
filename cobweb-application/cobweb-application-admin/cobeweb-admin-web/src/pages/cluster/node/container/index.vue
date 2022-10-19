<template>
    <div>
        <view-table :max-height="maxHeight" ref="containerTable" :field-list="fieldList" :load-data="loadContainerTable">
            <template #operation="scope">
                <SubmitBtn type="success" text="启动" :func="startNodeContainer" :params="scope.row"/>
                <SubmitBtn type="warning" text="停止" :func="stopNodeContainer" :params="scope.row"/>
                <ConfirmSubmitBtn type="danger" text="删除" :func="deleteNodeContainer" :params="scope.row"/>
                <el-button style="margin: 5px" size="small" @click="openContainerLog(scope.row)">查看实时日志</el-button>
            </template>
        </view-table>

        <LogView ref="logViewRef"/>
    </div>
</template>

<script setup>
import {ref} from 'vue'
import {deleteContainer, listContainer, startContainer, stopContainer} from "@/api/containerApi";
import {ElMessage} from "element-plus";
import SubmitBtn from "@/components/SubmitBtn";
import ConfirmSubmitBtn from "@/components/ConfirmSubmitBtn";
import ViewTable from "@/components/ViewTable";
import {fieldList} from '@/pages/cluster/node/container/list';
import LogView from '@/pages/cluster/node/container/LogView'

const containerTable=ref()
const maxHeight=ref(window.innerHeight-150)
const logViewRef=ref()

const props = defineProps({
    ip:{
        type:String
    }
})

defineExpose({
    refresh
})

//加载表格数据
function loadContainerTable(dataList){
    listContainer(props.ip).then(res=>{
        dataList.value=res.data
    })
}

//刷新表格
function refresh(){
    containerTable.value.refresh()
}

//启动容器
function startNodeContainer(row){
    return startContainer(props.ip,row.containerId).then(res=>{
        if(res.code===200){
            ElMessage.success("启动成功")
            refresh()
        }
    })
}

//停止容器
function stopNodeContainer(row){
    return stopContainer(props.ip,row.containerId).then(res=>{
        if(res.code===200){
            ElMessage.success("停止成功")
            refresh()
        }
    })
}

//删除容器
function deleteNodeContainer(row){
    return deleteContainer(props.ip,row.containerId).then(res=>{
        if(res.code===200){
            ElMessage.success("删除成功")
            refresh()
        }
    })
}

//查看容器实时日志
function openContainerLog(row){
    logViewRef.value.openLog(props.ip,row.containerId,row.names)
}

</script>

<style scoped>

</style>
