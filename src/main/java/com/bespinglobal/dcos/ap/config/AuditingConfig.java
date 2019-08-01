package com.bespinglobal.dcos.ap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Project : Auto-Provisioning
 * Class : JpaAuditingConfig
 * Version : 2019.07.16 v0.1
 * Created by taehyoung.yim on 2019-07-16.
 * *** 저작권 주의 ***
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider") // JPA Auditing - Common Entity 의 생성시간, 수정시간 자동화
public class AuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {

        /**
         * TODO:
         *   Spring security 를 사용한다면 SecurityContextHolder 에서 Authentication 객체로부터 UserDto 정보를 가져올 수 있을 것이고
         *   Token 기반의 인증을 사용한다면 일반적으로 Token 을 파싱한 결과를 Thread local 에 담아둘 것이므로
         *   Thread local 로부터 사용자 정보를 가져오는 구현을 아래에서 구현하면 될 것이다.
         *   if you are using spring security, SecurityContextHolder.getContext().getAuthentication().getName()
         */
        return () -> Optional.ofNullable("it will be changed by JpaAuditingConfig");
    }
}
