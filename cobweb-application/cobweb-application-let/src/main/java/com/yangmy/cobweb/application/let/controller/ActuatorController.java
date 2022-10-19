package com.yangmy.cobweb.application.let.controller;

import com.yangmy.cobweb.application.let.yml.CobwebLetYml;
import com.yangmy.cobweb.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author YangMingYang
 * @since 2022/10/19
 */
@Slf4j
@Validated
@Api(tags = "执行器相关接口")
@RestController
@RequestMapping("/actuator")
public class ActuatorController {

    @Resource
    private CobwebLetYml cobwebLetYml;

    @ApiOperation("设置cobweb-admin地址baseURL")
    @RequestMapping("/setAdmin")
    public R<Void> setAdmin(@RequestParam String baseURL){
        cobwebLetYml.setAdminBaseURL(baseURL);
        return R.success();
    }
}
