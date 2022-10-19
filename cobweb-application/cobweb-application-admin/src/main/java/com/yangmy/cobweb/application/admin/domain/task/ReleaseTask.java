package com.yangmy.cobweb.application.admin.domain.task;

import com.yangmy.cobweb.application.admin.config.DumpDirConfig;
import com.yangmy.cobweb.application.admin.domain.dto.ContainerArgs;
import com.yangmy.cobweb.application.admin.domain.dto.ImageArgs;
import com.yangmy.cobweb.application.admin.websocket.ExecReleaseEndpoint;
import com.yangmy.cobweb.common.core.utils.SpringUtils;
import com.yangmy.cobweb.common.core.utils.TimeUtils;
import com.yangmy.cobweb.common.core.utils.database.Id;
import com.yangmy.cobweb.common.docker.DockerUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author YangMingYang
 * @since 2022/10/6
 */
@Slf4j
@Data
public class ReleaseTask {

    private boolean sendLine(String sessionId,String line){
        ExecReleaseEndpoint.sendResult(sessionId,line);
        return true;
    }

    public void exec(String sessionId){
        String tip;
        DumpDirConfig dumpDirConfig= SpringUtils.getBean(DumpDirConfig.class);
        String projectDir=dumpDirConfig.projectDir();
        //计算git工作目录绝对路径
        String workTree=projectDir+this.workTree;
        //克隆
        tip="\n》》》》》》》》》克隆Git仓库》》》》》》》》》\n";
        log.info(tip);
        sendLine(sessionId,tip);
        GitCloneTask gitCloneTask=new GitCloneTask();
        gitCloneTask.setRepositoryUrl(gitRepositoryUrl);
        gitCloneTask.setUsername(username);
        gitCloneTask.setPassword(password);
        gitCloneTask.setBranch(branch);
        gitCloneTask.setWorkTree(workTree);
        try{
            gitCloneTask.exec((line)-> sendLine(sessionId,line),(line)-> sendLine(sessionId,line));
        }catch (RuntimeException e){
            log.warn("克隆代码警告",e);
        }
        if(!ExecReleaseEndpoint.sessionMap.containsKey(sessionId)){
            return;
        }
        //更新
        tip="\n》》》》》》》》》更新代码》》》》》》》》》\n";
        log.info(tip);
        sendLine(sessionId,tip);
        GitPullTask gitPullTask=new GitPullTask();
        gitPullTask.setWorkTree(workTree);
        try{
            gitPullTask.exec((line)-> sendLine(sessionId,line),(line)-> sendLine(sessionId,line));
        }catch (RuntimeException e){
            log.warn("更新代码警告",e);
        }
        if(!ExecReleaseEndpoint.sessionMap.containsKey(sessionId)){
            return;
        }
        //打jar包
        tip="\n》》》》》》》》》Maven打包》》》》》》》》》\n";
        log.info(tip);
        sendLine(sessionId,tip);
        MavenPackageTask mavenPackageTask=new MavenPackageTask();
        mavenPackageTask.setPom(workTree);
        mavenPackageTask.setEnv(env);
        try{
            mavenPackageTask.exec((line)-> sendLine(sessionId,line),(line)-> sendLine(sessionId,line));
        }catch (RuntimeException e){
            log.warn(String.valueOf(e));
        }
        if(!ExecReleaseEndpoint.sessionMap.containsKey(sessionId)){
            return;
        }
        //生成时间戳
        long timestamp= TimeUtils.toTimestamp(LocalDateTime.now());
        if(CollectionUtils.isEmpty(imageArgsList)){
            return;
        }
        for (ImageArgs imageArgs : imageArgsList) {
            String imageName=imageArgs.getImageName();
            String tag=String.valueOf(timestamp);
            //构建镜像
            tip="\n》》》》》》》》》制作镜像 "+imageName+":"+tag+"》》》》》》》》》\n";
            log.info(tip);
            sendLine(sessionId,tip);
            BuildImageTask buildImageTask=new BuildImageTask();
            buildImageTask.setImageName(imageName);
            buildImageTask.setTag(tag);
            //计算dockerfile目录绝对路径
            String dockerfile= workTree+imageArgs.getDockerfile();
            buildImageTask.setDockerfile(dockerfile);
            buildImageTask.exec((line)-> sendLine(sessionId,line),(line)-> sendLine(sessionId,line));
            if(!ExecReleaseEndpoint.sessionMap.containsKey(sessionId)){
                return;
            }
            //推送镜像
            tip="\n》》》》》》》》》推送镜像到仓库"+registryAddress+" "+imageName+":"+tag+"》》》》》》》》》\n";
            log.info(tip);
            sendLine(sessionId,tip);
            PushImageTask pushImageTask=new PushImageTask();
            pushImageTask.setRegistryUrl(registryAddress);
            pushImageTask.setImageLocalName(imageName);
            pushImageTask.setLocalTag(tag);
            pushImageTask.setImageName(imageName);
            pushImageTask.setTag(tag);
            pushImageTask.exec((line)-> sendLine(sessionId,line),(line)-> sendLine(sessionId,line));
            //推送完删掉，避免磁盘爆满
            DockerUtils.deleteImage(registryAddress+"/"+imageName+":"+tag);
            tip="推送完成,删除无用镜像"+registryAddress+"/"+imageName+":"+tag+",避免磁盘爆满";
            log.info(tip);
            sendLine(sessionId,tip);
            DockerUtils.deleteImage(imageName+":"+tag);
            tip="推送完成,删除无用镜像"+imageName+":"+tag+",避免磁盘爆满";
            log.info(tip);
            sendLine(sessionId,tip);

            List<ContainerArgs> containerArgsList=imageArgs.getContainerArgsList();
            if (CollectionUtils.isEmpty(containerArgsList)) {
                continue;
            }
            for (ContainerArgs containerArgs : containerArgsList) {
                String nodeIp=containerArgs.getNodeIp();
                if(!ExecReleaseEndpoint.sessionMap.containsKey(sessionId)){
                    return;
                }
                //拉取镜像
                tip="\n》》》》》》》》》节点"+nodeIp+"拉取镜像 "+imageName+":"+tag+"》》》》》》》》》\n";
                log.info(tip);
                sendLine(sessionId,tip);
                PullImageTask pullImageTask=new PullImageTask();
                pullImageTask.setNodeIp(nodeIp);
                pullImageTask.setRegistryUrl(registryAddress);
                pullImageTask.setImageName(imageName);
                pullImageTask.setTag(tag);
                String res=pullImageTask.exec();
                log.info(res);
                sendLine(sessionId,res);
                if(!ExecReleaseEndpoint.sessionMap.containsKey(sessionId)){
                    return;
                }
                //创建容器并运行
                String containerName=imageName+"-"+tag;
                tip="\n》》》》》》》》》节点"+nodeIp+"创建容器并运行 "+containerName+"》》》》》》》》》\n";
                log.info(tip);
                sendLine(sessionId,tip);
                RunContainerTask runContainerTask=new RunContainerTask();
                runContainerTask.setNodeIp(nodeIp);
                runContainerTask.setContainerName(containerName);
                runContainerTask.setImageName(registryAddress+"/"+imageName);
                runContainerTask.setTag(tag);
                runContainerTask.setOptions(containerArgs.getOptions());
                res=runContainerTask.exec();
                log.info(res);
                sendLine(sessionId,res);
            }
        }
        sendLine(sessionId,"{over}");
    }

    @Id
    private String id;

    private String remark;

    private String gitRepositoryUrl;

    private String username;

    private String password;

    private String branch;

    private String workTree;

    private String env;

    private String registryAddress;

    private List<ImageArgs> imageArgsList;

}
