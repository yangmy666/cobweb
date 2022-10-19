package com.yangmy.cobweb.application.admin.websocket;

import com.yangmy.cobweb.application.admin.domain.task.ReleaseTask;
import com.yangmy.cobweb.common.core.utils.SpringUtils;
import com.yangmy.cobweb.common.core.utils.database.JSONDatabase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author YangMingYang
 * @since 2022/7/1
 */
@Slf4j
@ServerEndpoint("/execRelease/{taskId}")
@Component
public class ExecReleaseEndpoint {

    /**
     * session集合
     */
    public static final Map<String,Session> sessionMap=new ConcurrentHashMap<>();

    /**
     * 根据taskId推送输出
     */
    public static void sendResult(String sessionId,String line){
        Session session=sessionMap.get(sessionId);
        if(session!=null){
            try {
                session.getBasicRemote().sendText(line);
            } catch (IOException e) {
                log.error(String.valueOf(e));
            }
        }
    }

    @OnOpen
    public void onOpen(Session session,@PathParam("taskId")String taskId) {
        log.info("cmdPrint连接开启,sessionId:{},taskId:{}",session.getId(),taskId);
        String sessionId=session.getId();
        //存入session
        sessionMap.put(sessionId,session);
        //根据id查询任务
        JSONDatabase jsonDatabase= SpringUtils.getBean(JSONDatabase.class);
        ReleaseTask releaseTask=jsonDatabase.getById(taskId,ReleaseTask.class);
        ThreadPoolExecutor executor=SpringUtils.getBean(ThreadPoolExecutor.class);
        //执行发布任务
        executor.execute(()-> releaseTask.exec(sessionId));
    }

    @OnClose
    public void onClose(Session session) {
        close(session);
    }

    @OnError
    public void onError(Session session,Throwable ex) {
        log.error("websocket连接错误:{}",ex.toString());
        close(session);
    }

    private void close(Session session){
        log.info("cmdPrint连接关闭,sessionId:{}",session.getId());
        sessionMap.remove(session.getId());
    }

}
