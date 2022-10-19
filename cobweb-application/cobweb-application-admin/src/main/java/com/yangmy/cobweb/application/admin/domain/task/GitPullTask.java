package com.yangmy.cobweb.application.admin.domain.task;

import com.yangmy.cobweb.common.docker.GitUtils;
import lombok.Data;

import java.util.function.Function;

/**
 * @author YangMingYang
 * @since 2022/10/6
 */
@Data
public class GitPullTask {

    //工作目录
    private String workTree;

    public void exec(Function<String,Boolean> infoFunc, Function<String,Boolean> errorFunc){
        GitUtils.pull(workTree,infoFunc,errorFunc);
    }
}
