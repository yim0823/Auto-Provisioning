package com.bespinglobal.dcos.ap.config;

import com.bespinglobal.dcos.ap.api.dto.PersonDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    private static final int DEFAULT_SLEEP_MILLIS = 20;
    private static final int DEFAULT_TIMEOUT = 10000;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 아래 RestTemplate 를 이용하는 모든 Test-Units 은 connection pool 로 자동할당되어 관리 된다.
     * 관련 config 는 RestTemplateConfig, HttpClientConfig 이다.
     */

    @Test
    public void a1_sync_get_persons_to_ResponseEntity_String() {

        // given
        String url = "http://localhost:" + randomServerPort  + "/rest/persons";

        // when
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class);

        // then
        assertThat(result.getStatusCodeValue(), is(200));
        assertThat(Objects.requireNonNull(result.getBody()).contains("persons"), is(true));
    }

    @Test
    public void a2_sync_get_persons_to_ResponseEntity_String_translation_to_json() throws IOException {

        // given
        String url = "http://localhost:" + randomServerPort  + "/rest/persons";

        // when
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        List<PersonDto.Create> personDtoList = objectMapper.readValue(
                JsonPath.read(response.getBody(), "$.data.persons[*]").toString(),
                new TypeReference<List<PersonDto.Create>>() {});

        // then
        assertThat(personDtoList.size(), is(6));
    }

    @Test
    public void a3_sync_get_person_with_params() {

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
        assertThat(result.getStatusCodeValue(), is(200));
    }

    @Test
    public void a4_sync_post_person() throws IOException {

        // given
        String url = "http://localhost:" + randomServerPort + "/rest/persons";

        PersonDto.Create create = new PersonDto.Create();
        create.setFirstName("ducklee");
        create.setLastName("Choi");
        create.setGender("Female");
        create.setHobby("swim");
        create.setBirthDate(LocalDate.of(1954, 01, 15));

        HttpEntity<PersonDto.Create> request = new HttpEntity<>(create);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

//        // when
//        ResponseEntity<String> result = restTemplate.postForEntity(url, create, String.class);
//        System.out.println("################ " + result.getBody());
//--------------------

//        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
//        requestBody.add("firstName", "ducklee");
//        requestBody.add("lastName", "Choi");
//        requestBody.add("gender", "Female");
//        requestBody.add("birthDate", LocalDate.of(1954, 01, 15));
//        requestBody.add("hobby", "swim");
//
//        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//        headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
//        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//
//        HttpEntity<MultiValueMap<String, Object>> httpEntity =
//                new HttpEntity<MultiValueMap<String, Object>>(requestBody, headers);

//        // when
//        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
//        System.out.println("################ " + result.getBody());

//        ResponseEntity<String> response =
//                restTemplate.postForEntity(url, create, String.class);
//
//        PersonDto.Create personDto = objectMapper.readValue(
//                JsonPath.read(response.getBody(), "$.data").toString(),
//                PersonDto.Create.class);
//
//        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
//        assertThat(personDto, notNullValue());
//        assertThat(personDto.getFirstName(), is("ducklee"));
    }

    @Test(timeout = DEFAULT_TIMEOUT)
    public void a20_async_get_user_to_ResponseEntity_String() throws InterruptedException {

        // given
        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();

        String URL = "https://api.github.com/users/octocat";

        // when
        ListenableFuture<ResponseEntity<String>> listenableFuture = asyncRestTemplate.getForEntity(URL, String.class);
        listenableFuture.addCallback(result ->
                System.out.println("result = " + result),
                Throwable::printStackTrace);

        while (!listenableFuture.isDone()) {
            Thread.sleep(DEFAULT_SLEEP_MILLIS);
        }
    }

}