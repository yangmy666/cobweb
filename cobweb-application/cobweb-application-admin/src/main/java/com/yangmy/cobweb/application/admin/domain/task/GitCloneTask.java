package com.yangmy.cobweb.application.admin.domain.task;

import com.yangmy.cobweb.common.docker.GitUtils;
import lombok.Data;

import java.util.function.Function;

/**
 * @author YangMingYang
 * @since 2022/10/13
 */
@Data
public class GitCloneTask {

    //git仓库地址
    private String repositoryUrl;

    private String username;

    private String password;

    //分支
    private String branch;

    //克隆下来放的目录
    private String workTree;

    public void exec(Function<String,Boolean> infoFunc, Function<String,Boolean> errorFunc){
        GitUtils.cloneRepository(repositoryUrl,username,password,branch,workTree,
                infoFunc,errorFunc);
    }
}
