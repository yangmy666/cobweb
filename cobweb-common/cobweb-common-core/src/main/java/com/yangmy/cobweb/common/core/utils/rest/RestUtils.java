package com.yangmy.cobweb.common.core.utils.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author YangMingYang
 * @since 2022/10/3
 */
public class RestUtils {

    private static final ThreadLocal<String> threadLocal=new ThreadLocal<>();

    public static void setBaseURL(String baseUrl){
        threadLocal.remove();
        threadLocal.set(baseUrl);
    }

    public static String getBaseURL(){
        return threadLocal.get();
    }

    private static final RestTemplate restTemplate=new RestTemplate();

    public static String get(String url){
        ResponseEntity<String> response = restTemplate.getForEntity(url,String.class);
        return response.getBody();
    }

    public static String get(String url,Map<String, String> query){
        StringBuilder params= new StringBuilder();
        for (String key : query.keySet()) {
            if(!params.toString().equals("")){
                params.append("&");
            }
            params.append(key).append("=").append(query.get(key));
        }
        url=url+"?"+ params;
        ResponseEntity<String> response = restTemplate.getForEntity(url,String.class);
        return response.getBody();
    }

    public static String post(String url, Map<String, String> form){
        HttpEntity<String> request=buildFormHttpEntity(form);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        return response.getBody();
    }

    public static String put(String url, Map<String, String> form){
        HttpEntity<String> request=buildFormHttpEntity(form);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT,request,String.class);
        return response.getBody();
    }

    public static String patch(String url, Map<String, String> form){
        HttpEntity<String> request=buildFormHttpEntity(form);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH,request,String.class);
        return response.getBody();
    }

    public static String delete(String url, Map<String, String> form){
        HttpEntity<String> request=buildFormHttpEntity(form);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE,request,String.class);
        return response.getBody();
    }

    //构建表单HttpEntity
    private static HttpEntity<String> buildFormHttpEntity(Map<String, String> form){
        HttpHeaders headers=new HttpHeaders();
        headers.add("Content-Type","application/x-www-form-urlencoded");
        StringBuilder body= new StringBuilder();
        for (String key : form.keySet()) {
            if(!body.toString().equals("")){
                body.append("&");
            }
            body.append(key).append("=").append(form.get(key));
        }
        return new HttpEntity<>(body.toString(),headers);
    }

}
