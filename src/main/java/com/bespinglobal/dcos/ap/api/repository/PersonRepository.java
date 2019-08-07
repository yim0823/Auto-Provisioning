package com.bespinglobal.dcos.ap.api.repository;

import com.bespinglobal.dcos.ap.api.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Project : Auto-Provisioning
 * Class : PersonRepository
 * Version : 2019.07.16 v0.1
 * Created by taehyoung.yim on 2019-07-16.
 * *** 저작권 주의 ***
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByFirstName(String firstName);
}
