package com.yangmy.cobweb.application.admin.domain.task;

import com.yangmy.cobweb.common.docker.DockerUtils;
import lombok.Data;

import java.util.function.Function;

/**
 * @author YangMingYang
 * @since 2022/10/12
 */
@Data
public class BuildImageTask {

    private String imageName;

    private String tag;

    //dockerfile所在目录
    private String dockerfile;

    public void exec(Function<String,Boolean> infoFunc, Function<String,Boolean> errorFunc){
        DockerUtils.buildImage(imageName,tag,dockerfile,infoFunc,errorFunc);
    }
}
