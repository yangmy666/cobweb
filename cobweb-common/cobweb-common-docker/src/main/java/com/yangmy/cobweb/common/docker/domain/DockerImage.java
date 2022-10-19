package com.yangmy.cobweb.common.docker.domain;

import lombok.Data;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
@Data
public class DockerImage {

    private String repository;

    private String tag;

    private String imageId;

    private String created;

    private String size;
}
