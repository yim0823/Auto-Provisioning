package com.bespinglobal.dcos.ap.api.repositories.account.repository;

import com.bespinglobal.dcos.ap.api.repositories.account.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Project : Information-Collector
 * Class : com.bespinglobal.dcos.ic.api.repositories.asset.repository.UserRepository
 * Version : 2019.07.22 v0.1
 * Created by taehyoung.yim on 2019-07-23.
 * *** 저작권 주의 ***
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
