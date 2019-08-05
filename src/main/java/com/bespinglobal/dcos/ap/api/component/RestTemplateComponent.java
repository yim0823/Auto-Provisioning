package com.bespinglobal.dcos.ap.api.component;

import com.bespinglobal.dcos.ap.api.exception.ApplicationException;
import com.bespinglobal.dcos.ap.utils.RtCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.api.component.RestTemplateComponent
 * Version : 2019.07.31 v0.1
 * Created by taehyoung.yim on 2019-07-31.
 * *** 저작권 주의 ***
 */
@Component
public class RestTemplateComponent {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateComponent.class);

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    @Autowired
    public RestTemplateComponent(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * GET one object
     * @param clazz 리턴 받고자 하는 클래스
     * @param url 요청 URL
     * @param uriVariables 요청에 들어갈 파라미터들
     * @return T
     */
    public <T> T getForEntity(Class<T> clazz, String url, String jsonPath, Object... uriVariables) {

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, uriVariables);
            JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);

            return readValue(response, javaType, jsonPath);
        } catch (HttpClientErrorException e) {
            handleException(e, url);
        }

        return null;
    }

    public <T> List<T> getForList(Class<T> clazz, String url, String jsonPath, Object... uriVariables) {

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, uriVariables);
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);

            return readValue(response, collectionType, jsonPath);
        } catch (HttpClientErrorException e) {
            handleException(e, url);
        }

        return null;
    }

    /**
     * Header 가 필요한 GET one object
     * @param clazz 리턴 받고자 하는 클래스
     * @param url 요청 URL
     * @param headers REST API 의 Header
     * @param params 요청에 들어갈 파라미터들
     * @return T
     */
    public <T> T getExchangeForObject(
            Class<T> clazz, String url, HttpHeaders headers, Map<String, Object> params, String jsonPath) {

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), String.class, params);
            JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);

            return readValue(response, javaType, jsonPath);
        } catch (HttpClientErrorException e) {
            handleException(e, url);
        }

        return null;
    }

    /**
     * Header 가 필요한 GET objects
     * @param clazz 리턴 받고자 하는 클래스
     * @param url 요청 URL
     * @param headers REST API 의 Header
     * @return T
     */
    public <T> List<T> getExchangeForList(Class<T> clazz, String url, HttpHeaders headers, String jsonPath) {

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);

            return readValue(response, collectionType, jsonPath);
        } catch (HttpClientErrorException e) {
            handleException(e, url);
        }

        return null;
    }

    public <T, R> T postForEntity(
            Class<T> clazz, String url, HttpHeaders headers, R body, String jsonPath, Object... uriVariables) {

        try {
            HttpEntity<R> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class, uriVariables);
            JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);

            return readValue(response, javaType, jsonPath);
        } catch (HttpClientErrorException e) {
            handleException(e, url);
        }

        return null;
    }

    public <T, R> T putForEntity(
            Class<T> clazz, String url, HttpHeaders headers, R body, String jsonPath, Object... uriVariables) {

        try {
            HttpEntity<R> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class, uriVariables);
            JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);

            return readValue(response, javaType, jsonPath);
        } catch (HttpClientErrorException e) {
            handleException(e, url);
        }

        return null;
    }

    public void delete(String url, Object... uriVariables) {

        try {
            restTemplate.delete(url, uriVariables);
        } catch (HttpClientErrorException e) {
            handleException(e, url);
        }
    }

    private <T> T readValue(ResponseEntity<String> response, JavaType javaType, String jsonPath) {

        T result = null;
        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {

            try {
                if (jsonPath == null) {
                    result = objectMapper.readValue(response.getBody(), javaType);
                } else {
                    result = objectMapper.readValue(
                            objectMapper.writeValueAsString(JsonPath.read(response.getBody(), jsonPath)),
                            javaType);
                }
            } catch (IOException e) {
                logger.error("Failure when parsing the response of HttpClient because of {}", e.getMessage(), e);
            }
        } else {
            logger.info("No data found {}", response.getStatusCode());
        }

        return result;
    }

    private void handleException(HttpClientErrorException e, String url) {

        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            logger.error("No data found {}", url);
            throw new ApplicationException(RtCode.RT_NOT_SUPPORT, "About " + url  + ", " + e.getMessage());
        } else {
            logger.error("Rest client exception {}", url);
            throw new ApplicationException(RtCode.RT_INTERNAL_ERROR, "About " + url  + ", " + e.getMessage());
        }
    }

}
