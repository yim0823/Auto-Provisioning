package com.bespinglobal.dcos.ap.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * Project : Information-Collector
 * Class : GracefulShutdownListener
 * Version : 2019.07.22 v0.1
 * Created by taehyoung.yim on 2019-07-22.
 * *** 저작권 주의 ***
 */

/**
 * ContextClosedEvent 는 Spring application context 가 종료될 때 발생하는 이벤트이다.
 *  - 구동 중인 application 에 kill 명령이 전달되면 발생하는 이벤트가 여기에 속한다.
 * Undertow 에서 제공하는 GracefulShutdownHandler 를 이용하여,
 *  - Application Context 제거 전에 처리 중인 요청은 처리하고 이후 요청은 거부하게 구현했다.
 *  - ContextClosedEvent 가 발생하면 해당 method 는 별도의 thread 로 실행된다.
 */
@Component
public class GracefulShutdownListener implements ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(GracefulShutdownListener.class);

    private GracefulShutdownHandlerWrapper gracefulShutdownHandlerWrapper;

    @Autowired
    public GracefulShutdownListener(GracefulShutdownHandlerWrapper gracefulShutdownHandlerWrapper) {
        this.gracefulShutdownHandlerWrapper = gracefulShutdownHandlerWrapper;
    }

    @Override
    public void onApplicationEvent(final ContextClosedEvent event) {

        logger.info("## [GRACEFUL_SHUTDOWN] Starting shutdown process ##");

        // 이 시점부터 새로운 요청에 대해 거부를 한다. Client 는 503 Service Unavailable 응답을 수신한다.
        gracefulShutdownHandlerWrapper.getGracefulShutdownHandler().shutdown();

        try {
            logger.warn("## [GRACEFUL_SHUTDOWN] Waiting termination of threads.. ##");

            // 이미 요청을 받아 처리 중인 요청에 대한 응답을 완료한다.
            gracefulShutdownHandlerWrapper.getGracefulShutdownHandler().awaitShutdown();
        } catch (InterruptedException e) {
            logger.error("## [GRACEFUL_SHUTDOWN] Undertow termination error ##", e);
            Thread.currentThread().interrupt();;
        }

        logger.info("## [GRACEFUL_SHUTDOWN] Shutdown process is completed successfully ##");
    }


}
