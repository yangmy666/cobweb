package com.yangmy.cobweb.common.core.utils.database;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author YangMingYang
 * @since 2022/10/1
 */
public class FileUtils {

    /**
     * 读取文件内容为字符串
     */
    public static String getFile(String fileName){
        File file=new File(fileName);
        if(!file.exists()){
            return null;
        }
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br=null;
        StringBuilder str=new StringBuilder();
        try {
            is = new FileInputStream(file);
            isr=new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line=br.readLine();
            while(line!=null){
                str.append(line);
                line=br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is!=null){
                    is.close();
                }
                if(isr!=null){
                    isr.close();
                }
                if(br!=null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str.toString();
    }

    /**
     * 保存或者全量修改文件内容
     */
    public static void putFile(String fileName,String content){
        createFile(fileName);
        File file=new File(fileName);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte[] bytes=content.getBytes(StandardCharsets.UTF_8);
            os.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createFile(String fileName){
        File file=new File(fileName);
        try {
            boolean flag=file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createDir(String dirPath){
        File file = new File(dirPath);
        boolean flag = file.mkdirs();
    }

    public static void saveFile(String dir,MultipartFile multipartFile){
        String fileName= dir+multipartFile.getOriginalFilename();
        File file=new File(fileName);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte[] bytes=multipartFile.getBytes();
            os.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
