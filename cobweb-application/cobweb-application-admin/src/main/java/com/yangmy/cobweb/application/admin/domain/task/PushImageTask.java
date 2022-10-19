package com.yangmy.cobweb.application.admin.domain.task;

import com.yangmy.cobweb.common.docker.DockerUtils;
import lombok.Data;

import java.util.function.Function;

/**
 * @author YangMingYang
 * @since 2022/10/12
 */
@Data
public class PushImageTask {

    //推送仓库地址
    private String registryUrl;
    private String imageLocalName;
    private String localTag;
    private String imageName;
    private String tag;

    public void exec(Function<String,Boolean> infoFunc, Function<String,Boolean> errorFunc){
        DockerUtils.pushImage(registryUrl,imageLocalName,localTag,imageName,tag,
                infoFunc,errorFunc);
    }
}
