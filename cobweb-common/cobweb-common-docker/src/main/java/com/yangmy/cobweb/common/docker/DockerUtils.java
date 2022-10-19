package com.yangmy.cobweb.common.docker;

import com.yangmy.cobweb.common.docker.domain.DockerContainer;
import com.yangmy.cobweb.common.docker.domain.DockerImage;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
public class DockerUtils {

    public static Integer getImageNum(){
        String cmd="docker images -q";
        String info=CmdUtils.exec(cmd);
        if(StringUtils.hasText(info)){
            return info.split("\n").length;
        }
        return 0;
    }

    public static Integer getContainerNum(){
        String cmd="docker ps -q";
        String info=CmdUtils.exec(cmd);
        if(StringUtils.hasText(info)){
            return info.split("\n").length;
        }
        return 0;
    }

    public static void buildImage(String imageName,String tag,String dockerfile,Function<String,Boolean> infoFunc, Function<String,Boolean> errorFunc){
        String cmd="docker build -t "+imageName+":"+tag+" "+dockerfile;
        CmdUtils.exec(cmd,infoFunc,errorFunc);
    }

    public static String buildImage(String imageName,String tag,String dockerfile){
        String cmd="docker build -t "+imageName+":"+tag+" "+dockerfile;
        return CmdUtils.exec(cmd);
    }

    public static void logs(String containerId, Function<String,Boolean> infoFunc, Function<String,Boolean> errorFunc){
        String cmd="docker logs -f -n 100 "+containerId;
        CmdUtils.exec(cmd,infoFunc,errorFunc);
    }

    /**
     * @param registryUrl 例如 192.168.96.135:5000
     */
    public static String pullImage(String registryUrl,String imageName,String tag){
        String cmd;
        if(StringUtils.hasText(registryUrl)){
            if(StringUtils.hasText(tag)){
                cmd="docker pull "+registryUrl+"/"+imageName+":"+tag;
            }else{
                cmd="docker pull "+registryUrl+"/"+imageName;
            }
        }else{
            if(StringUtils.hasText(tag)){
                cmd="docker pull "+imageName+":"+tag;
            }else{
                cmd="docker pull "+imageName;
            }
        }
        return CmdUtils.exec(cmd);
    }

    public static String deleteImage(String image){
        String cmd="docker rmi -f "+image;
        return CmdUtils.exec(cmd);
    }


    public static void pushImage(String registryUrl,String imageOldName,String OldTag,
                                   String imageNewName,String newTag,
                                   Function<String,Boolean> infoFunc, Function<String,Boolean> errorFunc){
        String fullName=registryUrl+"/"+imageNewName+":"+newTag;
        String tagCmd="docker tag "+imageOldName+":"+OldTag+" "+fullName;
        CmdUtils.exec(tagCmd);
        String pushCmd="docker push "+fullName;
        CmdUtils.exec(pushCmd,infoFunc,errorFunc);
    }

    public static String pushImage(String registryUrl,String imageOldName,String OldTag,
                                   String imageNewName,String newTag){
        String fullName=registryUrl+"/"+imageNewName+":"+newTag;
        String tagCmd="docker tag "+imageOldName+":"+OldTag+" "+fullName;
        CmdUtils.exec(tagCmd);
        String pushCmd="docker push "+fullName;
        return CmdUtils.exec(pushCmd);
    }

    public static String pushImage(String registryUrl,String imageId,String imageName,String tag){
        String fullName=registryUrl+"/"+imageName+":"+tag;
        String tagCmd="docker tag "+imageId+" "+fullName;
        CmdUtils.exec(tagCmd);
        String pushCmd="docker push "+fullName;
        return CmdUtils.exec(pushCmd);
    }

    public static String runContainer(String containerName,String options,String imageName,String tag){
        String cmd="docker run -d "+options+" "+"--name="+containerName+" "+imageName+":"+tag;
        return CmdUtils.exec(cmd);
    }

    public static String startContainer(String containerId){
        String cmd="docker start "+containerId;
        return CmdUtils.exec(cmd);
    }

    public static String stopContainer(String containerId){
        String cmd="docker stop "+containerId;
        return CmdUtils.exec(cmd);
    }

    public static String deleteContainer(String containerId){
        String cmd="docker rm -f "+containerId;
        return CmdUtils.exec(cmd);
    }

    public static List<DockerImage> listImage(){
        List<DockerImage> resList=new LinkedList<>();

        String cmd="docker images";
        String result=CmdUtils.exec(cmd);
        List<String> lineList = new ArrayList<>(Arrays.asList(result.split("\n")));
        String title=lineList.get(0);
        int REPOSITORY=title.indexOf("REPOSITORY");
        int TAG=title.indexOf("TAG");
        int IMAGE_ID=title.indexOf("IMAGE ID");
        int CREATED=title.indexOf("CREATED");
        int SIZE=title.indexOf("SIZE");
        lineList.remove(0);
        for (String line : lineList) {
            DockerImage dockerImage =new DockerImage();
            dockerImage.setRepository(line.substring(REPOSITORY,TAG).trim());
            dockerImage.setTag(line.substring(TAG,IMAGE_ID).trim());
            dockerImage.setImageId(line.substring(IMAGE_ID,CREATED).trim());
            dockerImage.setCreated(line.substring(CREATED,SIZE).trim());
            dockerImage.setSize(line.substring(SIZE).trim());
            resList.add(dockerImage);
        }
        return resList;
    }

    public static List<DockerContainer> listContainer(){
        List<DockerContainer> resList=new LinkedList<>();

        String cmd="docker ps -a";
        String result=CmdUtils.exec(cmd);
        List<String> lineList = new ArrayList<>(Arrays.asList(result.split("\n")));
        String title=lineList.get(0);
        int CONTAINER_ID=title.indexOf("CONTAINER ID");
        int IMAGE=title.indexOf("IMAGE");
        int COMMAND=title.indexOf("COMMAND");
        int CREATED=title.indexOf("CREATED");
        int STATUS=title.indexOf("STATUS");
        int PORTS=title.indexOf("PORTS");
        int NAMES=title.indexOf("NAMES");
        lineList.remove(0);
        for (String line : lineList) {
            DockerContainer dockerContainer =new DockerContainer();
            dockerContainer.setContainerId(line.substring(CONTAINER_ID,IMAGE).trim());
            dockerContainer.setImage(line.substring(IMAGE,COMMAND).trim());
            dockerContainer.setCommand(line.substring(COMMAND,CREATED).trim());
            dockerContainer.setCreated(line.substring(CREATED,STATUS).trim());
            dockerContainer.setStatus(line.substring(STATUS,PORTS).trim());
            dockerContainer.setPorts(line.substring(PORTS,NAMES).trim());
            dockerContainer.setNames(line.substring(NAMES).trim());
            resList.add(dockerContainer);
        }
        return resList;
    }


}
