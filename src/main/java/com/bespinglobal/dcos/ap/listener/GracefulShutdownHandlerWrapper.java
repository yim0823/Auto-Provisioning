package com.bespinglobal.dcos.ap.listener;

import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.GracefulShutdownHandler;
import org.springframework.stereotype.Component;

/**
 * Project : Auto-Provisioning
 * Class : GracefulShutdownHandlerWrapper
 * Version : 2019.07.22 v0.1
 * Created by taehyoung.yim on 2019-07-22.
 * *** 저작권 주의 ***
 */
@Component
public class GracefulShutdownHandlerWrapper implements HandlerWrapper {

    private GracefulShutdownHandler gracefulShutdownHandler;

    @Override
    public HttpHandler wrap(final HttpHandler handler) {

        if (gracefulShutdownHandler == null) {
            synchronized (this) {
                if (gracefulShutdownHandler == null) {
                    this.gracefulShutdownHandler = new GracefulShutdownHandler(handler);
                }
            }
        }

        return gracefulShutdownHandler;
    }

    GracefulShutdownHandler getGracefulShutdownHandler() {
        return gracefulShutdownHandler;
    }
}
