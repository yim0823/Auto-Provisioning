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
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(personDtoList.size(), is(notNullValue()));
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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        PersonDto.Create create = new PersonDto.Create();
        create.setFirstName("ducklee");
        create.setLastName("Choi");
        create.setGender("Female");
        create.setHobby("swim");
        create.setBirthDate(LocalDate.of(1954, 01, 15));

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(create), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // when
        PersonDto.Create personDto = objectMapper.readValue(
                objectMapper.writeValueAsString(JsonPath.read(response.getBody(), "$.data.person")),
                new TypeReference<PersonDto.Create>() {});

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(personDto, notNullValue());
        assertThat(personDto.getFirstName(), is("ducklee"));
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