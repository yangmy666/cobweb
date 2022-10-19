package com.yangmy.cobweb.application.admin.domain.dto;

import com.yangmy.cobweb.common.core.utils.database.Id;
import lombok.Data;

/**
 * @author YangMingYang
 * @since 2022/10/7
 */
@Data
public class ImageRegistry {

    @Id
    private String id;

    private String address;

}
