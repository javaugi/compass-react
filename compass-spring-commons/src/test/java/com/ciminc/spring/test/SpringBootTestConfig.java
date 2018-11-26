/*
 * Copyright (C) 2016 Center for Information Management, Inc.
 *
 * This program is proprietary.
 * Redistribution without permission is strictly prohibited.
 * For more information, contact <http://www.ciminc.com>
 */
package com.ciminc.spring.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

/**
 *
 *
 * @author ben
 * @version $LastChangedRevision $LastChangedDate Last Modified Author:
 * $LastChangedBy
 */
@SpringBootApplication
@ComponentScan("com.ciminc")
@EnableCaching
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ImportResource({"classpath*:testapplicationContext.xml"})
public class SpringBootTestConfig {

    private static final Logger log = LoggerFactory.getLogger(SpringBootTestConfig.class);
}
