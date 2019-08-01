package com.bespinglobal.dcos.ap.api.component;

import com.bespinglobal.dcos.ap.api.dto.PersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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

    private static RestTemplate restTemplate;

    @Autowired
    public RestTemplateComponent(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static PersonDto.ResponseOne responseForObject(String url) {
        return restTemplate.getForObject(url, PersonDto.ResponseOne.class);
    }

    public static ResponseEntity<PersonDto.ResponseOne> response(String url) {

        Map<String, Long> params = new HashMap<>();
        params.put("id", 1L);

        return restTemplate.exchange(
                "http://localhost:8080/rest/persons?id={id}",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                PersonDto.ResponseOne.class,
                params
        );
    }
}
