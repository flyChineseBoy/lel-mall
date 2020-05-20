package org.lele.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * org.lele.common.utils
 *
 * @author: lele
 * @date: 2020-05-20
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 直接获取applicationContext
     * @return applicationContext
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * 根据名称获取Bean
     *
     * @param beanName bean名称
     * @return Bean实例
     */
    public static Object getBean(String beanName) {
        if (context == null || StringUtils.isBlank(beanName)) {
            return null;
        }
        return context.getBean(beanName);
    }

    /**
     * 根据类型获取Bean
     *
     * @param className bean类型
     * @param <T>       bean类型
     * @return Bean实例
     */
    public static <T> T getBean(Class<T> className) {
        if (context == null || className == null) {
            return null;
        }
        return context.getBean(className);
    }
}
