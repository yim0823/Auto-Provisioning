package com.bespinglobal.dcos.ap.config;

import com.bespinglobal.dcos.ap.listener.GracefulShutdownHandlerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Project : Information-Collector
 * Class : GracefulShutdownConfig
 * Version : 2019.07.22 v0.1
 * Created by taehyoung.yim on 2019-07-22.
 * *** 저작권 주의 ***
 */

/**
 * HA 환경에서 Application Layer 에서 종료 시그널(kill -9와 같은) 발생했을 시,
 *  - 유입된 요청들은 모두 처리 후, 정상적인 종료가 이루어져야 한다.
 *  - 기존에는 try-catch 로 종료 시그널을 캐시해 처리 중인 요청까지 처리하고 종료하는 로직을 직접 구현했으나,
 *  - GracefulShutdownHandler 를 이용해 이를 가능케한다.
 * 로직,
 *  - 종료 시그널 발생 -> 처리 중인 요청들 모두 처리 -> 요청에 대한 정상적인 응답 보냄 -> App 종료
 */
@Configuration
public class GracefulShutdownConfig {

    private GracefulShutdownHandlerWrapper gracefulShutdownHandlerWrapper;

    @Autowired
    public GracefulShutdownConfig(GracefulShutdownHandlerWrapper gracefulShutdownHandlerWrapper) {
        this.gracefulShutdownHandlerWrapper = gracefulShutdownHandlerWrapper;
    }

    @Bean
    public UndertowServletWebServerFactory servletWebServerFactory() {

        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
        factory.addDeploymentInfoCustomizers(
                deploymentInfo -> deploymentInfo.addOuterHandlerChainWrapper(gracefulShutdownHandlerWrapper)
        );

        return factory;
    }
}
