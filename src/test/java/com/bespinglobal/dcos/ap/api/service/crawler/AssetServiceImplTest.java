package com.bespinglobal.dcos.ap.api.service.crawler;

import com.bespinglobal.dcos.ap.api.component.crawler.AccountDbCrawler;
import com.bespinglobal.dcos.ap.api.component.crawler.AwsAssetDbCrawler;
import com.bespinglobal.dcos.ap.api.component.crawler.CollectorContext;
import com.bespinglobal.dcos.ap.api.component.crawler.GcpAssetDbCrawler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Project : Information-Collector
 * Class : com.bespinglobal.dcos.ap.api.service.crawler.AssetServiceImplTest
 * Version :
 * Created by taehyoung.yim on 2019-07-25.
 * *** 저작권 주의 ***
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AssetServiceImplTest {

    private CollectorContext collectorService;

    @Before
    public void setUp() {
        this.collectorService = new CollectorContext();
    }

    @Test
    public void a1_conform_strategy_pattern() {

        // given
        collectorService.setDbCrawlerType(new AwsAssetDbCrawler());

        // when
        String result = collectorService.crawling();

        // then
        checkTheResult(result, "crawling asset data on AWS from the OpsNow's database");
    }

    @Test
    public void a2_conform_strategty_pattern() {

        // given
        collectorService.setDbCrawlerType(new GcpAssetDbCrawler());

        // when
        String result = collectorService.crawling();

        // then
        checkTheResult(result, "crawling asset data on GCP from the OpsNow's database");
    }

    @Test
    public void a3_conform_strategy_pattern() {

        // given
        collectorService.setDbCrawlerType(new AccountDbCrawler());

        // when
        String result = collectorService.crawling();

        // then
        checkTheResult(result, "crawling account data from the Portal's database");
    }

    private void checkTheResult(String result, String expected) {

        assertThat(result, is(notNullValue()));
        assertThat(result, is(expected));
    }
}