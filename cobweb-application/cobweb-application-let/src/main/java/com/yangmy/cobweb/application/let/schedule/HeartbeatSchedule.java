package com.yangmy.cobweb.application.let.schedule;

import com.yangmy.cobweb.application.let.api.admin.ClusterApi;
import com.yangmy.cobweb.application.let.yml.CobwebLetYml;
import com.yangmy.cobweb.common.core.config.WebMvcConfig;
import com.yangmy.cobweb.common.core.utils.IpUtils;
import com.yangmy.cobweb.common.core.utils.rest.RestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
@Slf4j
@Component
public class HeartbeatSchedule {

    @Resource
    private WebMvcConfig webMvcConfig;
    @Resource
    private ClusterApi clusterApi;
    @Resource
    private CobwebLetYml cobwebLetYml;
    @Resource
    private ThreadPoolExecutor executor;

    /**
     * 每5秒心跳一次
     */
    @Scheduled(initialDelay = 1000,fixedDelay = 5*1000)
    public void heartbeat(){
        executor.execute(()->{
            String ip= IpUtils.getLocalIp4Address();
            String baseURL= webMvcConfig.getBaseURL();
            RestUtils.setBaseURL(cobwebLetYml.getAdminBaseURL());
            try {
                clusterApi.heartbeat(ip,baseURL);
            }catch (Exception e){
               log.error("发送心跳失败",e);
            }
        });
    }

}
