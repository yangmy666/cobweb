package com.yangmy.cobweb.application.admin.websocket;

import com.yangmy.cobweb.application.admin.api.let.DockerApi;
import com.yangmy.cobweb.application.admin.utils.ClusterContext;
import com.yangmy.cobweb.common.core.domain.R;
import com.yangmy.cobweb.common.core.utils.SpringUtils;
import com.yangmy.cobweb.common.core.utils.rest.RestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YangMingYang
 * @since 2022/10/14
 */
@Slf4j
@ServerEndpoint("/dockerLog/{nodeIp}/{containerId}")
@Component
public class DockerLogsEndpoint {

    /**
     * key:session
     * value:节点ip
     */
    public static final Map<Session,String> sessionMap=new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("nodeIp")String nodeIp,@PathParam("containerId")String containerId) {
        log.info("建立websocket连接,sessionId:{}",session.getId());
        sessionMap.put(session,nodeIp);
        DockerApi dockerApi= SpringUtils.getBean(DockerApi.class);
        ClusterContext clusterContext=SpringUtils.getBean(ClusterContext.class);
        //选择节点ip
        RestUtils.setBaseURL(clusterContext.getNodeBaseURL(nodeIp));
        //开启日志推送
        dockerApi.openLogs(session.getId(),containerId);
    }

    @OnClose
    public void onClose(Session session) {
        stopLogs(session);
    }

    @OnError
    public void onError(Session session,Throwable ex) {
        log.error("websocket连接错误:{}",ex.toString());
        stopLogs(session);
    }

    private void stopLogs(Session session){
        try {
            session.close();
        } catch (IOException e) {
            log.error("websocket关闭异常:{}",e.toString());
        }
        String nodeIp=sessionMap.get(session);
        sessionMap.remove(session);
        DockerApi dockerApi= SpringUtils.getBean(DockerApi.class);
        ClusterContext clusterContext=SpringUtils.getBean(ClusterContext.class);
        //选择节点ip
        RestUtils.setBaseURL(clusterContext.getNodeBaseURL(nodeIp));
        //停止日志推送
        R<Void> r=dockerApi.stopLogs(session.getId());
        if(r.getCode()==200){
            log.info("关闭websocket连接,sessionId:{}",session.getId());
        }
    }
}
