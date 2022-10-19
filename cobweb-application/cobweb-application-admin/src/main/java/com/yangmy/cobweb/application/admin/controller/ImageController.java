package com.yangmy.cobweb.application.admin.controller;

import com.yangmy.cobweb.application.admin.api.let.DockerApi;
import com.yangmy.cobweb.application.admin.utils.ClusterContext;
import com.yangmy.cobweb.application.admin.domain.dto.ImageRegistry;
import com.yangmy.cobweb.common.core.utils.database.JSONDatabase;
import com.yangmy.cobweb.common.core.domain.R;
import com.yangmy.cobweb.common.core.utils.rest.RestUtils;
import com.yangmy.cobweb.common.docker.domain.DockerImage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author YangMingYang
 * @since 2022/10/5
 */
@Validated
@Api(tags = "镜像相关接口")
@RestController
@RequestMapping("/image")
public class ImageController {

    @Resource
    private DockerApi dockerApi;
    @Resource
    private JSONDatabase jsonDatabase;
    @Resource
    private ClusterContext clusterContext;

    @ApiOperation("查询所有镜像仓库地址")
    @GetMapping("/listRegistry")
    public R<List<ImageRegistry>> listRegistry(){
        List<ImageRegistry> list= jsonDatabase.list(ImageRegistry.class);
        return R.success(list);
    }

    @ApiOperation("添加镜像仓库地址")
    @PostMapping("/addRegistry")
    public R<Void> addRegistry(ImageRegistry registry){
        jsonDatabase.add(registry,ImageRegistry.class);
        return R.success();
    }

    @ApiOperation("删除镜像仓库地址")
    @DeleteMapping("/removeRegistry")
    public R<Void> removeRegistry(@RequestParam String id){
        jsonDatabase.deleteById(id,ImageRegistry.class);
        return R.success();
    }


    @ApiOperation("查询指定节点所有镜像")
    @GetMapping("/listImage")
    public R<List<DockerImage>> listImage(@RequestParam String ip){
        RestUtils.setBaseURL(clusterContext.getNodeBaseURL(ip));
        return dockerApi.listImage();
    }

    @ApiOperation("操作指定节点拉取镜像")
    @PostMapping("/pullImage")
    public R<Void> pullImage(@RequestParam("ip")String ip,
                               @RequestParam(value = "registryUrl",required=false) String registryUrl,
                               @RequestParam("imageName") String imageName,
                               @RequestParam(value = "tag",required=false)String tag){
        RestUtils.setBaseURL(clusterContext.getNodeBaseURL(ip));
        return dockerApi.pullImage(registryUrl,imageName,tag);
    }

    @ApiOperation("操作指定节点推送镜像")
    @PostMapping("/pushImage")
    public R<Void> pushImage(@RequestParam("ip")String ip,
                               @RequestParam(value = "registryUrl") String registryUrl,
                               @RequestParam("imageId") String imageId,
                               @RequestParam("imageName") String imageName,
                               @RequestParam("tag") String tag){
        RestUtils.setBaseURL(clusterContext.getNodeBaseURL(ip));
        return dockerApi.pushImage(registryUrl,imageId,imageName,tag);
    }

    @ApiOperation("操作指定节点删除镜像")
    @DeleteMapping("/deleteImage")
    public R<Void> deleteImage(@RequestParam("ip")String ip,
                                 @RequestParam("imageId") String imageId,
                                 @RequestParam("imageName") String imageName){
        RestUtils.setBaseURL(clusterContext.getNodeBaseURL(ip));
        R<Void> r=dockerApi.deleteImage(imageName);
        if(r.getCode()!=200){
            return dockerApi.deleteImage(imageId);
        }
        return r;
    }
}
