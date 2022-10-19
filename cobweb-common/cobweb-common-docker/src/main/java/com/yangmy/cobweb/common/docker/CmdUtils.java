package com.yangmy.cobweb.common.docker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;

/**
 * @author YangMingYang
 * @since 2022/9/29
 */
@Slf4j
public class CmdUtils {

    private static Process exe(String cmd) throws IOException {
        log.info("执行命令：{}",cmd);
        String[] cmdArray=cmd.split("\\s+");
        return Runtime.getRuntime().exec(cmdArray);
    }

    public static void exec(String cmd, Function<String,Boolean> infoFunc, Function<String,Boolean> errorFunc){
        InputStreamReader isr = null;
        BufferedReader br=null;
        InputStreamReader errorIsr = null;
        BufferedReader errorBre=null;
        StringBuilder info= new StringBuilder();
        StringBuilder error= new StringBuilder();
        try {
            //执行命令
            Process process= exe(cmd);

            //读取成功输出
            isr = new InputStreamReader(process.getInputStream());
            br = new BufferedReader(isr);
            String line=br.readLine();
            boolean next=true;
            /*BufferedReader的readLine()方法在流未关闭但没有数据输出时会阻塞，
                只有当流的对端关闭时才会返回null。*/
            while(next&&line!=null){
                info.append(line).append("\n");
                next=infoFunc.apply(line);
                line=br.readLine();
            }

            //读取错误输出
            errorIsr = new InputStreamReader(process.getErrorStream());
            errorBre = new BufferedReader(errorIsr);
            line=errorBre.readLine();
            next=true;
            while(next&&line!=null){
                error.append(line).append("\n");
                next=errorFunc.apply(line);
                line=br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(isr!=null){
                    isr.close();
                }
                if(br!=null){
                    br.close();
                }
                if(errorIsr!=null){
                    errorIsr.close();
                }
                if(errorBre!=null){
                    errorBre.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(StringUtils.hasText(info)){
            log.info("cmd执行结果:'{}'",info);
        }
        if(StringUtils.hasText(error)){
            throw new RuntimeException("cmd执行异常:'"+error+"'");
        }
    }

    public static String exec(String cmd){
        InputStreamReader isr = null;
        BufferedReader br=null;
        InputStreamReader errorIsr = null;
        BufferedReader errorBre=null;
        StringBuilder info= new StringBuilder();
        StringBuilder error= new StringBuilder();
        try {
            //执行命令
            Process process= exe(cmd);
            isr = new InputStreamReader(process.getInputStream());
            br = new BufferedReader(isr);
            String line=br.readLine();
            while(line!=null){
                info.append(line).append("\n");
                line=br.readLine();
            }

            errorIsr = new InputStreamReader(process.getErrorStream());
            errorBre = new BufferedReader(errorIsr);
            line=errorBre.readLine();
            while(line!=null){
                error.append(line).append("\n");
                line=errorBre.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(isr!=null){
                    isr.close();
                }
                if(br!=null){
                    br.close();
                }
                if(errorIsr!=null){
                    errorIsr.close();
                }
                if(errorBre!=null){
                    errorBre.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(StringUtils.hasText(info)){
            log.info("cmd执行结果:'{}'",info);
        }
        if(StringUtils.hasText(error)){
            throw new RuntimeException("cmd执行异常:'"+error+"'");
        }
        return info.toString();
    }

}
