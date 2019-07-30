package com.bespinglobal.dcos.ap.api.repositories.basic.domain;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

/**
 * Project : Information-Collector
 * Class : Person
 * Version : 2019.07.16 v0.1
 * Created by taehyoung.yim on 2019-07-16.
 * *** 저작권 주의 ***
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "person")
@Audited(withModifiedFlag = true)
public class Person extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private LocalDate birthDate;

    private String hobby;

    @Builder
    private Person(String firstName, String lastName, String gender, LocalDate birthDate, String hobby) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.hobby = hobby;
    }

    public Person update(String firstName, String lastName, LocalDate birthDate, String hobby) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.hobby = hobby;
        return this;
    }
}
