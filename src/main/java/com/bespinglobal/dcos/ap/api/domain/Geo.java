package com.bespinglobal.dcos.ap.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.api.domain.Geo
 * Version : 2019.08.05 v0.1
 * Created by taehyoung.yim on 2019-08-05.
 * *** 저작권 주의 ***
 */
@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Geo {

    @JsonProperty("lat")
    @Column(name = "geo_lat", length = 30, insertable = false, updatable = false)
    private String lat;

    @JsonProperty("lng")
    @Column(name = "geo_lng", length = 30, insertable = false, updatable = false)
    private String lng;

    @Builder
    public Geo(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
