package com.yangmy.cobweb.application.admin.controller;

import com.yangmy.cobweb.application.admin.api.let.DockerApi;
import com.yangmy.cobweb.application.admin.domain.dto.DockerInfo;
import com.yangmy.cobweb.application.admin.domain.dto.Node;
import com.yangmy.cobweb.application.admin.domain.dto.Server;
import com.yangmy.cobweb.application.admin.utils.ClusterContext;
import com.yangmy.cobweb.common.core.domain.R;
import com.yangmy.cobweb.common.core.utils.rest.RestUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
@Validated
@Api(tags = "集群节点相关接口")
@RestController
@RequestMapping("/cluster")
public class ClusterController {

    @Resource
    private ClusterContext clusterContext;
    @Resource
    private DockerApi dockerApi;
    @Resource
    private ThreadPoolExecutor executor;

    @ApiOperation("心跳一次")
    @PostMapping("/heartbeat")
    public R<Void> heartbeat(@RequestParam String ip,
                             @RequestParam String baseURL){
        Node node=new Node();
        node.setIp(ip);
        node.setBaseURL(baseURL);
        clusterContext.put(ip,node);
        return R.success();
    }

    @ApiOperation("查询集群所有服务器")
    @GetMapping("/listServer")
    public R<List<Server>> listServer(){
        List<Node> nodeList= clusterContext.list();
        if(CollectionUtils.isEmpty(nodeList)){
            return R.success();
        }
        List<Server> serverList=new CopyOnWriteArrayList<>();
        CountDownLatch latch=new CountDownLatch(nodeList.size());
        for (Node node : nodeList) {
            executor.execute(()->{
                Server server=new Server();
                server.setIp(node.getIp());
                RestUtils.setBaseURL(node.getBaseURL());
                R<DockerInfo> r=dockerApi.getDockerInfo();
                if(r.getCode()==200&&r.getData()!=null){
                    server.setContainerNum(r.getData().getContainerNum());
                    server.setImageNum(r.getData().getImageNum());
                }
                serverList.add(server);
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return R.success(serverList);
    }

}
