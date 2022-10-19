<template>
<div>
    <view-table ref="registryTable" :field-list="fieldList" :load-data="loadRegistryTable">
        <template #header>
            <el-button type="primary" size="small" @click="addRegistryDialogVisible=true">添加镜像仓库地址</el-button>
        </template>
        <template #operation="scope">
            <confirm-submit-btn type="danger" text="删除" :func="removeRegistryAddress" :params="scope.row"/>
        </template>
    </view-table>

    <!--    添加镜像仓库弹出框-->
    <el-dialog v-model="addRegistryDialogVisible" title="添加镜像仓库地址" width="30%" center>
        <el-form :inline="true" :model="addRegistryForm" class="demo-form-inline">
            <el-form-item label="镜像仓库地址">
                <el-input v-model="addRegistryForm.address" placeholder="" />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button size="small" @click="addRegistryDialogVisible = false">取消</el-button>
                <SubmitBtn type="primary" text="添加" :func="addRegistryAddress" />
            </span>
        </template>
    </el-dialog>
</div>
</template>

<script setup>
import {ref} from 'vue'
import {addRegistry, listRegistry, removeRegistry} from "@/api/imageApi";
import {isEmpty} from "@/utils";
import {ElMessage} from "element-plus";
import SubmitBtn from '@/components/SubmitBtn'
import ConfirmSubmitBtn from '@/components/ConfirmSubmitBtn'
import ViewTable from "@/components/ViewTable";
import {fieldList} from '@/pages/cluster/node/registry/list';

const registryTable=ref()
const addRegistryDialogVisible=ref(false)
const addRegistryForm=ref({})

//加载仓库表格
function loadRegistryTable(dataList){
    listRegistry().then(res=>{
        if(res.code===200){
            dataList.value=res.data
        }
    })
}

//刷新表格
function refresh(){
    registryTable.value.refresh()
}

//添加仓库地址
function addRegistryAddress(){
    if(isEmpty(addRegistryForm.value.address)){
        ElMessage.warning("仓库地址不能为空")
        return
    }
    return addRegistry(addRegistryForm.value.address).then(res=>{
        if(res.code===200){
            ElMessage.success("添加成功")
            addRegistryForm.value={}
            addRegistryDialogVisible.value=false
            refresh()
        }
    })
}

//删除仓库地址
function removeRegistryAddress(row){
    return removeRegistry(row.id).then(res=>{
        if(res.code===200){
            ElMessage.success("删除成功")
            refresh()
        }
    })
}

</script>

<style scoped>

</style>
