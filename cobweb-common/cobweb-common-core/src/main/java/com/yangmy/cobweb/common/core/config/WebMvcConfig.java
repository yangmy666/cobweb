package com.yangmy.cobweb.common.core.config;

import com.yangmy.cobweb.common.core.utils.IpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author YangMingYang
 * @since 2022-03-31
 */
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                .addPathPrefix("/"+applicationName, c -> c.isAnnotationPresent(RestController.class));
    }

    @Value("${server.port}")
    private String port;
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 获取当前应用的baseUrl
     * @return
     */
    public String getBaseURL(){
        String ip= IpUtils.getLocalIp4Address();
        return "http://"+ip+":"+port+"/"+applicationName;
    }


}
