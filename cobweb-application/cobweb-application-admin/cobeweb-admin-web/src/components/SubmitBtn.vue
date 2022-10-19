<template>
<el-button :type="type" :size="size" @click="click"
           :loading="loading">{{text}}</el-button>
</template>

<script setup>
import {ref} from 'vue'

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
    },
    size:{
        type:String,
        default:'small'
    }
})

const loading=ref(false)

function click(){
    loading.value=true
    props.func(props.params).finally(()=>{
        loading.value=false
    })
}
</script>

<style scoped>

</style>
