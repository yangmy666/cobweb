package com.yangmy.cobweb.common.core.utils.rest;

import com.yangmy.cobweb.common.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YangMingYang
 * @since 2022/10/5
 */
@Slf4j
@Component
@Aspect
public class RestApiAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    private void GetMapping() {}
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    private void PostMapping() {}
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    private void PutMapping() {}
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    private void PatchMapping() {}
    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    private void DeleteMapping() {}
    @Pointcut("@within(com.yangmy.cobweb.common.core.utils.rest.RestApi)")
    private void RestAPi() {}

    @Around("GetMapping()&&RestAPi()")
    public Object getAround(ProceedingJoinPoint pjp) throws Throwable {
        //请求url
        String url=getRequestUrl(pjp,HttpMethod.GET);
        //请求参数
        Map<String,String> query=getRequestParams(pjp);
        try {
            //执行rest请求
            String json;
            if(query.isEmpty()){
                json=RestUtils.get(url);
            }else{
                json=RestUtils.get(url,query);
            }
            //获取方法的返回类型
            Method method=getMethod(pjp);
            Type returnType=method.getGenericReturnType();
            //将请求响应的json字符串转换为方法返回类型的对象
            return JsonUtils.parseJson(json,returnType);
        }catch (Exception e){
            log.error("发送请求失败'{}'",url,e);
            //执行熔断
            return pjp.proceed();
        }
    }

    @Around("PostMapping()&&RestAPi()")
    public Object postAround(ProceedingJoinPoint pjp) throws Throwable {
        //请求url
        String url=getRequestUrl(pjp,HttpMethod.POST);
        //请求参数
        Map<String,String> form=getRequestParams(pjp);
        try {
            //执行rest请求
            String json=RestUtils.post(url,form);
            //获取方法的返回类型
            Method method=getMethod(pjp);
            Type returnType=method.getGenericReturnType();
            //将请求响应的json字符串转换为方法返回类型的对象
            return JsonUtils.parseJson(json,returnType);
        }catch (Exception e){
            log.error("发送请求失败'{}'",url,e);
            //执行熔断
            return pjp.proceed();
        }
    }

    @Around("PutMapping()&&RestAPi()")
    public Object putAround(ProceedingJoinPoint pjp) throws Throwable {
        //请求url
        String url=getRequestUrl(pjp,HttpMethod.PUT);
        //请求参数
        Map<String,String> form=getRequestParams(pjp);
        try {
            //执行rest请求
            String json=RestUtils.put(url,form);
            //获取方法的返回类型
            Method method=getMethod(pjp);
            Type returnType=method.getGenericReturnType();
            //将请求响应的json字符串转换为方法返回类型的对象
            return JsonUtils.parseJson(json,returnType);
        }catch (Exception e){
            log.error("发送请求失败'{}'",url,e);
            //执行熔断
            return pjp.proceed();
        }
    }

    @Around("PatchMapping()&&RestAPi()")
    public Object patchAround(ProceedingJoinPoint pjp) throws Throwable {
        //请求url
        String url=getRequestUrl(pjp,HttpMethod.PATCH);
        //请求参数
        Map<String,String> form=getRequestParams(pjp);
        try {
            //执行rest请求
            String json=RestUtils.patch(url,form);
            //获取方法的返回类型
            Method method=getMethod(pjp);
            Type returnType=method.getGenericReturnType();
            //将请求响应的json字符串转换为方法返回类型的对象
            return JsonUtils.parseJson(json,returnType);
        }catch (Exception e){
            log.error("发送请求失败'{}'",url,e);
            //执行熔断
            return pjp.proceed();
        }
    }

    @Around("DeleteMapping()&&RestAPi()")
    public Object deleteAround(ProceedingJoinPoint pjp) throws Throwable {
        //请求url
        String url=getRequestUrl(pjp,HttpMethod.DELETE);
        //请求参数
        Map<String,String> form=getRequestParams(pjp);
        try {
            //执行rest请求
            String json=RestUtils.delete(url,form);
            //获取方法的返回类型
            Method method=getMethod(pjp);
            Type returnType=method.getGenericReturnType();
            //将请求响应的json字符串转换为方法返回类型的对象
            return JsonUtils.parseJson(json,returnType);
        }catch (Exception e){
            log.error("发送请求失败'{}'",url,e);
            //执行熔断
            return pjp.proceed();
        }
    }

    //获取请求url
    private String getRequestUrl(ProceedingJoinPoint pjp, HttpMethod httpMethod){
        Method method=getMethod(pjp);
        Class<?> clazz=method.getDeclaringClass();
        //从线程变量中得到baseUrl
        String baseUrl=RestUtils.getBaseURL()!=null?RestUtils.getBaseURL():"";
        //解析springmvc注解得到请求path
        RequestMapping requestMapping=clazz.getAnnotation(RequestMapping.class);
        String requestPath=requestMapping.value()[0]!=null?requestMapping.value()[0]:"";
        String path="";
        if(httpMethod==HttpMethod.GET){
            GetMapping mapping=method.getAnnotation(GetMapping.class);
            path=mapping.value()[0]!=null?mapping.value()[0]:"";
        }
        if(httpMethod==HttpMethod.POST){
            PostMapping mapping=method.getAnnotation(PostMapping.class);
            path=mapping.value()[0]!=null?mapping.value()[0]:"";
        }
        if(httpMethod==HttpMethod.PUT){
            PutMapping mapping=method.getAnnotation(PutMapping.class);
            path=mapping.value()[0]!=null?mapping.value()[0]:"";
        }
        if(httpMethod==HttpMethod.PATCH){
            PatchMapping mapping=method.getAnnotation(PatchMapping.class);
            path=mapping.value()[0]!=null?mapping.value()[0]:"";
        }
        if(httpMethod==HttpMethod.DELETE){
            DeleteMapping mapping=method.getAnnotation(DeleteMapping.class);
            path=mapping.value()[0]!=null?mapping.value()[0]:"";
        }

        return baseUrl+requestPath+path;
    }

    //获取请求参数
    private Map<String,String> getRequestParams(ProceedingJoinPoint pjp)throws Throwable{
        Method method=getMethod(pjp);
        //请求参数
        Map<String,String> params=new HashMap<>();
        Object[] args= pjp.getArgs();
        //根据RequestParam注解构建
        //一个参数可能有多个注解,所以是二维数组
        Annotation[][] annotations=method.getParameterAnnotations();
        for (int i=0;i<annotations.length;i++) {
            Annotation[] annotationArray=annotations[i];
            for (Annotation a : annotationArray) {
                if(a instanceof RequestParam){
                    RequestParam requestParam= (RequestParam) a;
                    String argName=requestParam.value();
                    if(args[i]!=null){
                        params.put(argName,args[i].toString());
                    }
                }
            }
        }
        //如果没有RequestParam且参数只有一个则根据该参数对象构建
        if(params.isEmpty()&&args.length==1){
            //根据对象构建参数
            Object o=args[0];
            assert o != null;
            Field[] fields=o.getClass().getDeclaredFields();
            for (Field field : fields) {
                String queryKey=field.getName();
                String queryValue=field.get(o).toString();
                params.put(queryKey,queryValue);
            }
        }
        return params;
    }

    //根据JoinPoint对象获取method对象
    private Method getMethod(JoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        return methodSignature.getMethod();
    }

}
