package com.yq.sms.client.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * <p> 数据库配置</p>
 * @author youq  2019/8/23 17:36
 */
@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.yq.sms.client.repository"})
@EntityScan(basePackages = {"com.yq.sms.client.entity", "org.springframework.data.jpa.convert.threeten"})
public class DbRepositoryConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() throws ClassNotFoundException {
        DataSource ds = DataSourceBuilder.create(Class.forName("org.apache.tomcat.jdbc.pool.DataSource").getClassLoader()).build();
        ((org.apache.tomcat.jdbc.pool.DataSource) ds).setValidationQuery("select 1");
        ((org.apache.tomcat.jdbc.pool.DataSource) ds).setTestWhileIdle(true);
        return ds;
    }

}
