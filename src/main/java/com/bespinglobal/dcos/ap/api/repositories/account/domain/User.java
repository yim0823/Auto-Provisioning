package com.bespinglobal.dcos.ap.api.repositories.account.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Project : Information-Collector
 * Class : com.bespinglobal.dcos.ic.api.repositories.asset.domain.User
 * Version : 2019.07.23 v0.1
 * Created by taehyoung.yim on 2019-07-23.
 * *** 저작권 주의 ***
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String job;

    @Builder
    private User(String name, String job) {
        this.name = name;
        this.job = job;
    }
}
