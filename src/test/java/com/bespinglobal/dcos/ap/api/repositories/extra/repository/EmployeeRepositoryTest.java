package com.bespinglobal.dcos.ap.api.repositories.extra.repository;

import com.bespinglobal.dcos.ap.api.dto.EmployeeDto;
import com.bespinglobal.dcos.ap.api.repositories.extra.domain.Employee;
import com.bespinglobal.dcos.ap.api.repositories.extra.repository.EmployeeRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Project : Auto-Provisioning
 * Class : EmployeeRepositoryTest
 * Version :
 * Created by taehyoung.yim on 2019-07-19.
 * *** 저작권 주의 ***
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Before
    public void setUp() {
        String id = UUID.randomUUID().toString();

        EmployeeDto.Create create = new EmployeeDto.Create();
        create.setId(id);
        create.setFirstName("taehyoung");
        create.setLastName("Yim");
        create.setEmail("taehyoung.yim@bespinglobal.com");

        Employee target = create.toEntity();
        employeeRepository.save(target);
    }

    @After
    public void endUp() {
        employeeRepository.deleteAll();
    }

    @Test
    public void a1_save_employee() {

        // given
        String id = UUID.randomUUID().toString();

        EmployeeDto.Create create = new EmployeeDto.Create();
        create.setId(id);
        create.setFirstName("kyounghee");
        create.setLastName("Kim");
        create.setEmail("kyounghee.kim@bespinglobal.com");

        Employee target = create.toEntity();

        // when
        Employee created = employeeRepository.save(target);

        // then
        EmployeeDto.Response response = EmployeeDto.Response.of(created);
        Assert.assertNotNull(response.getId());
        assertThat(response.getFirstName(), is("kyounghee"));
        assertThat(response.getLastName(), is("Kim"));
    }

    @Test
    public void a2_findByFirstName() {

        // given
        String fistName = "taehyoung";

        // when
        List<Employee> employeeList = employeeRepository.findByFirstName(fistName);

        // then
        assertThat(employeeList.size(), is(1));
        assertThat(employeeList.get(0).getFirstName(), is(fistName));
    }
}