package com.yangmy.cobweb.application.admin.config;

import com.yangmy.cobweb.application.admin.yml.CobwebAdminYml;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author YangMingYang
 * @since 2022/10/7
 */
@Component
public class DumpDirConfig {

    @Resource
    private CobwebAdminYml cobwebAdminYml;

    //项目目录
    public String projectDir(){
        return cobwebAdminYml.getCobwebDump()+"/project";
    }

    //数据目录
    public String dataDir(){
        return cobwebAdminYml.getCobwebDump()+"/data";
    }
}
