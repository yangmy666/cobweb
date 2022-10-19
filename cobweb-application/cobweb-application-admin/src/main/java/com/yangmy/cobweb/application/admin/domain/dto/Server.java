package com.yangmy.cobweb.application.admin.domain.dto;

import lombok.Data;

/**
 * @author YangMingYang
 * @since 2022/10/19
 */
@Data
public class Server {

    private String ip;

    private Integer containerNum;

    private Integer imageNum;
}
