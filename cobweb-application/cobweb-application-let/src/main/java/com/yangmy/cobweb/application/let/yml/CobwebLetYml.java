package com.yangmy.cobweb.application.let.yml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
@Data
@Component
@ConfigurationProperties(prefix = "cobweb.let")
public class CobwebLetYml {

    //主节点地址 example http://192.168.96.135:30880/cobweb-admin
    private String adminBaseURL;
}
