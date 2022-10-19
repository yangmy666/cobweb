package com.yangmy.cobweb.application.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author YangMingYang
 * @since 2022/10/15
 */
@Data
public class ContainerArgs{

    @ApiModelProperty("节点ip")
    private String nodeIp;

    @ApiModelProperty("容器选项")
    private String options;
}
