package com.bespinglobal.dcos.ap.api.service;

import com.bespinglobal.dcos.ap.APApplication;
import com.bespinglobal.dcos.ap.api.dto.PersonDto;
import com.bespinglobal.dcos.ap.api.exception.ApplicationException;
import com.bespinglobal.dcos.ap.api.domain.Person;
import com.bespinglobal.dcos.ap.api.repository.PersonRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Project : Auto-Provisioning
 * Class : PersonServiceTest
 * Version :
 * Created by taehyoung.yim on 2019-07-16.
 * *** 저작권 주의 ***
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { APApplication.class })
public class PersonServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Before
    public void setUp() {
        personRepository.save(
                Person.builder()
                        .firstName("taehyoung")
                        .lastName("Yim")
                        .gender("Male")
                        .hobby("football")
                        .birthDate(LocalDate.of(1983, 8, 23))
                        .build()
        );
    }

//    @After
//    public void tearDown() throws Exception {
//        personRepository.deleteAll();
//    }

    @Test
    public void a1_findAll() {

        // given
        personRepository.save(
                Person.builder()
                        .firstName("kyounghee")
                        .lastName("Kim")
                        .gender("Female")
                        .birthDate(LocalDate.of(1983, 8, 31))
                        .build()
        );

        // when
        List<PersonDto.Response> responseList = personService.findAll();

        //then
        assertThat(responseList.size(), is(2));
    }

    @Test
    public void a2_findById() {

        // given
        Person person = personRepository.save(Person.builder()
                .firstName("lionel")
                .lastName("Yim")
                .gender("Male")
                .birthDate(LocalDate.of(2018, 11, 20))
                .build());

        // when
        PersonDto.Response response = personService.findById(person.getId());

        // then
        assertThat(response.getFirstName(), is("lionel"));
        assertThat(response.getLastName(), is("Yim"));
        assertThat(response.getGender(), is("Male"));
    }

    @Test
    public void a3_findById_not_found() {

        // expect
        thrown.expect(ApplicationException.class);
        thrown.expectMessage("[Not exist] There are no Person with the ID");

        // when
        personService.findById(0L);
    }

    @Test
    public void a4_add() {

        // given
        PersonDto.Create create = new PersonDto.Create();
        create.setBirthDate(LocalDate.of(1983, 8, 31));
        create.setGender("Female");
        create.setFirstName("kyounghee");
        create.setLastName("Kim");
        create.setHobby("watch the football game");

        // when
        PersonDto.Response response = personService.add(create);

        // then
        assertThat(response.getId(), is(notNullValue()));
    }

    @Test
    public void a5_delete() {

        // given
        Long id = personRepository.findAll().get(0).getId();

        // when
        personService.delete(id);

        // then
        assertThat(personRepository.count(), is(0L));
    }

    @Test
    public void a6_delete_not_found() {

        //expect
        thrown.expect(ApplicationException.class);
        thrown.expectMessage("[Not exist] There are no Person with the ID");

        //when
        personService.delete(0L);
    }

    @Test
    public void a7_update() {

        // given
        Long id = personRepository.findAll().get(0).getId();

        PersonDto.Update update = new PersonDto.Update();
        update.setBirthDate(LocalDate.of(2018, 11, 20));
        update.setFirstName("lionel");
        update.setLastName("Yim");

        // when
        personService.update(id, update);

        // then
        personRepository.findById(id)
                .ifPresent(person -> {
                    assertThat(person.getFirstName(), is("lionel"));
                    assertThat(person.getLastName(), is("Yim"));
                });
    }
}