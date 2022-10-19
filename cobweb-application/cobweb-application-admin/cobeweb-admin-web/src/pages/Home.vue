<template>
<div>
    <!--左侧菜单-->
    <div style="display: inline-block;vertical-align: top;width: 200px">
        <el-menu
                default-active="1"
                class="el-menu-vertical-demo">
            <el-menu-item index="1" @click="clusterNode">
                <el-icon>
                    <el-image :src="cluster"/>
                </el-icon>
                <span>集群节点</span>
            </el-menu-item>
            <el-menu-item index="2" @click="index.push('/container')">
                <el-icon>
                    <el-image :src="container"/>
                </el-icon>
                <span>容器概览</span>
            </el-menu-item>
            <el-menu-item index="3" @click="index.push('/release')">
                <el-icon>
                    <el-image :src="task"/>
                </el-icon>
                <span>发布任务</span>
            </el-menu-item>

<!--            <el-menu-item index="4" @click="index.push('/test')">-->
<!--                <span>Test</span>-->
<!--            </el-menu-item>-->
        </el-menu>
    </div>

    <!--右侧面板-->
    <div style="display: inline-block;vertical-align: top;
    max-width: calc(100vw - 240px);padding: 10px">
        <router-view/>
    </div>
</div>
</template>

<script setup>
import index from "@/router";
import cluster from '@/assets/cluster.png'
import container from '@/assets/container.png'
import task from '@/assets/task.png'
import {onBeforeMount} from "vue";

onBeforeMount(()=>{
    clusterNode()
})

function clusterNode(){
    index.push("/cluster")
}

</script>

<style scoped>

</style>
