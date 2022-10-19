import {get, post, put, patch, del, postJson} from "@/utils/request";

export function listReleaseTask(){
    return get("/release/listReleaseTask")
}

export function saveReleaseTask(releaseTask){
    return postJson("/release/saveReleaseTask",releaseTask)
}

export function deleteReleaseTask(id){
    return del("/release/deleteReleaseTask",{id:id})
}