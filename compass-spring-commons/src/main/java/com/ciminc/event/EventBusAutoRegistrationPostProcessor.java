/*
 * Copyright (C) 2016 Center for Information Management, Inc.
 *
 * This program is proprietary.
 * Redistribution without permission is strictly prohibited.
 * For more information, contact <http://www.ciminc.com>
 */
package com.ciminc.event;

import com.ciminc.event.EventBus;
import com.ciminc.event.IEventBus;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import net.engio.mbassy.listener.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 *
 * post processor will automatically register any spring beans with any method
 * that has a Handler annotation so we will no longer have to register every
 * bean that wants to listen to events
 *
 *
 * @author ben
 * @version $LastChangedRevision $LastChangedDate Last Modified Author:
 * $LastChangedBy
 */
@Component
public class EventBusAutoRegistrationPostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(EventBusAutoRegistrationPostProcessor.class);
    private static final EventBusBeanPostProcessor EVENTBUS_BEANPOST_PROCESSOR = new EventBusBeanPostProcessor();
    protected static final Set HANDLER_CLASSES = new HashSet();
    private static ApplicationContext APPLICATION_CONTEXT;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.addBeanPostProcessor(EVENTBUS_BEANPOST_PROCESSOR);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
        for (String beanName : applicationContext.getBeanDefinitionNames()) {
            Class<?> objClz = applicationContext.getType(beanName);

            for (Method m : objClz.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Handler.class)) {
                    EventBusAutoRegistrationPostProcessor.HANDLER_CLASSES.add(objClz);
                    break;
                }
            }
        }
    }

    static class EventBusBeanPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (APPLICATION_CONTEXT != null) {
                Class<?> beanClass = bean.getClass();
                if (org.springframework.aop.support.AopUtils.isAopProxy(bean)) {
                    beanClass = org.springframework.aop.support.AopUtils.getTargetClass(bean);
                }
                if (EventBusAutoRegistrationPostProcessor.HANDLER_CLASSES.contains(beanClass)) {
                    IEventBus bus = APPLICATION_CONTEXT.getBean(IEventBus.class);
                    if (bus != null) {
                        EventBus.register(bean);
                    }
                }
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

    }
}
