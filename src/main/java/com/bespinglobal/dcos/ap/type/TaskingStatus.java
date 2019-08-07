package com.bespinglobal.dcos.ap.type;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ap.type.TaskingStatus
 * Version : 2019.08.01 v0.1
 * Created by taehyoung.yim on 2019-08-01.
 * *** 저작권 주의 ***
 */
public enum TaskingStatus {

    queued("queued"),
    running("running"),
    skipped("skipped"),
    success("success"),
    failed("failed"),
    retry("retry"),
    removed("removed"),
    up_for_retry("up_for_retry"),
    None("None");

    private String value;

    TaskingStatus(String value) {
        this.value = value;
    }

    public String getKey() { return name(); }
    public String getValue() { return value; }
}
