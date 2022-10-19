package com.yangmy.cobweb.application.admin.domain.dto;

import lombok.Data;

/**
 * @author YangMingYang
 * @since 2022/10/16
 */
@Data
public class NodeDockerContainer {

    private String ip;

    private String containerId;

    private String image;

    private String command;

    private String created;

    private String status;

    private String ports;

    private String names;
}
