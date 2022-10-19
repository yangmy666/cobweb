package com.yangmy.cobweb.application.admin.yml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author YangMingYang
 * @since 2022/9/28
 */
@Data
@Component
@ConfigurationProperties(prefix = "cobweb.admin")
public class CobwebAdminYml {

    //cobweb-admin持久化文件夹
    private String cobwebDump;

}
