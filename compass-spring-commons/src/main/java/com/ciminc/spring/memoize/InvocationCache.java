/*
 * Copyright (C) 2016 Center for Information Management, Inc.
 *
 * This program is proprietary.
 * Redistribution without permission is strictly prohibited.
 * For more information, contact <http://www.ciminc.com>
 */
package com.ciminc.spring.memoize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * This class is basically a wrapper around ConcurrentHashMap to store
 * InvocationContexts with results. ConcurrentHashMap does not accept null keys
 * or values so we substitute our own handling for that.
 *
 * @author ben
 * @version $LastChangedRevision $LastChangedDate Last Modified Author:
 * $LastChangedBy
 */
public class InvocationCache {

    private static final Logger log = LoggerFactory.getLogger(InvocationCache.class);
    public static final Object VOID = new Object();
    /**
     * this needs to remain thread safe
     */
    private final Map<InvocationContext, Object> cache = new ConcurrentHashMap<>();

    public boolean contains(InvocationContext invocationContext) {
        return cache.containsKey(invocationContext);
    }

    public Object get(InvocationContext invocationContext) {
        Object result = cache.get(invocationContext);
        return result == VOID ? null : result;
    }

    public void put(InvocationContext methodInvocation, Object result) {
        cache.put(methodInvocation, null == result ? VOID : result);
    }
}
