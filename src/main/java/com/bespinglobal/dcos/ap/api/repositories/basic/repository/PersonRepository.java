package com.bespinglobal.dcos.ap.api.repositories.basic.repository;

import com.bespinglobal.dcos.ap.api.repositories.basic.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Project : Information-Collector
 * Class : PersonRepository
 * Version : 2019.07.16 v0.1
 * Created by taehyoung.yim on 2019-07-16.
 * *** 저작권 주의 ***
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
