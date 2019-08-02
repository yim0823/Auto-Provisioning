package com.bespinglobal.dcos.ap.config;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.config.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.config.HttpClientConfig
 * Version : 2019.07.31 v0.1
 * Created by taehyoung.yim on 2019-07-31.
 * *** 저작권 주의 ***
 *
 *  * Supports both HTTP and HTTPS.
 *  * Uses a connection pool to re-use connections and save overhead of creating connections.
 *  * Has a custom connection keep-alive strategy (to apply a default keep-alive if one isn't specified).
 *  * Starts an idle connection monitor to continuously clean up stale connections.
 */
@Configuration
@EnableScheduling
public class HttpClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientConfig.class);

    // Determines the timeout in milliseconds until a connection is established.
    @Value("${spring.httpclient.connect-timeout}")
    private int CONNECT_TIMEOUT;

    // The timeout when requesting a connection from the connection manager.
    @Value("${spring.httpclient.request-timeout}")
    private int REQUEST_TIMEOUT;

    // The timeout for waiting for data
    @Value("${spring.httpclient.socket-timeout}")
    private int SOCKET_TIMEOUT;

    @Value("${spring.httpclient.maximum-connections}")
    private int MAX_TOTAL_CONNECTIONS;

    @Value("${spring.httpclient.keep-alive-time}")
    private int DEFAULT_KEEP_ALIVE_TIME_MILLIS;

    @Value("${spring.httpclient.close-idle-time-sec}")
    private int CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS;

    /**
     * For Sync httpclient
     * @return
     */
    @Bean
    public PoolingHttpClientConnectionManager poolingConnectionManager() {

        SSLContextBuilder builder = new SSLContextBuilder();
        try {
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            logger.error("Pooling Connection Manager Initialisation failure because of " + e.getMessage(), e);
        }

        SSLConnectionSocketFactory sslConnectionSocketFactory = null;
        try {
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            logger.error("Pooling Connection Manager Initialisation failure because of " + e.getMessage(), e);
        }

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("https", sslConnectionSocketFactory)
                .register("http", new PlainConnectionSocketFactory())
                .build();

        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
                new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingHttpClientConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);

        return poolingHttpClientConnectionManager;
    }

    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {

        return new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                HeaderElementIterator iterator =
                        new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (iterator.hasNext()) {
                    HeaderElement element = iterator.nextElement();
                    String param = element.getName();
                    String value = element.getValue();

                    if (value != null && param.equalsIgnoreCase("timeout"))
                        return Long.parseLong(value) * 1000;
                }

                return DEFAULT_KEEP_ALIVE_TIME_MILLIS;
            }
        };
    }

    @Bean
    public CloseableHttpClient httpClient() {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingConnectionManager())
                .setKeepAliveStrategy(connectionKeepAliveStrategy())
                .build();
    }

    @Bean
    public Runnable idleConnectionMonitor(final PoolingHttpClientConnectionManager connectionManager) {

        return new Runnable() {
            @Override
            @Scheduled(fixedDelay = 10000)
            public void run() {

                try {
                    if (connectionManager != null) {
                        logger.trace("run IdleConnectionMonitor - Closing expired and idle connections...");
                        connectionManager.closeExpiredConnections();
                        connectionManager.closeIdleConnections(CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS, TimeUnit.SECONDS);
                    } else {
                        logger.trace("run IdleConnectionMonitor - Http Client Connection manager is not initialised");
                    }
                } catch (Exception e) {
                    logger.error("run IdleConnectionMonitor - Exception occurred. msg={}, e={}", e.getMessage(), e);
                }
            }
        };
    }

    /**
     * For async httpclient
     * @return
     */
    @Bean
    public PoolingNHttpClientConnectionManager poolingNHttpClientConnectionManager() {

        PoolingNHttpClientConnectionManager connectionManager = null;
        try {
            connectionManager = new PoolingNHttpClientConnectionManager(
                    new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT));
            connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        } catch (IOReactorException e) {
            logger.error("Pooling Connection Manager Initialisation failure because of " + e.getMessage(), e);
        }

        return connectionManager;
    }

    @Bean
    public CloseableHttpAsyncClient asyncHttpClient() {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        return HttpAsyncClientBuilder.create()
                .setConnectionManager(poolingNHttpClientConnectionManager())
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

}
