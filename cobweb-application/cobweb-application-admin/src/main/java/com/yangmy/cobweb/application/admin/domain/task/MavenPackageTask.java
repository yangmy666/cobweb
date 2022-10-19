package com.yangmy.cobweb.application.admin.domain.task;

import com.yangmy.cobweb.common.docker.MavenUtils;
import lombok.Data;

import java.util.function.Function;

/**
 * @author YangMingYang
 * @since 2022/10/6
 */
@Data
public class MavenPackageTask {

    private String pom;

    private String env;

    public void exec(Function<String,Boolean> infoFunc, Function<String,Boolean> errorFunc){
        MavenUtils.pkg(pom,env,infoFunc,errorFunc);
    }
}
