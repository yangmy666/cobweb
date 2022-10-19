import axios from 'axios';
import {ElMessage, ElNotification} from 'element-plus'
import qs from "qs";

const instance=axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: import.meta.env.VITE_REQUEST_BASE_URL
})

instance.interceptors.request.use(request=>{
    return request
})

//响应拦截器
instance.interceptors.response.use(response=> {
        const res=response.data
        //操作失败
        if(res.code===500){
            ElMessage.error(res.msg)
            return null
        }

        //参数校验错误
        if(res.code===400){
            ElMessage.warning(res.msg)
            return null
        }

        return res
    },
    (error) => {
        let { message } = error;
        if (message === 'Network Error') {
            ElMessage.error('Network Error')
        } else if (message.includes('timeout')) {
            ElMessage.error('请求超时')
        } else if (message.includes('Request failed with status code')) {
            ElMessage.error('接口' + message.substring(message.length - 3) + '异常')
        }
        return Promise.reject(error);
    })

export function get(url,query){
    return instance.get(url,
        {
            params:query
        })
}

export function post(url,form){
    return instance.post(url,
        qs.stringify(form),
        {
            headers:{
                contentType:'application/x-www-form-urlencoded'
            }
        })
}

export function postJson(url,data){
    return instance.post(url,
        data,
        {
            headers:{
                contentType:'application/json'
            }
        })
}

export function put(url,form){
    return instance.put(url,
        qs.stringify(form),
        {
            headers:{
                contentType:'application/x-www-form-urlencoded'
            }
        })
}

export function patch(url,form){
    return instance.patch(url,
        qs.stringify(form),
        {
            headers:{
                contentType:'application/x-www-form-urlencoded'
            }
        })
}

export function del(url,params){
    return instance.delete(url,
        {
            headers:{
                contentType:'application/x-www-form-urlencoded'
            },
            params:params
        })
}