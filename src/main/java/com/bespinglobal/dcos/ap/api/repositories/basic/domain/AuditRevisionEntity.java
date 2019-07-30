package com.bespinglobal.dcos.ap.api.repositories.basic.domain;

import com.bespinglobal.dcos.ap.listener.AuditRevisionListener;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.*;

/**
 * Project : Information-Collector
 * Class : com.bespinglobal.dcos.ic.api.repositories.basic.domain.AuditRevisionEntity
 * Version : 2019.07.22 v0.1
 * Created by taehyoung.yim on 2019-07-22.
 * *** 저작권 주의 ***
 */
@Entity
@Table(name = "revinfo", schema = "audit")
@RevisionEntity(AuditRevisionListener.class)
@AttributeOverrides({
        @AttributeOverride(name = "timestamp", column = @Column(name = "REVTSTMP")),
        @AttributeOverride(name = "id", column = @Column(name = "REV"))
})
@Getter
@Setter
public class AuditRevisionEntity extends DefaultRevisionEntity {

    @Column(name = "username", nullable = false)
    private String userName;

}
