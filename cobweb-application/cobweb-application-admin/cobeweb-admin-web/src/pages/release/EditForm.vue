<template>
<div>
    <!--    顶部状态栏-->
    <div style="margin-bottom: 10px">
        <el-button @click="router.back()"
                   style="margin-right: 10px">
            <el-image :src="backIcon"/>返回
        </el-button>
        <el-tag>构建发布任务</el-tag>
    </div>
    <div style="text-align: center">
        <!--git配置-->
        <el-form :model="releaseTask" label-width="150px">
            <el-form-item label="备注:">
                <el-input v-model="releaseTask.remark" class="input"/>
            </el-form-item>
            <el-form-item label="git仓库地址:">
                <el-input v-model="releaseTask.gitRepositoryUrl" class="input"
                          placeholder="https://或者http://"/>
            </el-form-item>
            <el-form-item label="账户名:">
                <el-input v-model="releaseTask.username" class="input"
                          placeholder="没有可不填"/>
            </el-form-item>
            <el-form-item label="密码:">
                <el-input v-model="releaseTask.password" class="input"
                          placeholder="没有可不填"/>
            </el-form-item>
            <el-form-item label="分支:">
                <el-input v-model="releaseTask.branch" class="input"
                          placeholder="例如：master"/>
            </el-form-item>
            <el-form-item label="工作目录:">
                <el-input v-model="releaseTask.workTree" class="input"
                          placeholder="格式：/项目名"/>
            </el-form-item>
            <el-form-item label="maven打包环境:">
                <el-input v-model="releaseTask.env" class="input"
                          placeholder="例如：dev test prod等"/>
            </el-form-item>
            <el-form-item label="推送镜像仓库地址:">
                <el-input v-model="releaseTask.registryAddress" class="input"
                          placeholder="例如：192.168.96.145:5000"/>
            </el-form-item>
            <el-form-item label="构建docker镜像:">
                <el-table :data="releaseTask.imageArgsList" style="width: 100%" empty-text=" ">
                    <el-table-column prop="dockerfile" label="Dockerfile目录" width="180"/>
                    <el-table-column prop="imageName" label="镜像命名" width="180" />
                    <el-table-column label="创建容器并运行" width="500">
                        <template #default="scope">
                            <el-table :data="scope.row.containerArgsList" style="width: 100%" empty-text=" ">
                                <el-table-column label="服务器" width="180">
                                    <template #default="scope1">
                                        <el-select v-model="scope1.row.nodeIp" placeholder="请选择服务器" class="m-2">
                                            <el-option
                                                    v-for="item in nodeList"
                                                    :key="item.ip"
                                                    :label="item.ip"
                                                    :value="item.ip"
                                            />
                                        </el-select>
                                    </template>
                                </el-table-column>
                                <el-table-column label="容器参数" width="240">
                                    <template #default="scope1">
                                        <el-input style="width: 180px" v-model="scope1.row.options"
                                                  :rows="3"
                                                  type="textarea"
                                                  placeholder="" />
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作" fixed="right" width="80">
                                    <template #default="scope1">
                                        <el-button size="small" type="danger" @click="removeContainer(scope.row.containerArgsList,scope1.row)">删除</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <el-button size="small" @click="openContainerDialog(scope.row)">添加</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" fixed="right" width="80">
                        <template #default="scope">
                            <el-button size="small" type="danger" @click="removeImage(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-button size="small" @click="openImageDialog">添加</el-button>
            </el-form-item>
        </el-form>
    </div >
    <div style="width: 100%;text-align: center">
        <submit-btn size="default" :func="save"  :params="releaseTask" text="保存"/>
    </div>

<!--    添加镜像弹窗-->
    <el-dialog
            v-model="addImageDialogVisible"
            title="构建镜像"
            width="50%" center>
        <el-form :model="addImageForm" label-width="120px">
            <el-form-item label="Dockerfile目录:">
                <el-input v-model="addImageForm.dockerfile" class="input"
                          placeholder="例如：/service（相对于工作目录而言）"/>
            </el-form-item>
            <el-form-item label="镜像命名:" >
                <el-input v-model="addImageForm.imageName" class="input"
                          placeholder="（只命名就行，制作时系统会自动生成时间戳作为镜像标签）"/>
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button size="small" @click="cancelAddImage">取消</el-button>
            <el-button size="small" type="primary" @click="addImage">确定</el-button>
        </template>
    </el-dialog>

    <!--    添加容器弹窗-->
    <el-dialog
            v-model="addContainerDialogVisible"
            title="创建容器并运行"
            width="50%" center>
        <el-form :model="addContainerForm" label-width="100px">
            <el-form-item label="服务器:">
                <el-select v-model="addContainerForm.nodeIp" placeholder="请选择服务器" class="m-2">
                    <el-option
                            v-for="item in nodeList"
                            :key="item.ip"
                            :label="item.ip"
                            :value="item.ip"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="容器参数:">
                <el-input style="width: 600px" v-model="addContainerForm.options"
                          :rows="5"
                          type="textarea"
                          placeholder="" />
            </el-form-item>
        </el-form>
        <template #footer>
            <el-button size="small" @click="cancelAddContainer">取消</el-button>
            <el-button size="small" type="primary" @click="addContainer">确定</el-button>
        </template>
    </el-dialog>
</div>
</template>

<script setup>
import {onBeforeMount, ref} from 'vue'
import SubmitBtn from '@/components/SubmitBtn'
import {saveReleaseTask} from '@/api/releaseApi'
import {ElMessage} from "element-plus";
import router from '@/router'
import backIcon from '@/assets/back.svg'
import {listServer} from "@/api/clusterApi";
import {useRoute} from "vue-router";
import {isEmpty} from "@/utils";

onBeforeMount(()=>{
    try{
        releaseTask.value=JSON.parse(String(useRoute().query.formData))
    }finally {
        listServer().then(res=>{
            if(res.code===200){
                nodeList.value=res.data
            }
        })
    }
})

//发布任务
const releaseTask=ref({})
//添加镜像
const addImageDialogVisible=ref(false)
const addImageForm=ref({})
//添加容器
const addContainerDialogVisible=ref(false)
const addContainerForm=ref({})
//添加容器可选服务器
const nodeList=ref([])

//保存发布任务
function save(){
    return saveReleaseTask(releaseTask.value).then(res=>{
        if(res.code===200){
            ElMessage.success('已保存')
            router.back()
        }
    })
}

//打开添加镜像弹窗
function openImageDialog(){
    addImageDialogVisible.value=true
}
//添加镜像
function addImage(){
    if(isEmpty(addImageForm.value.dockerfile)){
        ElMessage.warning('请输入dockerfile所在目录')
        return
    }
    if(isEmpty(addImageForm.value.imageName)){
        ElMessage.warning('请给要制作的镜像取个名字')
        return
    }
    let imageArgsList=releaseTask.value.imageArgsList
    if(imageArgsList!=null){
        releaseTask.value.imageArgsList=imageArgsList.concat(addImageForm.value)
    }else{
        releaseTask.value.imageArgsList=[addImageForm.value]
    }
    cancelAddImage()
}
//取消添加镜像
function cancelAddImage(){
    addImageForm.value={}
    addImageDialogVisible.value=false
}
//删除镜像
function removeImage(row){
    let index=releaseTask.value.imageArgsList.indexOf(row)
    releaseTask.value.imageArgsList.splice(index,1)
}

//镜像index
let imageIndex
//打开添加容器弹窗
function openContainerDialog(row){
    //查询可选服务器
    listServer().then(res=>{
        if(res.code===200){
            nodeList.value=res.data
            //记录镜像index
            imageIndex=releaseTask.value.imageArgsList.indexOf(row)
            //然后打开添加容器弹窗
            addContainerDialogVisible.value=true
        }
    })
}
//添加容器
function addContainer(){
    if(isEmpty(addContainerForm.value.nodeIp)){
        ElMessage.warning('请选择部署服务器')
        return
    }
    let containerArgsList=releaseTask.value.imageArgsList[imageIndex].containerArgsList
    if(containerArgsList!=null){
        releaseTask.value.imageArgsList[imageIndex].containerArgsList=containerArgsList.concat(addContainerForm.value)
    }else{
        releaseTask.value.imageArgsList[imageIndex].containerArgsList=[addContainerForm.value]
    }
    cancelAddContainer()
}
//取消添加容器
function cancelAddContainer(){
    addContainerForm.value={}
    addContainerDialogVisible.value=false
}
//删除容器
function removeContainer(containerArgsList,row){
    const index=containerArgsList.indexOf(row)
    containerArgsList.splice(index,1)
}
</script>

<style scoped>
.input{
    width: 400px;
}
</style>
