package com.yangmy.cobweb.common.docker.domain;

import lombok.Data;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
@Data
public class DockerContainer {

    private String containerId;

    private String image;

    private String command;

    private String created;

    private String status;

    private String ports;

    private String names;
}
