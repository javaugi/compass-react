/*
 * Copyright (C) 2016 Center for Information Management, Inc.
 *
 * This program is proprietary.
 * Redistribution without permission is strictly prohibited.
 * For more information, contact <http://www.ciminc.com>
 */
package com.ciminc.event;

/**
 * simple abstraction for an event bus.
 *
 * @author ben
 * @version $LastChangedRevision $LastChangedDate Last Modified Author:
 * $LastChangedBy
 */
public interface IEventBus {

    public abstract void post(final Object event);

    public abstract void postAsync(final Object object);

    public abstract void register(final Object object);

    public abstract void unregister(final Object object);
}
