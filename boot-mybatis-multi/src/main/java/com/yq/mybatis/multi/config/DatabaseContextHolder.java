package com.yq.mybatis.multi.config;

/**
 * <p> 保存一个线程安全的DatabaseTypeEnum容器</p>
 * @author youq  2019/4/27 17:51
 */
public class DatabaseContextHolder {

    private static final ThreadLocal<DatabaseTypeEnum> contextHolder = new ThreadLocal<>();

    public static final DatabaseTypeEnum DEFAULT_DATASOURCE = DatabaseTypeEnum.DB1;

    public static void setDatabaseType(DatabaseTypeEnum type) {
        contextHolder.set(type);
    }

    public static DatabaseTypeEnum getDatabaseType() {
        return contextHolder.get();
    }

}
