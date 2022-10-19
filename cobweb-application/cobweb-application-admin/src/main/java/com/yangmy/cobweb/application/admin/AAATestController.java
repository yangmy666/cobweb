package com.yangmy.cobweb.application.admin;

import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
@Validated
@Api(tags = "测试相关接口")
@RestController
@RequestMapping("/test")
public class AAATestController {



    @GetMapping("/list")
    public void list(){

    }

}
