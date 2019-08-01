package com.bespinglobal.dcos.ap.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.config.JpaConfig
 * Version : 2019.07.31 v0.1
 * Created by taehyoung.yim on 2019-07-31.
 * *** 저작권 주의 ***
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.bespinglobal.dcos.ap.api.repository",
        repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
public class JpaConfig {
}
