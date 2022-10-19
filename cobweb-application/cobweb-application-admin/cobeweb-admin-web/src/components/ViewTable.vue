<template>
    <div>
        <div style="margin-bottom: 5px">
            <el-button @click="refresh" size="small">刷新</el-button>
            <slot name="header"/>
        </div>
        <el-table v-loading="tableLoading" :max-height="maxHeight" :data="dataList" :empty-text="emptyText">
            <el-table-column v-for="item in fieldList" :label="item.label"
                             :width="item.width" :prop="item.prop" :fixed="item.fixed">
                <template v-if="item.slot" #default="scope">
                    <slot :name="item.prop" :row="scope.row"/>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script setup>
import {ref, onBeforeMount, watch} from 'vue'

let dataList=ref([])
let tableLoading=ref(false)

const props = defineProps({
    fieldList:{
        type:Array,
        default:[]
    },
    loadData:{
        type:Function,
        default:()=>{}
    },
    maxHeight:{
      type:Number
    },
    emptyText:{
        type:String,
        default:"无"
    }
})

defineExpose({
    refresh
})

onBeforeMount(() => {
    refresh()
})

//加载表格数据
function refresh() {
    tableLoading.value=true
    props.loadData(dataList)
}

watch(dataList,()=>{
    tableLoading.value=false
})

</script>

<style scoped>

</style>
