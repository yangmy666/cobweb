package com.yangmy.cobweb.application.let.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author YangMingYang
 * @since 2022/10/1
 */
@Slf4j
@Component
public class CobwebApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        event.getApplicationContext();
    }
}

