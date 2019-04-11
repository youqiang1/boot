package com.yq.mybatis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p> mybatis配置</p>
 * @author youq  2019/4/11 14:20
 */
@Configuration
@MapperScan("com.yq.mybatis.mapper")
public class MybatisConfig {
}
