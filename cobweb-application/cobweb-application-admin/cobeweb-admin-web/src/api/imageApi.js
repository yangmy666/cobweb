import {get,post,put,patch,del} from "@/utils/request";

export function listRegistry(){
    return get("/image/listRegistry")
}

export function addRegistry(address){
    const form={
        address:address
    }
    return post("/image/addRegistry",form)
}

export function removeRegistry(id){
    const params={
        id:id
    }
    return del("/image/removeRegistry",params)
}

export function listImage(ip){
    return get("/image/listImage",{
        ip:ip
    })
}

export function pullImage(ip,imageName,tag,registryUrl){
    const form={
        ip:ip,
        imageName:imageName,
        tag:tag,
        registryUrl:registryUrl
    }
    return post("/image/pullImage",form)
}

export function deleteImage(ip,imageId,imageName){
    return del("/image/deleteImage",{
        ip:ip,
        imageId:imageId,
        imageName:imageName
    })
}

export function pushImage(ip,registryUrl,imageId,imageName,tag){
    const form={
        ip:ip,
        registryUrl:registryUrl,
        imageId:imageId,
        imageName:imageName,
        tag:tag
    }
    return post("/image/pushImage",form)
}