package com.yangmy.cobweb.application.let.api.admin;

import com.yangmy.cobweb.common.core.domain.R;
import com.yangmy.cobweb.common.core.utils.rest.RestApi;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
@Component
@RestApi
@RequestMapping("/cluster")
public class ClusterApi {

    @ApiOperation("心跳一次")
    @PostMapping("/heartbeat")
    public R<Void> heartbeat(@RequestParam("ip") String ip,
                             @RequestParam("baseURL") String baseURL){
        throw new RuntimeException("接口调用失败");
    }

}
