package com.yangmy.cobweb.common.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author YangMingYang
 * @since 2022/10/1
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext=applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType){
        return SpringUtils.applicationContext.getBean(requiredType);
    }
}
