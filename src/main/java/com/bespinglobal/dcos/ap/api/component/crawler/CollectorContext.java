package com.bespinglobal.dcos.ap.api.component.crawler;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ic.api.component.crawler.CollectorContext
 * Version : 2019.07.24 v0.1
 * Created by taehyoung.yim on 2019-07-24.
 * *** 저작권 주의 ***
 */
public class CollectorContext {

    private DbCrawlerStrategy dbCrawlerType;

    public void setDbCrawlerType(DbCrawlerStrategy dbCrawlerType) {
        this.dbCrawlerType = dbCrawlerType;
    }

    public String crawling() {
        return dbCrawlerType.crawling();
    }
}
