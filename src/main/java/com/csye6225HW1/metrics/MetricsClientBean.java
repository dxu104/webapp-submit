package com.csye6225HW1.metrics;

import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class MetricsClientBean {

    private final static Logger logger = LoggerFactory.getLogger(MetricsClientBean.class);

    @Value("${publish.metrics}")
    private boolean metricsPublish;

    @Value("${metrics.server.hostname}")
    private String metricsServerHostName;

    @Value("${metrics.server.port}")
    private int metricsServerPort;
//在应用程序中创建 StatsD 客户端实例：
    @Bean
    public StatsDClient metricsClient(){
        logger.info("publish metrics: "+ metricsPublish);
        if(metricsPublish) {
            return new NonBlockingStatsDClient("csye6225", metricsServerHostName, metricsServerPort);
        }
        return new NoOpStatsDClient();
    }

}
