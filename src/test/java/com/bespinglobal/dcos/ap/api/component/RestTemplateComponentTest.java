package com.bespinglobal.dcos.ap.api.component;

import com.bespinglobal.dcos.ap.api.dto.PersonDto;
import com.bespinglobal.dcos.ap.api.dto.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.api.component.RestTemplateComponentTest
 * Version :
 * Created by taehyoung.yim on 2019-08-05.
 * *** 저작권 주의 ***
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
public class RestTemplateComponentTest {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateComponentTest.class);

    private static final String baseUrl = "http://jsonplaceholder.typicode.com";

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private RestTemplateComponent restTemplateComponent;

    @Test
    public void a1_get_users() {

        // given
        String url = baseUrl + "/users";

        // when
        List<UserDto.Create> result = restTemplateComponent.getForList(UserDto.Create.class, url, null);

        // then
        result.forEach(s -> logger.info("{}, {}", s.getName(), s.getAddress().getCity()));

        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(10));
    }

    @Test
    public void a2_get_user() {

        // given
        String url = baseUrl + "/users/{id}";
        Long id = 1L;

        // when
        UserDto.Create result = restTemplateComponent.getForEntity(UserDto.Create.class, url, null, id);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getUserId(), is(1L));
        assertThat(result.getName(), is("Leanne Graham"));
        assertThat(result.getCompany().getCompanyName(), is("Romaguera-Crona"));
        assertThat(result.getAddress().getCity(), is("Gwenborough"));
    }

    /**
     * REST API request 에 Header 가 필요한 경우, exchange 로 한다.
     */

    @Test
    public void a3_get_user_with_exchange() {

        // given
        String url = baseUrl + "/users/{id}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> params = new HashMap<>();
        params.put("id", 1L);

        // when
        UserDto.Create result = restTemplateComponent.getExchangeForObject(
                UserDto.Create.class, url, headers, params, null);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getUserId(), is(1L));
        assertThat(result.getName(), is("Leanne Graham"));
        assertThat(result.getUserName(), is("Bret"));
        assertThat(result.getCompany().getCompanyName(), is("Romaguera-Crona"));
    }

    @Test
    public void a4_get_users_with_exchange() {

        // given
        String url = baseUrl + "/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // when
        List<UserDto.Create> result = restTemplateComponent.getExchangeForList(
                UserDto.Create.class, url, headers, null);

        // then
        result.forEach(s -> logger.info("{}, {}, {}", s.getUserId(), s.getName(), s.getAddress().getCity()));

        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(10));
    }

    @Test
    public void a5_save_user() {

        // given
        String url = "http://localhost:" + randomServerPort + "/rest/persons";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        PersonDto.Create create = new PersonDto.Create();
        create.setFirstName("ducklee");
        create.setLastName("Choi");
        create.setGender("Female");
        create.setHobby("swim");
        create.setBirthDate(LocalDate.of(1954, 1, 15));

        // when
        PersonDto.Create result = restTemplateComponent.postForEntity(
                PersonDto.Create.class, url, headers, create, "$.data.person");

        // then
        logger.info("{}, {}, {}", result.getFirstName(), result.getBirthDate(), result.getGender());

        assertThat(result, is(notNullValue()));
        assertThat(result.getFirstName(), is(notNullValue()));
        assertThat(result.getGender(), is(notNullValue()));
    }
}