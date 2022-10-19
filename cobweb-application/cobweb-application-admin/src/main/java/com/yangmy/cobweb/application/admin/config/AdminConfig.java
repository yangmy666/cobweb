package com.yangmy.cobweb.application.admin.config;

import com.yangmy.cobweb.application.admin.utils.ClusterContext;
import com.yangmy.cobweb.common.core.utils.database.JSONDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author liumf
 * @date 18:04 2022/7/3
 * @className WebSocketConfig
 * @description
 */
@Configuration
public class AdminConfig {

    @Resource
    private DumpDirConfig dumpDirConfig;

    @Bean
    public ClusterContext clusterContext(){
        return new ClusterContext();
    }

    /**
     * 开启webSocket支持
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public JSONDatabase jsonDatabase(){
        return JSONDatabase.build(dumpDirConfig.dataDir());
    }

    @Bean
    public ThreadPoolExecutor executor(){
        return new ThreadPoolExecutor(20,100,
                30, TimeUnit.SECONDS,new LinkedBlockingQueue<>());
    }

}
