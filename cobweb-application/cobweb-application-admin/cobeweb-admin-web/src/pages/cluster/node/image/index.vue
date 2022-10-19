<template>
    <view-table :max-height="maxHeight" ref="imageTable" :field-list="fieldList" :load-data="loadImageTable">
        <template #header>
            <el-button size="small" type="primary" @click="openPullImage">拉取镜像</el-button>
            <el-button size="small" type="info" @click="openRegistryDrawer">镜像仓库</el-button>
        </template>
        <template #operation="scope">
            <el-button type="success" size="small"
                       @click="openRunContainer(scope.row)">创建容器</el-button>
            <el-button size="small"
                       @click="openPushImage(scope.row)">推送</el-button>
            <confirm-submit-btn type="danger" text="删除" :func="deleteNodeImage" :params="scope.row"/>
        </template>
    </view-table>

    <!--    拉取镜像弹出框-->
    <el-dialog v-model="pullImageDialogVisible" title="拉取镜像"
               :close-on-click-modal="false" width="30%" center>
        <el-form :inline="true" :model="pullImageForm" class="demo-form-inline">
            <el-form-item label="镜像名称" >
                <el-input v-model="pullImageForm.imageName" placeholder="" />
            </el-form-item>
            <el-form-item label="镜像标签" >
                <el-input v-model="pullImageForm.tag" placeholder="不填将拉取latest" />
            </el-form-item>
            <el-form-item label="仓库地址">
                <el-select v-model="pullImageForm.registryAddress" class="m-2" placeholder="DockerHub" style="margin-right: 30px">
                    <el-option
                            v-for="item in registryList"
                            :key="item.address"
                            :label="item.address"
                            :value="item.address"
                    />
                </el-select>
                <el-button style="margin-top: 10px" type="primary" size="small" @click="pullImageForm.registryAddress=null">DockerHub</el-button>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button size="small" @click="pullImageDialogVisible = false">取消</el-button>
                <submit-btn type="primary" text="拉取" :func="pullNodeImage"/>
            </span>
        </template>
    </el-dialog>
    
    <!--镜像仓库抽屉列表-->
    <el-drawer v-model="registryDrawer" :with-header="false">
        <registry/>
    </el-drawer>
    
    <!--    创建容器弹出框-->
    <el-dialog v-model="runContainerDialogVisible" title="创建容器并运行" :close-on-click-modal="false"
               width="30%" center>
        <el-form :inline="true" :model="runContainerForm" class="demo-form-inline">
            <el-form-item label="容器名称">
                <el-input v-model="runContainerForm.containerName" placeholder="" />
            </el-form-item>
            <el-form-item label="镜像">
                <el-select v-model="runContainerForm.image" class="m-2" placeholder="选择">
                    <el-option
                            v-for="item in imageList"
                            :key="item.repository+':'+item.tag"
                            :label="item.repository+':'+item.tag"
                            :value="JSON.stringify({
                                repository:item.repository,
                                tag:item.tag
                            })"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="选项（例如 -p 8080:8080）">
            </el-form-item>
            <el-form-item>
                <el-input style="width: 400px" v-model="runContainerForm.options"
                          :rows="5"
                          type="textarea"
                          placeholder="" />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button size="small" @click="runContainerDialogVisible = false">取消</el-button>
                <SubmitBtn type="success" text="创建" :func="runNodeContainer"/>
            </span>
        </template>
    </el-dialog>

    <!--    推送镜像弹出框-->
    <el-dialog v-model="pushImageDialogVisible" title="推送镜像到仓库"
               :close-on-click-modal="false" width="30%" center>
        <el-form :inline="true" :model="pushImageForm" class="demo-form-inline">
            <el-form-item label="本地镜像">
                <el-select v-model="pushImageForm.imageId" class="m-2" placeholder="选择">
                    <el-option
                            v-for="item in imageList"
                            :key="item.imageId"
                            :label="item.repository+':'+item.tag"
                            :value="item.imageId"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="仓库地址">
                <el-select v-model="pushImageForm.registryAddress" class="m-2" placeholder="选择">
                    <el-option
                            v-for="item in registryList"
                            :key="item.address"
                            :label="item.address"
                            :value="item.address"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="新名称" >
                <el-input v-model="pushImageForm.imageName" placeholder="" />
            </el-form-item>
            <el-form-item label="新标签" >
                <el-input v-model="pushImageForm.tag" placeholder="" />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button size="small" @click="pushImageDialogVisible = false">取消</el-button>
                <submit-btn type="primary" text="推送" :func="pushNodeImage"/>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
import {ref} from 'vue'
import {ElMessage} from "element-plus";
import SubmitBtn from "@/components/SubmitBtn";
import ConfirmSubmitBtn from "@/components/ConfirmSubmitBtn";
import {isEmpty} from "@/utils";
import {deleteImage, listImage, listRegistry, pullImage, pushImage} from "@/api/imageApi";
import {runContainer} from "@/api/containerApi";
import ViewTable from "@/components/ViewTable";
import {fieldList} from '@/pages/cluster/node/image/list';
import Registry from '@/pages/cluster/node/registry'

const imageTable=ref()
const maxHeight=ref(window.innerHeight-150)
const imageList=ref([])
const registryList=ref([])
const pullImageDialogVisible=ref(false)
const pullImageForm=ref({})
const registryDrawer=ref(false)
const runContainerForm=ref({})
const runContainerDialogVisible=ref(false)
const pushImageDialogVisible=ref(false)
const pushImageForm=ref({})

const props = defineProps({
    ip:{
        type:String
    }
})

defineExpose({
    refresh
})

//加载镜像表格
function loadImageTable(dataList){
    listImage(props.ip).then(res=>{
        if(res.code===200){
            dataList.value=res.data
            imageList.value=dataList.value
        }
    })
}

//刷新镜像表格
function refresh(){
    imageTable.value.refresh()
}

//查询仓库列表
function listRegistryAddress(){
    return listRegistry().then(res=>{
        if(res.code===200){
            registryList.value=res.data
        }
    })
}

//打开拉取镜像
function openPullImage(){
    listRegistryAddress().finally(()=>{
        //选择默认仓库
        if(registryList.value!=null&&registryList.value.length>0){
            pullImageForm.value.registryAddress=registryList.value[0].address
        }
        pullImageDialogVisible.value=true
    })
}

//拉取镜像
function pullNodeImage(){
    if(isEmpty(pullImageForm.value.imageName)){
        ElMessage.warning("镜像名称不能为空")
        return
    }
    const form=pullImageForm.value
    return pullImage(props.ip,form.imageName,form.tag,form.registryAddress).then(res=>{
        if(res.code===200){
            ElMessage.success("拉取完成:"+res.msg)
            pullImageForm.value={}
            pullImageDialogVisible.value=false
            refresh()
        }
    })
}

//打开仓库抽屉
function openRegistryDrawer(){
    registryDrawer.value=true
}

//打开创建容器页面并选择镜像
function openRunContainer(row){
    runContainerForm.value.image=JSON.stringify({
        repository:row.repository,
        tag:row.tag
    })
    //默认容器名称
    runContainerForm.value.containerName=row.repository + '-' + row.tag
    runContainerDialogVisible.value=true
}

//创建容器并运行
function runNodeContainer(){
    const form=runContainerForm.value
    if(isEmpty(form.image)){
        ElMessage.warning("请选择镜像")
        return
    }
    const image=JSON.parse(form.image)
    return runContainer(props.ip,
            form.containerName,
            image.repository,
            image.tag,
            form.options).then(res=>{
        if(res.code===200){
            ElMessage.success("创建成功")
            runContainerForm.value={}
            runContainerDialogVisible.value=false
        }
    })
}

//打开推送镜像弹窗
function openPushImage(row){
    listRegistryAddress().finally(()=>{
        pushImageForm.value.imageId=row.imageId
        pushImageForm.value.registryAddress=registryList.value[0].address
        pushImageForm.value.imageName=row.repository
        pushImageForm.value.tag=row.tag
        pushImageDialogVisible.value=true
    })
}

//推送镜像
function pushNodeImage(){
    const form=pushImageForm.value
    if(isEmpty(form.registryAddress)){
        ElMessage.warning("仓库地址不能为空")
        return
    }
    if(isEmpty(form.imageId)){
        ElMessage.warning("镜像不能为空")
        return
    }
    if(isEmpty(form.imageName)){
        ElMessage.warning("推送到远程后的镜像名不能为空")
        return
    }
    if(isEmpty(form.tag)){
        ElMessage.warning("推送到远程后的标签不能为空")
        return
    }
    return pushImage(props.ip,form.registryAddress,
            form.imageId,
            form.imageName,
            form.tag).then(res=>{
        if(res.code===200){
            ElMessage.success("推送完成:"+res.msg)
            pushImageDialogVisible.value=false
            refresh()
        }
    })
}

//删除镜像
function deleteNodeImage(row){
    return deleteImage(props.ip,row.imageId,row.repository+':'+row.tag).then(res=>{
        if(res.code===200){
            ElMessage.success("删除成功")
            refresh()
        }
    })
}

</script>

<style scoped>

</style>
