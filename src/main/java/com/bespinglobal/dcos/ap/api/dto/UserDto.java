package com.bespinglobal.dcos.ap.api.dto;

import com.bespinglobal.dcos.ap.api.domain.Address;
import com.bespinglobal.dcos.ap.api.domain.Company;
import com.bespinglobal.dcos.ap.api.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.api.dto.UserDto
 * Version : 2019.08.05 v0.1
 * Created by taehyoung.yim on 2019-08-05.
 * *** 저작권 주의 ***
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {

    /**
     * Rest API 호출에 대한 응답처리를 위한 테스트 Entity 이다. 응답 형식은 다음과 같다:
     * https://jsonplaceholder.typicode.com/users
     */

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Create {

        @JsonProperty("id")
        private Long userId;

        @NotEmpty
        private String name;

        @NotEmpty
        @JsonProperty("username")
        private String userName;

        private String email;
        private Address address;
        private String phone;

        @JsonProperty("website")
        private String webSite;

        private Company company;

        @Builder
        public Create(
                Long userId,
                String name,
                String userName,
                String email,
                Address address,
                String phone,
                String webSite,
                Company company) {
            this.userId = userId;
            this.name = name;
            this.userName = userName;
            this.email = email;
            this.address = address;
            this.phone = phone;
            this.webSite = webSite;
            this.company = company;
        }

        public User toEntity() {
            return User.builder()
                    .userId(userId)
                    .name(name)
                    .userName(userName)
                    .email(email)
                    .address(address)
                    .phone(phone)
                    .webSite(webSite)
                    .company(company)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Update {

        private Address address;

        @Builder
        public Update(final Address address) {
            this.address = address;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {

        private Long userId;
        private String name;
        private String userName;
        private String phone;
        private String webSite;
        private Company company;

        @Builder
        public Response(
                Long userId, String name, String userName, String phone, String webSite, Company company) {
            this.userId = userId;
            this.name = name;
            this.userName = userName;
            this.phone = phone;
            this.webSite = webSite;
            this.company = company;
        }

        public static Response of(User user) {
            return Response.builder()
                    .userId(user.getUserId())
                    .userName(user.getUserName())
                    .name(user.getName())
                    .phone(user.getPhone())
                    .webSite(user.getWebSite())
                    .company(user.getCompany())
                    .build();
        }
    }
}
