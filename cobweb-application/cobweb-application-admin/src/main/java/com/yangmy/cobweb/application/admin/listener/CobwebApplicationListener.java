package com.yangmy.cobweb.application.admin.listener;

import com.yangmy.cobweb.application.admin.config.DumpDirConfig;
import com.yangmy.cobweb.common.core.utils.database.FileUtils;
import com.yangmy.cobweb.application.admin.yml.CobwebAdminYml;
import com.yangmy.cobweb.common.core.config.WebMvcConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author YangMingYang
 * @since 2022/10/1
 */
@Slf4j
@Component
public class CobwebApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private WebMvcConfig webMvcConfig;
    @Resource
    private CobwebAdminYml cobwebAdminYml;
    @Resource
    private DumpDirConfig dumpDirConfig;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        FileUtils.createDir(cobwebAdminYml.getCobwebDump());
        FileUtils.createDir(dumpDirConfig.projectDir());
        FileUtils.createDir(dumpDirConfig.dataDir());
        System.out.println("*************************************************************************************\n");
        System.out.println(" ./jdk8/bin/java -jar cobweb-application-let-0.0.1-SNAPSHOT.jar --admin="+webMvcConfig.getBaseURL());
        System.out.println("\n*************************************************************************************");
    }
}

