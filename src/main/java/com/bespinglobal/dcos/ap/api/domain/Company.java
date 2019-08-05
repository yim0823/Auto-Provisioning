package com.bespinglobal.dcos.ap.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.api.domain.Company
 * Version : 2019.08.05 v0.1
 * Created by taehyoung.yim on 2019-08-05.
 * *** 저작권 주의 ***
 */
@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {

    @JsonProperty("name")
    @Column(name = "company_name", length = 30)
    private String companyName;

    @JsonProperty("catchPhrase")
    @Column(name = "company_catch_phrase", length = 30)
    private String companyCatchPhrase;

    @JsonProperty("bs")
    @Column(name = "company_bs", length = 30)
    private String companyBs;

    @Builder
    public Company(String companyName, String companyCatchPhrase, String companyBs) {
        this.companyName = companyName;
        this.companyCatchPhrase = companyCatchPhrase;
        this.companyBs = companyBs;
    }
}
