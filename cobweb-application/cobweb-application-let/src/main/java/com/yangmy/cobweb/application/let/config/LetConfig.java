package com.yangmy.cobweb.application.let.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author YangMingYang
 * @since 2022/10/12
 */
@Configuration
public class LetConfig {

    @Bean
    public ThreadPoolExecutor executor(){
        return new ThreadPoolExecutor(20,100,
                30, TimeUnit.SECONDS,new LinkedBlockingQueue<>());
    }

}
