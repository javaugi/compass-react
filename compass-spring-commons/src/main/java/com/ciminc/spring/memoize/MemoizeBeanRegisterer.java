/*
 * Copyright (C) 2016 Center for Information Management, Inc.
 *
 * This program is proprietary.
 * Redistribution without permission is strictly prohibited.
 * For more information, contact <http://www.ciminc.com>
 */
package com.ciminc.spring.memoize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import java.util.List;
import java.util.Arrays;

/**
 *
 * This class is responsible for adding bean definitions for InvocationCache's
 * for each known scope. This will allow us to differ the cache's life cycle to
 * spring for management.
 *
 * The bean definition for this class is in the XML file because
 * BeanDefinitionRegistryPostProcessor's cannot be implemented during annotation
 * scanning. This is a limitation of spring.
 *
 * @author ben
 * @version $LastChangedRevision $LastChangedDate Last Modified Author:
 * $LastChangedBy
 */
public class MemoizeBeanRegisterer implements BeanDefinitionRegistryPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(MemoizeBeanRegisterer.class);
    public static final String MEMOIZER_CACHE_BEAN_NAME_SUFFIX = "_memoizer_cache";
    GenericApplicationContext applicationContext;
    //TODO: it would be nice if we could get notified when a Scope is registered
    // with spring and update this list
    public static List<String> MEMOIZER_KNOWN_SCOPES = Arrays.asList(
            "test"
    );

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (String registeredScopeName : MEMOIZER_KNOWN_SCOPES) {
            log.info("Registering Memoizer bean for scope {}", registeredScopeName);
            registry.registerBeanDefinition(getBeanNameForScope(registeredScopeName), getInvocationCacheDefinition(registeredScopeName));
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.debug("Memoizer post bean factory");
    }

    public static String getBeanNameForScope(String scopeName) {
        return scopeName + MEMOIZER_CACHE_BEAN_NAME_SUFFIX;
    }

    BeanDefinition getInvocationCacheDefinition(String scope) {
        BeanDefinition bd = new GenericBeanDefinition();
        bd.setBeanClassName(InvocationCache.class.getCanonicalName());
        bd.setScope(scope);
        return bd;
    }

}
