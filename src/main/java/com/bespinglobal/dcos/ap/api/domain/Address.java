package com.bespinglobal.dcos.ap.api.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.api.domain.Address
 * Version : 2019.08.05 v0.1
 * Created by taehyoung.yim on 2019-08-05.
 * *** 저작권 주의 ***
 */
@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(name = "street", length = 30, nullable = false)
    private String street;

    @Column(name = "suite", length = 30, nullable = false)
    private String suite;

    @Column(name = "city", length = 30, nullable = false)
    private String city;

    @Column(name = "zip_code", length = 30)
    private String zipCode;

    @Builder
    public Address(String street, String suite, String city, String zipCode) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipCode = zipCode;
    }
}
