package com.bespinglobal.dcos.ap.api.dto;

import com.bespinglobal.dcos.ap.api.repositories.extra.domain.Employee;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Project : Information-Collector
 * Class : EmployeeDto
 * Version : 2019.07.17 v0.1
 * Created by taehyoung.yim on 2019-07-17.
 * *** 저작권 주의 ***
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmployeeDto {

    @Setter
    @Getter
    public static class Create {

        @NotEmpty
        private String id;
        @NotEmpty
        private String firstName;
        @NotEmpty
        private String lastName;
        @NotEmpty
        private String email;

        public Employee toEntity() {
            return Employee.builder()
                    .id(id)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .build();
        }
    }

    @Setter
    @Getter
    public static class Update {

        @NotEmpty
        private String firstName;
        @NotEmpty
        private String lastName;
        @NotEmpty
        private String email;

        public Employee apply(Employee employee) {
            return employee.update(firstName, lastName, email);
        }
    }

    @Getter
    public static class Response {

        private String id;
        private String firstName;
        private String lastName;
        private String email;

        @Builder
        public Response(String id, String firstName, String lastName, String email) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        public static Response of(Employee employee) {
            return Response.builder()
                    .id(employee.getId())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .email(employee.getEmail())
                    .build();
        }
    }

    @Getter
    public static class ResponseOne {

        private Response employee;

        public ResponseOne(Response employee) { this.employee = employee; }
    }

    @Getter
    public static class ResponseList {

        private List<Response> employees;

        public ResponseList(List<Response> employees) { this.employees = employees; }
    }
}
