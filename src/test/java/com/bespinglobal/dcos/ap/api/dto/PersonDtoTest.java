package com.bespinglobal.dcos.ap.api.dto;

import com.bespinglobal.dcos.ap.api.domain.Person;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

/**
 * Project : Auto-Provisioning
 * Class : PersonDtoTest
 * Version :
 * Created by taehyoung.yim on 2019-07-16.
 * *** 저작권 주의 ***
 */
public class PersonDtoTest {

    @Test
    public void created_toEntity() {

        // given
        PersonDto.Create create = new PersonDto.Create();
        create.setFirstName("taehyoung");
        create.setLastName("Yim");
        create.setGender("Male");
        create.setBirthDate(LocalDate.of(1983, 8, 23));

        // when
        Person person = create.toEntity();

        // then
        assertThat(person.getFirstName(), is("taehyoung"));
        assertThat(person.getLastName(), is("Yim"));
        assertThat(person.getGender(), is("Male"));
        assertThat(person.getBirthDate(), is(LocalDate.of(1983, 8, 23)));
    }

    @Test
    public void update_apply() {

        // given
        Person person = Person.builder()
                .firstName("taehyoung")
                .lastName("Yim")
                .gender("Male")
                .birthDate(LocalDate.of(1983, 8, 23))
                .build();

        // when
        PersonDto.Update update = new PersonDto.Update();
        update.setFirstName("kyounghee");
        update.setLastName("Kim");
        update.setBirthDate(LocalDate.of(1983, 8,31));

        update.apply(person);

        // then
        assertThat(person.getFirstName(), is("kyounghee"));
        assertThat(person.getLastName(), is("Kim"));
        assertThat(person.getBirthDate(), is(LocalDate.of(1983, 8,31)));
    }

    @Test
    public void response_of() {

        // given
        Person person = Person.builder()
                .firstName("taehyoung")
                .lastName("Yim")
                .gender("Male")
                .birthDate(LocalDate.of(1983, 8, 23))
                .build();

        // when
        PersonDto.Response response = PersonDto.Response.of(person);

        // then
        assertThat(response.getAge(), is(35L));
        assertThat(response.getFirstName(), is("taehyoung"));
        assertThat(response.getLastName(), is("Yim"));
        assertThat(response.getGender(), is("Male"));
        assertThat(response.getBirthDate(), is(LocalDate.of(1983, 8, 23)));
    }
}