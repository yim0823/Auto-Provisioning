package com.bespinglobal.dcos.ap.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.config.RestTemplateConfig
 * Version : 2019.07.31 v0.1
 * Created by taehyoung.yim on 2019-07-31.
 * *** 저작권 주의 ***
 */
@Configuration
public class RestTemplateConfig {

    private CloseableHttpClient httpClient;
    private CloseableHttpAsyncClient asyncHttpClient;

    @Autowired
    public RestTemplateConfig(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);

        return clientHttpRequestFactory;
    }

    /*
    TODO. AsyncRestTemplate 에 connection pool 관리쪽은 현재 deprecated 됐고,
      이 로직 동작하지 않는다. 변경 필요, 현재(2019.08.02) 요구되는 기능에는 사용할 일이 없어
      여기까지만 구현했다. 필요시에는 보수가 필요하다.
     */
    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate(asyncHttpRequestFactory(), restTemplate());
    }

    @Bean
    public AsyncClientHttpRequestFactory asyncHttpRequestFactory() {
        return new HttpComponentsAsyncClientHttpRequestFactory(asyncHttpClient);
    }

    @Bean
    public TaskScheduler taskScheduler() {

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("poolScheduler");
        scheduler.setPoolSize(50);

        return scheduler;
    }
}
