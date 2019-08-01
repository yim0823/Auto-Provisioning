package com.bespinglobal.dcos.ap.config;

import com.bespinglobal.dcos.ap.api.domain.Person;
import com.bespinglobal.dcos.ap.api.dto.PersonDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.config.RestTemplateConfigTest
 * Version :
 * Created by taehyoung.yim on 2019-07-31.
 * *** 저작권 주의 ***
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestTemplateConfigTest {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateConfigTest.class);

    @Autowired
    private RestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void a1_get_persons_to_ResponseEntity_String() {

        // given
        String url = "http://localhost:" + randomServerPort  + "/rest/persons";

        // when
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);

        // then
        assertThat(result.getStatusCodeValue(), is(200));
        assertThat(result.getBody().contains("persons"), is(true));
    }

    @Test
    public void a2_get_persons_to_ResponseEntity_String_translation_to_json() throws IOException {

        // given
        String url = "http://localhost:" + randomServerPort  + "/rest/persons";

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        List<PersonDto.Create> personDtoList = objectMapper.readValue(
                JsonPath.read(response.getBody(), "$.data.persons[*]").toString(),
                new TypeReference<List<PersonDto.Create>>() {});

        personDtoList.stream().forEach(s ->
                System.out.println(s.getId() + ", " + s.getFirstName() + ", " + s.getBirthDate() + ", " + s.getAge()));

        // then
        assertThat(personDtoList.size(), is(6));
    }

    @Test
    public void a3_get_person() {

        // given
        String url = "http://localhost:" + randomServerPort + "/rest/persons?id={id}";

        Map<String, Long> params = new HashMap<>();
        params.put("id", 1L);

        // when
        ResponseEntity<PersonDto> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                PersonDto.class,
                params
        );

        // then
        System.out.println("===================== ");
        System.out.println("== " + result.getStatusCode());
        System.out.println("== " + result.getBody());
        System.out.println("===================== ");
    }
}