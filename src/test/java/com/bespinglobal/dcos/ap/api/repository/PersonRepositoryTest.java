package com.bespinglobal.dcos.ap.api.repository;

import com.bespinglobal.dcos.ap.api.domain.Person;
import com.bespinglobal.dcos.ap.api.dto.PersonDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.api.repository.PersonRepositoryTest
 * Version :
 * Created by taehyoung.yim on 2019-07-31.
 * *** 저작권 주의 ***
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Before
    public void setUp() {

        PersonDto.Create create = new PersonDto.Create();
        create.setFirstName("taehyoung");
        create.setLastName("Yim");
        create.setGender("Male");
        create.setHobby("play soccer");
        create.setBirthDate(LocalDate.of(1983, 8, 23));

        Person target = create.toEntity();
        personRepository.save(target);
    }

    @After
    public void endUp() {
        personRepository.deleteAll();
    }

    @Test
    public void a1_save_person() {

        // given
        PersonDto.Create create = new PersonDto.Create();
        create.setFirstName("kyounghee");
        create.setLastName("Kim");
        create.setGender("Female");
        create.setHobby("read books");
        create.setBirthDate(LocalDate.of(1983, 8, 31));

        Person target = create.toEntity();

        // when
        Person created = personRepository.save(target);

        // then
        PersonDto.Response response = PersonDto.Response.of(created);
        assertThat(response.getId(), notNullValue());
        assertThat(response.getFirstName(), is("kyounghee"));
        assertThat(response.getLastName(), is("Kim"));
    }

    @Test
    public void a2_find_by_FirstName() {

        // given
        String firstName = "taehyoung";

        // when
        List<Person> personList = personRepository.findByFirstName(firstName);

        // then
        assertThat(personList.size(), is(1));
        assertThat(personList.get(0).getFirstName(), is(firstName));
    }

}