import {get,post,put,patch,del} from "@/utils/request";

//查询集群所有节点
export function listServer(){
    return get("/cluster/listServer")
}