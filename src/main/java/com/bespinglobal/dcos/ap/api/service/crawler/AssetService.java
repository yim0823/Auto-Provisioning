package com.bespinglobal.dcos.ap.api.service.crawler;

/**
 * Project : Information-Collector
 * Class : com.bespinglobal.dcos.ic.api.service.crawler.AssetService
 * Version : 2019.09.25 v0.1
 * Created by taehyoung.yim on 2019-07-25.
 * *** 저작권 주의 ***
 */
public interface AssetService {

    void crawlingAwsDataByService();

    void crawlingAwsAccountDataByService();

    void crawlingAllAccountData();
}
