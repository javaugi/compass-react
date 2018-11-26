/*
 * Copyright (C) 2016 Center for Information Management, Inc.
 *
 * This program is proprietary.
 * Redistribution without permission is strictly prohibited.
 * For more information, contact <http://www.ciminc.com>
 */
package com.ciminc.event;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.error.IPublicationErrorHandler;
import net.engio.mbassy.bus.error.PublicationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Event bus that utilizes mbassador
 *
 * @author ben
 * @version $LastChangedRevision $LastChangedDate Last Modified Author:
 * $LastChangedBy
 */
public class MBassadorEventBus implements IEventBus {

    MBassador eventBus;
    private static final Logger log = LoggerFactory.getLogger(MBassadorEventBus.class);

    public MBassadorEventBus() {
        this.eventBus = new MBassador(new IPublicationErrorHandler() {
            @Override
            public void handleError(final PublicationError error) {
                log.error(error.toString(), error.getCause());
            }
        });
    }

    @Override
    public void post(Object event) {
        eventBus.publish(event);
    }

    @Override
    public void postAsync(Object object) {
        eventBus.publishAsync(object);
    }

    @Override
    public void register(Object object) {
        eventBus.subscribe(object);
    }

    @Override
    public void unregister(Object object) {
        eventBus.unsubscribe(object);
    }

}
