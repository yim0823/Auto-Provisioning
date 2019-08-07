package com.bespinglobal.dcos.ap.api.service.crawler;

import com.bespinglobal.dcos.ap.api.component.crawler.AccountDbCrawler;
import com.bespinglobal.dcos.ap.api.component.crawler.AwsAssetDbCrawler;
import com.bespinglobal.dcos.ap.api.component.crawler.CollectorContext;
import com.bespinglobal.dcos.ap.api.component.crawler.GcpAssetDbCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ic.api.service.crawler.CrawlerServiceImpl
 * Version : 2019.07.25 v0.1
 * Created by taehyoung.yim on 2019-07-25.
 * *** 저작권 주의 ***
 */
@Service
public class AssetServiceImpl implements AssetService {

    private static final Logger logger = LoggerFactory.getLogger(AssetServiceImpl.class);

    private CollectorContext collectorService;

    public AssetServiceImpl() {
        this.collectorService = new CollectorContext();
    }

    @Override
    public void crawlingAwsDataByService() {

        collectorService.setDbCrawlerType(new AwsAssetDbCrawler());
        logger.info(collectorService.crawling());
    }

    @Override
    public void crawlingAwsAccountDataByService() {

        collectorService.setDbCrawlerType(new GcpAssetDbCrawler());
        logger.info(collectorService.crawling());
    }


    @Override
    public void crawlingAllAccountData() {

        collectorService.setDbCrawlerType(new AccountDbCrawler());
        logger.info(collectorService.crawling());
    }
}
