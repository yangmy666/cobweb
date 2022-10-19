<template>
    <!--    顶部状态栏-->
    <div style="margin-bottom: 10px">
        <el-button @click="router.back()"
                   style="margin-right: 10px">
            <el-image :src="backIcon"/>返回
        </el-button>
        <el-tag type="success">{{ip}}</el-tag>
    </div>
    <!--    正文表格-->
    <div>
        <el-tabs
                v-model="activateName"
                type="card"
                class="demo-tabs"
                @tab-click="tabClick">
            <!--容器管理-->
            <el-tab-pane label="容器" name="container">
                <Container ref="container" :ip="ip"/>
            </el-tab-pane>
            <!--镜像管理-->
            <el-tab-pane label="镜像" name="image">
                <Image ref="image" :ip="ip"/>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script setup>
import {nextTick, onBeforeMount, ref} from 'vue'
import {useRoute} from "vue-router"
import backIcon from '@/assets/back.svg'
import router from "@/router"
import Container from "@/pages/cluster/node/container";
import Image from "@/pages/cluster/node/image";

const activateName=ref('container')
const ip=ref()
const container=ref()
const image=ref()

onBeforeMount(() => {
    ip.value=useRoute().query.ip
    //等vue挂载好再调用
    nextTick(()=>{
        container.value.refresh()
    })
})

function tabClick(tab, event) {
    if(tab.props.name==='container'){
        //等vue挂载好再调用
        nextTick(()=>{
            container.value.refresh()
        })
    }
    if(tab.props.name==='image'){
        nextTick(()=>{
            image.value.refresh()
        })

    }
}


</script>

<style scoped>
.demo-tabs > .el-tabs__content {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
}
.el-tabs--right .el-tabs__content,
.el-tabs--left .el-tabs__content {
    height: 100%;
}
</style>
