/*
 * Copyright (C) 2016 Center for Information Management, Inc.
 *
 * This program is proprietary.
 * Redistribution without permission is strictly prohibited.
 * For more information, contact <http://www.ciminc.com>
 */
package com.ciminc.spring.memoize;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * This is an aspectj aspect that will cache method call results and return the
 * cached results on subsequent invocations with the same arguments.
 *
 * example:
 *
 *
 * @author ben
 * @version $LastChangedRevision $LastChangedDate Last Modified Author:
 * $LastChangedBy
 */
@Component
@Aspect
public class Memoizer {

    private static final Logger log = LoggerFactory.getLogger(Memoizer.class);
    @Autowired
    private GenericApplicationContext applicationContext;

    @Around("@annotation(com.ciminc.spring.memoize.Memoize)")
    public Object memoize(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        Memoize annotation = sig.getMethod().getAnnotation(Memoize.class);
        String scopeName = annotation.scopeName();
        InvocationContext invocationContext = new InvocationContext(
                pjp.getSignature().getDeclaringType(),
                pjp.getSignature().getName(),
                pjp.getArgs()
        );
        if (!MemoizeBeanRegisterer.MEMOIZER_KNOWN_SCOPES.contains(scopeName)) {
            log.error("Scope is not known to Memoizer {}", scopeName);
        }
        InvocationCache scopedCache = (InvocationCache) applicationContext.getBean(MemoizeBeanRegisterer.getBeanNameForScope(scopeName));
        Object result;
        if (!scopedCache.contains(invocationContext)) {
            result = pjp.proceed();
            log.debug("Memoizing result {}, for method invocation: {}", result, invocationContext);
            scopedCache.put(invocationContext, result);
        } else {
            result = scopedCache.get(invocationContext);
            log.debug("Using memoized result: {}, for method invocation: {}", result, invocationContext);
        }
        return result;
    }
}
