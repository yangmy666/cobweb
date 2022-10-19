<template>
    <!--    顶部状态栏-->
    <div style="margin-bottom: 10px">
        <el-tag>容器概览</el-tag>
    </div>
    <view-table :max-height="maxHeight" ref="containerTable" :field-list="fieldList" :load-data="loadContainerTable">
        <template #operation="scope">
            <SubmitBtn type="success" text="启动" :func="startNodeContainer" :params="scope.row"/>
            <SubmitBtn type="warning" text="停止" :func="stopNodeContainer" :params="scope.row"/>
            <ConfirmSubmitBtn type="danger" text="删除" :func="deleteNodeContainer" :params="scope.row"/>
            <el-button style="margin: 5px" size="small" @click="openContainerLog(scope.row)">查看实时日志</el-button>
        </template>
    </view-table>
    <LogView ref="logViewRef"/>
</template>

<script setup>
import {ref} from 'vue'
import {deleteContainer, listClusterContainer, startContainer, stopContainer} from "@/api/containerApi";
import {ElMessage} from "element-plus";
import SubmitBtn from "@/components/SubmitBtn";
import ConfirmSubmitBtn from "@/components/ConfirmSubmitBtn";
import ViewTable from "@/components/ViewTable";
import {fieldList} from '@/pages/container/list';
import LogView from '@/pages/cluster/node/container/LogView'

const containerTable=ref()
const maxHeight=ref(window.innerHeight-90)
const logViewRef=ref()

defineExpose({
    refresh
})

//加载表格数据
function loadContainerTable(dataList){
    listClusterContainer().then(res=>{
        dataList.value=res.data
    })
}

//刷新表格
function refresh(){
    containerTable.value.refresh()
}

//启动容器
function startNodeContainer(row){
    return startContainer(row.ip,row.containerId).then(res=>{
        if(res.code===200){
            ElMessage.success("启动成功")
            refresh()
        }
    })
}

//停止容器
function stopNodeContainer(row){
    return stopContainer(row.ip,row.containerId).then(res=>{
        if(res.code===200){
            ElMessage.success("停止成功")
            refresh()
        }
    })
}

//删除容器
function deleteNodeContainer(row){
    return deleteContainer(row.ip,row.containerId).then(res=>{
        if(res.code===200){
            ElMessage.success("删除成功")
            refresh()
        }
    })
}

//查看容器实时日志
function openContainerLog(row){
    logViewRef.value.openLog(row.ip,row.containerId,row.names)
}

</script>

<style scoped>

</style>
