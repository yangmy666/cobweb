package com.yangmy.cobweb.common.core.domain;

import com.yangmy.cobweb.common.core.constant.Status;
import lombok.Data;

/**
 * @author YangMingYang
 */
@Data
public class R<T> {

    /** 状态码 */
    private Integer code;
    /** 返回信息 */
    private String msg;
    /** 数据对象 */
    private T data;

    public R(Integer code, String msg, T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }

    public static <T> R<T> success(String msg){
        return new R<>(200,msg,null);
    }

    public static <T> R<T> success(T data){
        return new R<>(200,"success",data);
    }

    public static <T> R<T> success(){
        return new R<>(200,"success",null);
    }

    public static <T> R<T> failed(String msg){
        return new R<>(500,msg,null);
    }

    public static <T> R<T> failed(){
        return new R<>(500,"failed",null);
    }

    public static <T> R<T> valid(String msg){
        return new R<>(400,msg,null);
    }

    public static <T> R<T> unAuthorized(){
        return new R<>(401,"unAuthorized",null);
    }

    public static <T> R<T> forbidden(String msg){
        return new R<>(403,msg,null);
    }

    public static R<String> sessionId(String sessionId){
        return new R<>(Status.SESSION_ID,null,sessionId);
    }

    public static R<String> text(String text){
        return new R<>(Status.TEXT,null,text);
    }
}
