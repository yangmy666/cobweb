package com.yangmy.cobweb.application.let.controller;

import com.yangmy.cobweb.application.let.api.admin.ContainerApi;
import com.yangmy.cobweb.application.let.domain.dto.DockerInfo;
import com.yangmy.cobweb.application.let.yml.CobwebLetYml;
import com.yangmy.cobweb.common.core.domain.R;
import com.yangmy.cobweb.common.core.utils.rest.RestUtils;
import com.yangmy.cobweb.common.docker.DockerUtils;
import com.yangmy.cobweb.common.docker.domain.DockerContainer;
import com.yangmy.cobweb.common.docker.domain.DockerImage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
@Slf4j
@Validated
@Api(tags = "docker相关接口")
@RestController
@RequestMapping("/docker")
public class DockerController {

    @Resource
    private ContainerApi containerApi;
    @Resource
    private CobwebLetYml cobwebLetYml;

    @ApiOperation("查询docker信息")
    @GetMapping("/getDockerInfo")
    public R<DockerInfo> getDockerInfo(){
        DockerInfo dockerInfo=new DockerInfo();
        dockerInfo.setContainerNum(DockerUtils.getContainerNum());
        dockerInfo.setImageNum(DockerUtils.getImageNum());
        return R.success(dockerInfo);
    }

    @ApiOperation("查询所有镜像")
    @GetMapping("/listImage")
    public R<List<DockerImage>> listImage(){
        //查询所有镜像
        List<DockerImage> list=DockerUtils.listImage();
        return R.success(list);
    }

    @ApiOperation("查询所有容器")
    @GetMapping("/listContainer")
    public R<List<DockerContainer>> listContainer(){
        return R.success(DockerUtils.listContainer());
    }

    @ApiOperation("拉取镜像")
    @PostMapping("/pullImage")
    public R<Void> pullImage(@RequestParam(required=false) String registryUrl,
                             @RequestParam String imageName,
                             @RequestParam(required=false) String tag){
        return R.success(DockerUtils.pullImage(registryUrl,imageName,tag));
    }

    @ApiOperation("删除镜像")
    @DeleteMapping("/deleteImage")
    public R<Void> deleteImage(@RequestParam String image){
        return R.success(DockerUtils.deleteImage(image));
    }

    @ApiOperation("推送镜像")
    @PostMapping("/pushImage")
    public R<Void> pushImage(@RequestParam String registryUrl,
                               @RequestParam String imageId,
                               @RequestParam String imageName,
                               @RequestParam String tag){
        return R.success(DockerUtils.pushImage(registryUrl,imageId,imageName,tag));
    }

    @ApiOperation("创建容器并运行")
    @PostMapping("/runContainer")
    public R<Void> runContainer(@RequestParam String containerName,
                                  @RequestParam String imageName,
                                  @RequestParam String tag,
                                     @RequestParam String options){
        return R.success(DockerUtils.runContainer(containerName,options,imageName,tag));
    }

    @ApiOperation("启动容器")
    @PutMapping("/startContainer")
    public R<Void> startContainer(@RequestParam String containerId){
        return R.success(DockerUtils.startContainer(containerId));
    }

    @ApiOperation("停止容器")
    @PutMapping("/stopContainer")
    public R<Void> stopContainer(@RequestParam String containerId){
        return R.success(DockerUtils.stopContainer(containerId));
    }

    @ApiOperation("删除容器")
    @DeleteMapping("/deleteContainer")
    public R<Void> deleteContainer(@RequestParam String containerId){
        return R.success(DockerUtils.deleteContainer(containerId));
    }

    @ApiOperation("开启容器日志推送")
    @PostMapping("/openLogs")
    public R<Void> openLogs(@RequestParam String sessionId,
                            @RequestParam String containerId){
        new Thread(()->{
            sessionIdSet.add(sessionId);
            RestUtils.setBaseURL(cobwebLetYml.getAdminBaseURL());
            DockerUtils.logs(containerId,(line)->{
                //推送日志
                containerApi.sendLog(sessionId,line);
                return sessionIdSet.contains(sessionId);
            },(line)->{
                //推送日志
                containerApi.sendLog(sessionId,line);
                return sessionIdSet.contains(sessionId);
            });
        }).start();
        return R.success();
    }

    //正在进行推送任务中的sessionId们
    private final Set<String> sessionIdSet=new ConcurrentSkipListSet<>();

    @ApiOperation("停止容器日志推送")
    @PostMapping("/stopLogs")
    public R<Void> stopLogs(@RequestParam String sessionId){
        sessionIdSet.remove(sessionId);
        return R.success();
    }


}
