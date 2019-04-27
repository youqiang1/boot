package com.yq.mybatis.multi.config;

import com.sun.istack.internal.Nullable;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用DatabaseContextHolder获取当前线程的DatabaseType
 *
 * 2018/1/15 18:56
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    static final Map<DatabaseTypeEnum, List<String>> METHOD_TYPE_MAP = new HashMap<>();

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        DatabaseTypeEnum type = DatabaseContextHolder.getDatabaseType();
        logger.info("=========== dataSource ==========" + type);
        return type;
    }

}
