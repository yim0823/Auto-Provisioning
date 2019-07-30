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
 * Class : com.bespinglobal.dcos.ap.config.OpsNowAssetDataSourceConfig
 * Version : 2019.07.23 v0.1
 * Created by taehyoung.yim on 2019-07-23.
 * *** 저작권 주의 ***
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "opsNowAssetEntityManager",
        transactionManagerRef = "opsNowAssetTransactionManager",
        basePackages = "com.bespinglobal.dcos.ap.api.repositories.asset.repository"
)
public class DataSourceOpsNowAssetConfig {

    private Environment env;

    @Autowired
    public DataSourceOpsNowAssetConfig(Environment env) {
        this.env = env;
    }

    @Bean(name = "opsNowAssetDataSource")
    public DataSource opsNowAssetDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.opsnow.asset.driver-class-name")));
        dataSource.setUrl(env.getProperty("spring.datasource.opsnow.asset.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.opsnow.asset.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.opsnow.asset.password"));

        return dataSource;
    }

    @Bean(name = "opsNowAssetEntityManager")
    public LocalContainerEntityManagerFactoryBean opsNowAssetEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("opsNowAssetDataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.ejb.naming_strategy", env.getProperty("spring.jpa.hibernate.naming.physical-strategy"));

        return builder.dataSource(dataSource)
                .properties(properties)
                .packages("com.bespinglobal.dcos.ap.api.repositories.asset.domain")
                .build();
    }

    @Bean(name = "opsNowAssetTransactionManager")
    public PlatformTransactionManager opsNowAssetTransactionManager(
            @Qualifier("opsNowAssetEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
