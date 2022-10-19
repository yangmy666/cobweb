package com.yangmy.cobweb.common.core.utils.database;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author YangMingYang
 * @since 2022/10/7
 */
@Slf4j
public class JSONDatabase {

    private JSONDatabase(String dataDir){
        FileUtils.createDir(dataDir);
        this.dataDir=dataDir;
    }

    private final String dataDir;

    public static JSONDatabase build(String dataDir){
        return new JSONDatabase(dataDir);
    }

    //持久化
    private  <T> void dump(List<T> list,Class<T> clazz){
        String json=JSON.toJSONString(list);
        String fileName=clazz.getSimpleName();
        fileName=dataDir+"/"+fileName+".json";
        FileUtils.putFile(fileName,json);
    }

    //查询全部
    public  <T> List<T> list(Class<T> clazz){
        String fileName=clazz.getSimpleName();
        fileName=dataDir+"/"+fileName+".json";
        try{
            String json= FileUtils.getFile(fileName);
            return JSON.parseArray(json,clazz);
        }catch (JSONException e){
            return null;
        }
    }

    //根据id查找
    public  <T> T getById(Object id,Class<T> clazz){
        List<T> list= list(clazz);
        if(list==null){
            return null;
        }
        for (T item : list) {
            Field[] fields=item.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean isId=field.isAnnotationPresent(Id.class);
                if(isId){
                    try {
                        field.setAccessible(true);
                        Object itemId=field.get(item);
                        if(id.equals(itemId)){
                            return item;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        return null;
    }

    //新增
    public  <T> void add(T t,Class<T> clazz){
        Field[] fields=t.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean isId=field.isAnnotationPresent(Id.class);
            if(isId){
                field.setAccessible(true);
                String id= UUID.randomUUID().toString();
                try {
                    field.set(t,id);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        List<T> l= list(clazz);
        if(l==null){
            l=new ArrayList<>();
        }
        l.add(t);
        dump(l,clazz);
    }

    //全量修改
    public  <T> void updateById(T t,Class<T> clazz){
        List<T> list= list(clazz);
        if(list==null){
            return;
        }
        //遍历查找
        for (T item : list) {
            Field[] fields=item.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean isId=field.isAnnotationPresent(Id.class);
                if(isId){
                    field.setAccessible(true);
                    try {
                        Object itemId=field.get(item);
                        Object tId=field.get(t);
                        if(itemId.equals(tId)){
                            list.remove(item);
                            list.add(t);
                            dump(list,clazz);
                            return;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    //删除
    public  <T> void deleteById(Object id,Class<T> clazz){
        List<T> list= list(clazz);
        if(list==null){
            return;
        }
        list.removeIf(item -> {
            Field[] fields=item.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean isId=field.isAnnotationPresent(Id.class);
                if(isId){
                    try {
                        field.setAccessible(true);
                        Object itemId=field.get(item);
                        if(id.equals(itemId)){
                            return true;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        });
        dump(list,clazz);
    }
}
