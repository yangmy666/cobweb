<template>
    <el-popconfirm :title="'确定'+text+'吗?'"
                   confirm-button-text="确定"
                   cancel-button-text="取消"
                   @confirm="click">
        <template #reference>
            <el-button :type="type" size="small"
                       :loading="loading">{{ text }}</el-button>
        </template>
    </el-popconfirm>
</template>

<script setup>
import {ref, onBeforeMount} from 'vue'
const loading=ref(false)

const props = defineProps({
    text:{
        type:String,
        default:'按钮'
    },
    type:{
        type:String,
        default:''
    },
    func:{
        type:Function,
        default:()=>{}
    },
    params:{
        type:Object,
        default:{}
    }
})

function click(){
    loading.value=true
    props.func(props.params).finally(()=>{
        loading.value=false
    })
}
</script>

<style scoped>

</style>
