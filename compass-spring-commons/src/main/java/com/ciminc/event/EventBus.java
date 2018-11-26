/*
 * Copyright (C) 2016 Center for Information Management, Inc.
 *
 * This program is proprietary.
 * Redistribution without permission is strictly prohibited.
 * For more information, contact <http://www.ciminc.com>
 */
package com.ciminc.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 *
 * provide static access to event bus
 *
 * @author ben
 * @version $LastChangedRevision $LastChangedDate Last Modified Author:
 * $LastChangedBy
 */
public class EventBus {

    private static final Logger log = LoggerFactory.getLogger(EventBus.class);
    private static ApplicationContext APPLICATION_CONTEXT;

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    public static void post(Object event) {
        APPLICATION_CONTEXT.getBean(IEventBus.class).post(event);
    }

    public static void postAsync(Object object) {
        APPLICATION_CONTEXT.getBean(IEventBus.class).postAsync(object);
    }

    public static void register(Object object) {
        APPLICATION_CONTEXT.getBean(IEventBus.class).register(object);
    }

    public static void unregister(Object object) {
        APPLICATION_CONTEXT.getBean(IEventBus.class).unregister(object);
    }

}
