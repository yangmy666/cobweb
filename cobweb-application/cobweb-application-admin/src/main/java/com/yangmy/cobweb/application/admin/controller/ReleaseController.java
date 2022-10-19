package com.yangmy.cobweb.application.admin.controller;

import com.yangmy.cobweb.application.admin.domain.task.ReleaseTask;
import com.yangmy.cobweb.common.core.domain.R;
import com.yangmy.cobweb.common.core.utils.database.JSONDatabase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
@Validated
@Api(tags = "发布任务相关接口")
@RestController
@RequestMapping("/release")
public class ReleaseController {

    @Resource
    private JSONDatabase jsonDatabase;

    @ApiOperation("查询任务列表")
    @GetMapping("/listReleaseTask")
    public R<List<ReleaseTask>> listReleaseTask(){
        return R.success(jsonDatabase.list(ReleaseTask.class));
    }

    @ApiOperation("保存发布任务")
    @PostMapping("/saveReleaseTask")
    public R<Void> saveReleaseTask(@RequestBody ReleaseTask releaseTask){
        if(!StringUtils.hasText(releaseTask.getId())){
            jsonDatabase.add(releaseTask,ReleaseTask.class);
        }else{
            jsonDatabase.updateById(releaseTask,ReleaseTask.class);
        }
        return R.success();
    }

    @ApiOperation("删除发布任务")
    @DeleteMapping("/deleteReleaseTask")
    public R<Void> deleteReleaseTask(@RequestParam String id){
        jsonDatabase.deleteById(id,ReleaseTask.class);
        return R.success();
    }

}
