package xyz.bycor.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
/*
 * @Description: 从Spring容器中拿到其他Bean对象
 */


public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 从Spring容器中拿到其他Bean对象
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }


    /**
     * 从快速发布自定义事件
     */
    public static void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }


}
