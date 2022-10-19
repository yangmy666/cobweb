package com.yangmy.cobweb.common.docker;

import org.springframework.util.StringUtils;

import java.util.function.Function;

/**
 * @author YangMingYang
 * @since 2022/10/13
 */
public class MavenUtils {

    /**
     * maven打jar包
     * @param pomDir pom.xml所在目录
     * @param env 环境
     * @param infoFunc 正常输出
     * @param errorFunc 错误输出
     */
    public static void pkg(String pomDir,
                           String env,
                           Function<String,Boolean> infoFunc,
                           Function<String,Boolean> errorFunc){
        String cmd;
        if(StringUtils.hasText(env)){
            cmd="mvn clean package -DskipTests -P"+env+" -f "
                    +pomDir+"/pom.xml";
        }else{
            cmd="mvn clean package -DskipTests -f "
                    +pomDir+"/pom.xml";
        }
        CmdUtils.exec(cmd,infoFunc,errorFunc);
    }
}
