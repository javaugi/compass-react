/*
 * Copyright (C) 2016 Center for Information Management, Inc.
 *
 * This program is proprietary.
 * Redistribution without permission is strictly prohibited.
 * For more information, contact <http://www.ciminc.com>
 */
package com.ciminc.spring.memoize;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.ciminc.spring.test.SpringBootTestConfig;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.beans.factory.config.Scope;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author ben
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootTestConfig.class)
public class MemoizerTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public ExternalResource resource = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            testScope.vaporize();
        }
    };

    @Autowired
    MemoizerC memoizerC;
    @Autowired
    TestScope testScope;

    @Test
    public void testMemoize() throws Exception {
        Logger logger = (Logger) LoggerFactory.getLogger(Memoizer.class);
        logger.setLevel(Level.DEBUG);
        memoizerC.memoizeEcho("1");
        memoizerC.memoizeEcho("1");
        assertTrue(systemOutRule.getLog().contains("Using memoized"));
    }

    @Test
    public void testMemoize1() throws Exception {
        Logger logger = (Logger) LoggerFactory.getLogger(Memoizer.class);
        logger.setLevel(Level.DEBUG);
        memoizerC.memoizeEcho("1");
        memoizerC.memoizeEcho("2");
        memoizerC.memoizeEcho("3");
        memoizerC.memoizeEcho("1");
        memoizerC.memoizeEcho("2");
        memoizerC.memoizeEcho("3");
        assertEquals(StringUtils.countMatches(systemOutRule.getLog(), "Using memoized result"), 3);
        assertEquals(StringUtils.countMatches(systemOutRule.getLog(), "Memoizing result"), 3);
    }

    @Test
    public void testMemoizeClearScope() throws Exception {
        Logger logger = (Logger) LoggerFactory.getLogger(Memoizer.class);
        logger.setLevel(Level.DEBUG);
        memoizerC.memoizeEcho("1");
        testScope.vaporize();
        memoizerC.memoizeEcho("1");
        assertTrue(StringUtils.countMatches(systemOutRule.getLog(), "Memoizing result") == 2);
    }

    @Test
    public void testMemoizeClearCache() throws Exception {
        Logger logger = (Logger) LoggerFactory.getLogger(Memoizer.class);
        logger.setLevel(Level.DEBUG);

    }
}

@Component
class MemoizerC {

    @Memoize(scopeName = "test")
    public String memoizeEcho(String argument) {
        return argument;
    }
}

@Configuration
class MemoizerScope {

    @Bean
    public CustomScopeConfigurer testScope(TestScope scope) {
        CustomScopeConfigurer scopeConfigurer = new CustomScopeConfigurer();
        scopeConfigurer.addScope("test", scope);
        return scopeConfigurer;
    }

    @Bean
    public TestScope getTestSessioncope() {
        return new TestScope();
    }

}

class TestScope implements Scope {

    private Map<String, Object> objectMap = Collections.synchronizedMap(new HashMap<String, Object>());

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        if (!objectMap.containsKey(name)) {
            objectMap.put(name, objectFactory.getObject());
        }
        return objectMap.get(name);
    }

    @Override
    public Object remove(String name) {
        return objectMap.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return "test";
    }

    public void vaporize() {
        objectMap.clear();
    }
}
