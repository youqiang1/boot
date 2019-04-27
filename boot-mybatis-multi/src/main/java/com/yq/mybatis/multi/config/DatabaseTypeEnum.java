package com.yq.mybatis.multi.config;

/**
 * <p> 数据源类型</p>
 * @author youq  2019/4/27 17:30
 */
public enum DatabaseTypeEnum {

    DB1("db1"),

    DB2("db2");

    private String name;

    DatabaseTypeEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DatabaseType{name=" + name + "}";
    }

}
