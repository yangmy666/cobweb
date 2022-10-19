package com.yangmy.cobweb.application.admin.domain.task;

import com.yangmy.cobweb.application.admin.api.let.DockerApi;
import com.yangmy.cobweb.application.admin.utils.ClusterContext;
import com.yangmy.cobweb.common.core.domain.R;
import com.yangmy.cobweb.common.core.utils.SpringUtils;
import com.yangmy.cobweb.common.core.utils.rest.RestUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author YangMingYang
 * @since 2022/10/6
 */
@Slf4j
@Data
public class PullImageTask{

    private String nodeIp;

    private String registryUrl;

    private String imageName;

    private String tag;

    public String exec(){
        DockerApi dockerApi= SpringUtils.getBean(DockerApi.class);
        ClusterContext clusterContext=SpringUtils.getBean(ClusterContext.class);
        //选择节点ip
        RestUtils.setBaseURL(clusterContext.getNodeBaseURL(nodeIp));
        R<Void> r= dockerApi.pullImage(registryUrl,imageName,tag);
        return r.getMsg();
    }
}
