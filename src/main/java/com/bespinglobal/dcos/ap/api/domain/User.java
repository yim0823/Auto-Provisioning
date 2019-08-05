package com.bespinglobal.dcos.ap.api.domain;

import com.bespinglobal.dcos.ap.api.dto.UserDto;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.api.domain.User
 * Version : 2019.08.05 v0.1
 * Created by taehyoung.yim on 2019-08-05.
 * *** 저작권 주의 ***
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "user")
@Audited(withModifiedFlag = true)
public class User {

    private static final long serialVersionUID = 7691972589111054594L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "user_name", length = 30)
    private String userName;

    @Column(name = "email", length = 30, nullable = false)
    private String email;

    private Address address;

    private Geo geo;

    @Column(name = "phone", length = 30, nullable = false)
    private String phone;

    @Column(name = "web_site", length = 30)
    private String webSite;

    private Company company;

    @Builder
    public User(
            Long userId,
            String name,
            String userName,
            String email,
            Address address,
            Geo geo,
            String phone,
            String webSite,
            Company company) {
        this.userId = userId;
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.address = address;
        this.geo = geo;
        this.phone = phone;
        this.webSite = webSite;
        this.company = company;
    }

    public void update(UserDto.Update dto) {
        this.address = dto.getAddress();
    }
}
