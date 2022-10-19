package com.yangmy.cobweb.application.admin.schedule;

import com.alibaba.fastjson.JSON;
import com.yangmy.cobweb.application.admin.utils.ClusterContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author YangMingYang
 * @since 2022/10/6
 */
@Slf4j
@Component
public class ClearClusterSchedule {

    @Resource
    private ClusterContext clusterContext;

    /**
     * 每30秒删除所有节点
     */
    @Scheduled(initialDelay = 1000,fixedDelay = 30*1000)
    public void nodeExpiry(){
        clusterContext.clear();
        log.info("清空集群节点{}", JSON.toJSONString(clusterContext.list()));
    }
}
