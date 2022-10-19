package com.yangmy.cobweb.application.admin.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author YangMingYang
 * @since 2022/10/15
 */
@Data
public class ImageArgs{

    @ApiModelProperty("构建镜像名称")
    private String imageName;

    @ApiModelProperty("dockerfile所在目录")
    private String dockerfile;

    @ApiModelProperty("创建容器参数")
    private List<ContainerArgs> containerArgsList;
}
