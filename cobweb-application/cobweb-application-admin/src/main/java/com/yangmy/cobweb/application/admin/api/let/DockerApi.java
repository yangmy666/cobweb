package com.yangmy.cobweb.application.admin.api.let;

import com.yangmy.cobweb.application.admin.domain.dto.DockerInfo;
import com.yangmy.cobweb.common.core.domain.R;
import com.yangmy.cobweb.common.core.utils.rest.RestApi;
import com.yangmy.cobweb.common.docker.domain.DockerContainer;
import com.yangmy.cobweb.common.docker.domain.DockerImage;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
@RestApi
@Component
@RequestMapping("/docker")
public class DockerApi {

    @ApiOperation("查询docker信息")
    @GetMapping("/getDockerInfo")
    public R<DockerInfo> getDockerInfo(){
        throw new RuntimeException("接口调用失败");
    }

    @ApiOperation("查询所有镜像")
    @GetMapping("/listImage")
    public R<List<DockerImage>> listImage(){
        throw new RuntimeException("接口调用失败");
    }

    @ApiOperation("查询所有容器")
    @GetMapping("/listContainer")
    public R<List<DockerContainer>> listContainer(){
        throw new RuntimeException("接口调用失败");
    }

    @ApiOperation("拉取镜像")
    @PostMapping("/pullImage")
    public R<Void> pullImage(@RequestParam(value = "registryUrl",required=false) String registryUrl,
                               @RequestParam("imageName") String imageName,
                               @RequestParam(value="tag",required=false)String tag){
        throw new RuntimeException("接口调用失败");
    }

    @ApiOperation("删除镜像")
    @DeleteMapping("/deleteImage")
    public R<Void> deleteImage(@RequestParam("image") String image){
        throw new RuntimeException("接口调用失败");
    }

    @ApiOperation("推送镜像")
    @PostMapping("/pushImage")
    public R<Void> pushImage(@RequestParam("registryUrl") String registryUrl,
                               @RequestParam("imageId") String imageId,
                               @RequestParam("imageName") String imageName,
                               @RequestParam("tag") String tag){
        throw new RuntimeException("接口调用失败");
    }

    @ApiOperation("创建容器并运行")
    @PostMapping("/runContainer")
    public R<Void> runContainer(@RequestParam("containerName") String containerName,
                                  @RequestParam("imageName") String imageName,
                                  @RequestParam("tag") String tag,
                                  @RequestParam("options") String options){
        throw new RuntimeException("接口调用失败");
    }

    @ApiOperation("启动容器")
    @PutMapping("/startContainer")
    public R<Void> startContainer(@RequestParam("containerId") String containerId){
        throw new RuntimeException("接口调用失败");
    }

    @ApiOperation("停止容器")
    @PutMapping("/stopContainer")
    public R<Void> stopContainer(@RequestParam("containerId") String containerId){
        throw new RuntimeException("接口调用失败");
    }

    @ApiOperation("删除容器")
    @DeleteMapping("/deleteContainer")
    public R<Void> deleteContainer(@RequestParam("containerId") String containerId){
        throw new RuntimeException("接口调用失败");
    }

    @ApiOperation("开启容器日志推送")
    @PostMapping("/openLogs")
    public R<Void> openLogs(@RequestParam("sessionId") String sessionId,
                            @RequestParam("containerId") String containerId){
        throw new RuntimeException("接口调用失败");
    }

    @ApiOperation("停止容器日志推送")
    @PostMapping("/stopLogs")
    public R<Void> stopLogs(@RequestParam("sessionId") String sessionId){
        throw new RuntimeException("接口调用失败");
    }

}
