<template>
    <!--    顶部状态栏-->
    <div style="margin-bottom: 10px">
        <el-tag>发布任务</el-tag>
    </div>
    <!--任务表格-->
    <view-table ref="takTableRef" :field-list="fieldList" :load-data="loadReleaseTaskTable">
        <template #header>
            <el-button size="small" type="primary" @click="router.push('/release/editForm')">新建</el-button>
        </template>
        <template #operation="scope">
            <el-button size="small" type="primary" @click="exec(scope.row)">执行</el-button>
            <el-button size="small" type="primary" @click="router.push('/release/editForm?formData='+JSON.stringify(scope.row))">编辑</el-button>
            <ConfirmSubmitBtn type="danger" :params="scope.row" :func="deleteTask" text="删除"/>
        </template>
    </view-table>
    <!--正在执行的认任务输出-->
    <el-drawer direction="btt" size="100%" :with-header="false" :close-on-click-modal="false"
            v-model="printDrawer">
        <div style="margin-bottom: 10px">
            <el-button type="danger" size="small" @click="stop">关闭</el-button>
        </div>
        <ExecPrint ref="ExecPrintRef"/>
    </el-drawer>

</template>

<script setup>
import {fieldList} from '@/pages/release/list';
import ViewTable from "@/components/ViewTable";
import {listReleaseTask,deleteReleaseTask} from "@/api/releaseApi";
import {nextTick, ref} from "vue";
import ExecPrint from "@/pages/release/ExecPrint";
import router from '@/router'
import ConfirmSubmitBtn from "@/components/ConfirmSubmitBtn";
import {ElMessage} from "element-plus";

const printDrawer=ref(false)
const ExecPrintRef=ref()
const takTableRef=ref()

function loadReleaseTaskTable(dataList){
    listReleaseTask().then(res=>{
        if(res.code===200){
            dataList.value=res.data
        }
    })
}

function exec(row){
    //打开抽屉
    printDrawer.value=true
    //等挂载完成才能调用子组件方法
    nextTick(()=>{
        //执行任务
        ExecPrintRef.value.exec(row.id)
    })
}

function stop(){
    ExecPrintRef.value.stop()
    printDrawer.value=false
}

function deleteTask(row){
    return deleteReleaseTask(row.id).then(res=>{
        if(res.code===200){
            ElMessage.success('删除成功')
            takTableRef.value.refresh()
        }
    })
}
</script>

<style scoped>

</style>
