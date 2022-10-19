<template>
    <!--    顶部状态栏-->
    <div style="margin-bottom: 10px">
        <el-tag>集群节点</el-tag>
    </div>
    <view-table :field-list="fieldList" :loadData="listClusterNode">
        <template #server="scope">
            <div style="user-select: none">
                <el-image :src="server" style="z-index: -1"/>
                <div style="display: inline-block;vertical-align: top;margin-left: 20px;
                line-height: 32px;font-size: 15px;font-weight: bolder">
                    {{scope.row.ip}}
                </div>
                <el-image :src="container" style="z-index: -1;margin-left: 40px;"/>
                <div style="display: inline-block;vertical-align: top;margin-left: 5px;
                line-height: 32px;font-size: 15px;font-weight: bolder">
                    {{scope.row.containerNum}}
                </div>
                <el-image :src="image" style="z-index: -1;margin-left: 40px;width: 24px;height: 24px;margin-bottom: 4px"/>
                <div style="display: inline-block;vertical-align: top;margin-left: 7px;
                line-height: 32px;font-size: 15px;font-weight: bolder">
                    {{scope.row.imageNum}}
                </div>
            </div>
        </template>
        <template #operation="scope">
            <el-button size="small" @click="look(scope.row)">查看</el-button>
        </template>
    </view-table>
</template>

<script setup>
import server from '@/assets/server.png'
import {listServer} from '@/api/clusterApi'
import router from "@/router"
import ViewTable from "@/components/ViewTable";
import {fieldList} from './list'
import image from '@/assets/image.png'
import container from '@/assets/container.png'

//查询所有节点
function listClusterNode(dataList){
    listServer().then(res=>{
        if(res.code===200){
            dataList.value=res.data
        }
    })
}

function look(row){
    router.push({
        path:"/node",
        query:{
            ip:row.ip
        }
    })
}
</script>

<style scoped>

</style>
