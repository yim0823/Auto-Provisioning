package com.bespinglobal.dcos.ap.api.repositories.extra.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Project : Information-Collector
 * Class : Employee
 * Version : 2019.07.17 v0.1
 * Created by taehyoung.yim on 2019-07-17.
 * *** 저작권 주의 ***
 */
@Getter
@Table(name = "employee")
public class Employee {

    @PrimaryKey
    private String id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Builder
    private Employee(String id, String firstName, String lastName, String email) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Employee update(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        return this;
    }
}
