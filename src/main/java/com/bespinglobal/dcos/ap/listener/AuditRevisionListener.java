package com.bespinglobal.dcos.ap.listener;

import com.bespinglobal.dcos.ap.api.repositories.basic.domain.AuditRevisionEntity;
import org.hibernate.envers.RevisionListener;

/**
 * Project : Information-Collector
 * Class : com.bespinglobal.dcos.ic.listener.AuditRevisionListener
 * Version : 2019.07.22 v0.1
 * Created by taehyoung.yim on 2019-07-22.
 * *** 저작권 주의 ***
 */
public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {

        AuditRevisionEntity audit = (AuditRevisionEntity) revisionEntity;
        /**
         * TODO:
         *   Spring security 를 사용한다면 SecurityContextHolder 에서 Authentication 객체로부터 UserDto 정보를 가져올 수 있을 것이고
         *   Token 기반의 인증을 사용한다면 일반적으로 Token 을 파싱한 결과를 Thread local 에 담아둘 것이므로
         *   Thread local 로부터 사용자 정보를 가져오는 구현을 아래에서 구현하면 될 것이다.
         *   if you are using spring security, SecurityContextHolder.getContext().getAuthentication().getName()
         */
        audit.setUserName("admin");
    }
}
