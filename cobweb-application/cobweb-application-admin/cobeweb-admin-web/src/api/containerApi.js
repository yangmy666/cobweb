import {get,post,put,patch,del} from "@/utils/request";

export function listClusterContainer(){
    return get("/container/listClusterContainer")
}

export function listContainer(ip){
    return get("/container/listContainer",{
        ip:ip
    })
}

export function runContainer(ip,containerName,imageName,tag,options){
    return post("/container/runContainer",{
        ip:ip,
        containerName:containerName,
        imageName:imageName,
        tag:tag,
        options:options
    })
}

export function startContainer(ip,containerId){
    return put("/container/startContainer",{
        ip:ip,
        containerId:containerId
    })
}

export function stopContainer(ip,containerId){
    return put("/container/stopContainer",{
        ip:ip,
        containerId:containerId
    })
}

export function deleteContainer(ip,containerId){
    return del("/container/deleteContainer",{
        ip:ip,
        containerId:containerId
    })
}

