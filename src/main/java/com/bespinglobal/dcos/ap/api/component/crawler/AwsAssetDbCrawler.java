package com.bespinglobal.dcos.ap.api.component.crawler;

import org.springframework.stereotype.Component;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ic.api.component.crawler.GcpAssetDbCrawler
 * Version : 2019.07.25 v0.1
 * Created by taehyoung.yim on 2019-07-25.
 * *** 저작권 주의 ***
 */
@Component
public class AwsAssetDbCrawler implements DbCrawlerStrategy {

    @Override
    public String crawling() {
        return "crawling asset data on AWS from the OpsNow's database";
    }
}
