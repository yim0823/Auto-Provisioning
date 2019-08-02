package com.bespinglobal.dcos.ap.api.dto;

import com.bespinglobal.dcos.ap.api.domain.Person;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Project : Auto-Provisioning
 * Class : PersonDto
 * Version : 2019.07.16 v0.1
 * Created by taehyoung.yim on 2019-07-16.
 * *** 저작권 주의 ***
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonDto {

    @Setter
    @Getter
    public static class Create {

        private Long id;
        @NotEmpty
        private String firstName;
        @NotEmpty
        private String lastName;
        @NotNull
        private String gender;
        @NotNull
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate birthDate;
        @JsonIgnore
        private Long age;
        private String hobby;

        public Person toEntity() {
            return Person.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .gender(gender)
                    .birthDate(birthDate)
                    .hobby(hobby)
                    .build();
        }
    }

    @Setter
    @Getter
    public static class Update {

        private Long id;
        @NotEmpty
        private String firstName;
        @NotEmpty
        private String lastName;
        @NotNull
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthDate;

        private String hobby;

        public Person apply(Person person) {
            return person.update(id, firstName, lastName, birthDate, hobby);
        }
    }

    @Getter
    public static class Response {

        private Long id;
        private String firstName;
        private String lastName;

        @JsonIgnore
        private long age;

        private String gender;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthDate;

        private String hobby;

        @Builder
        private Response(Long id, String firstName, String lastName, String gender, LocalDate birthDate, String hobby) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = ChronoUnit.YEARS.between(birthDate, LocalDate.now());
            this.gender = gender;
            this.birthDate = birthDate;
            this.hobby = hobby;
        }

        public static Response of(Person person) {
            return Response.builder()
                    .id(person.getId())
                    .firstName(person.getFirstName())
                    .lastName(person.getLastName())
                    .gender(person.getGender())
                    .birthDate(person.getBirthDate())
                    .hobby(person.getHobby())
                    .build();
        }
    }

    @Getter
    public static class ResponseOne {

        private Response person;

        public ResponseOne(Response person) {
            this.person = person;
        }
    }

    @Getter
    public static class ResponseList {

        private List<Response> persons;

        public ResponseList(List<Response> persons) {
            this.persons = persons;
        }
    }
}
