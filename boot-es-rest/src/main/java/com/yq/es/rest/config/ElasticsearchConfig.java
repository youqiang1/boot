package com.yq.es.rest.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * <p> es rest client</p>
 * @author youq  2019/5/5 17:48
 */
@Slf4j
@Configuration
public class ElasticsearchConfig {

    private static final int ADDRESS_LENGTH = 2;

    private static final String HTTP_SCHEME = "http";

    @Value("${elasticsearch.ip}")
    private String ipAddress;

    @Bean
    public RestClient restClient() {
        HttpHost[] hosts = Arrays.stream(ipAddress.split(","))
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        return RestClient.builder(hosts).setMaxRetryTimeoutMillis(60 * 1000).build();
    }

    private HttpHost makeHttpHost(String host) {
        if (StringUtils.isEmpty(host)) {
            return null;
        }
        String[] address = host.split(":");
        if (ADDRESS_LENGTH == address.length) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, HTTP_SCHEME);
        }
        return null;
    }

}
