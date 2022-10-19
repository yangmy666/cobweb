package com.yangmy.cobweb.common.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YangMingYang
 * @since 2022/10/7
 */
public class JsonUtils {

    //递归解析复杂json字符串为泛型对象
    public static Object parseJson(String json, Type t1) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        if(t1 instanceof ParameterizedType){
            Type t2 = getTypeArgument(t1);
            Class<?> c1=Class.forName(((ParameterizedType) t1).getRawType().getTypeName());
            if(c1== List.class){
                //解数组
                List<String> list= JSON.parseArray(json,String.class);

                List<Object> resList=new ArrayList<>();
                for (String s : list) {
                    Object o=parseJson(s,t2);
                    resList.add(o);
                }
                return resList;
            }else{
                //解R<DockerImage>这种
                Object o=parseJson(json,c1);
                Field field=c1.getDeclaredField("data");
                field.setAccessible(true);
                Object data=field.get(o);
                if(data!=null){
                    data=parseJson(data.toString(),t2);
                    field.set(o,data);
                }
                return o;
            }
        }
        else{
            //解对象
            Class<?> c1=Class.forName(t1.getTypeName());
            try {
                return JSON.parseObject(json,c1);
            }catch (JSONException e){
                return json;
            }
        }
    }

    //获取一个类的第一个泛型
    private static Type getTypeArgument(Type type) {
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        return actualTypeArguments[0];
    }
}
