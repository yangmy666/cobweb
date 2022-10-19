package com.yangmy.cobweb.common.docker;

import org.springframework.util.StringUtils;

import java.util.function.Function;

/**
 * @author YangMingYang
 * @since 2022/10/13
 */
public class GitUtils {

    /**
     * 克隆仓库
     * @param repositoryUrl 仓库地址
     * @param username 账户名
     * @param password 密码
     * @param branch 分支
     * @param workTree 克隆下来放在哪个目录下
     * @param infoFunc 正常输出
     * @param errorFunc 错误输出
     */
    public static void cloneRepository(String repositoryUrl,
                                       String username,
                                       String password,
                                       String branch,
                                       String workTree,
                                       Function<String,Boolean> infoFunc,
                                       Function<String,Boolean> errorFunc){
        String cmd;
        if(StringUtils.hasText(username)&&StringUtils.hasText(password)){
            boolean flag=repositoryUrl.contains("http://");
            if(flag){
                String url=repositoryUrl.split("http://")[1];
                cmd="git clone -b "+branch+" http://"+username+":"
                        +password+"@"+url+" "+workTree;
            }else{
                String url=repositoryUrl.split("https://")[1];
                cmd="git clone -b "+branch+" https://"+username+":"
                        +password+"@"+url+" "+workTree;
            }
        }else{
            cmd="git clone -b "+branch+" "+repositoryUrl+" "+workTree;
        }
        CmdUtils.exec(cmd,infoFunc,errorFunc);
    }

    /**
     * 更新代码
     * @param workTree 工作目录
     * @return
     */
    public static void pull(String workTree,
                              Function<String,Boolean> infoFunc,
                              Function<String,Boolean> errorFunc){
        String cmd="git --git-dir="+workTree+"/.git --work-tree="+workTree+" pull";
        CmdUtils.exec(cmd,infoFunc,errorFunc);
    }
}
