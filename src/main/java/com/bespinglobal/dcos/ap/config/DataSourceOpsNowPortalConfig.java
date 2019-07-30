package com.bespinglobal.dcos.ap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Project : Information-Collector
 * Class : com.bespinglobal.dcos.ap.config.DataSourceOpsNowPortalConfig
 * Version : 2019.07.24 v0.1
 * Created by taehyoung.yim on 2019-07-24.
 * *** 저작권 주의 ***
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "opsNowPortalEntityManager",
        transactionManagerRef = "opsNowPortalTransactionManager",
        basePackages = {
                "com.bespinglobal.dcos.ap.api.repositories.account.repository"
        }
)
public class DataSourceOpsNowPortalConfig {

        private Environment env;

        @Autowired
        public DataSourceOpsNowPortalConfig(Environment env) {
                this.env = env;
        }

        @Bean(name = "opsNowPortalDataSource")
        public DataSource opsNowPortalDataSource() {

                DriverManagerDataSource dataSource = new DriverManagerDataSource();
                dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.opsnow.portal.driver-class-name")));
                dataSource.setUrl(env.getProperty("spring.datasource.opsnow.portal.url"));
                dataSource.setUsername(env.getProperty("spring.datasource.opsnow.portal.username"));
                dataSource.setPassword(env.getProperty("spring.datasource.opsnow.portal.password"));

                return dataSource;
        }

        @Bean(name = "opsNowPortalEntityManager")
        public LocalContainerEntityManagerFactoryBean opsNowPortalEntityManagerFactory(
                EntityManagerFactoryBuilder builder,
                @Qualifier("opsNowPortalDataSource") DataSource dataSource) {

                Map<String, Object> properties = new HashMap<>();
                properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
                properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
                properties.put("hibernate.ejb.naming_strategy", env.getProperty("spring.jpa.hibernate.naming.physical-strategy"));

                return builder.dataSource(dataSource)
                        .properties(properties)
                        .packages("com.bespinglobal.dcos.ap.api.repositories.account.domain")
                        .build();
        }

        @Bean(name = "opsNowPortalTransactionManager")
        public PlatformTransactionManager opsNowPortalTransactionManager(
                @Qualifier("opsNowPortalEntityManager") EntityManagerFactory entityManagerFactory) {
                return new JpaTransactionManager(entityManagerFactory);
        }
}
