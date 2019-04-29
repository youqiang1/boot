# boot
springboot + sharding + mybatis

简单的水平分片单库分表实现，这里采用的是按照字段sex取模分片存储。
2张实体表，每次查询的时候两张表一起查询。
插入10万条数据耗时：276945毫秒
SELECT * FROM t_user order by id DESC limit 50000, 10;耗时：360毫秒


既分库又分表其实只需要在配置文件修改一个分片规则即可，不用修改业务任何代码。
分库分表的数据表不能用自增主键，Sharding-JDBC会自动分配一个id，默认使用雪花算法（snowflake）生成64bit的长整型数据。
###修改分配规则
#数据源名称，多数据以逗号分隔
sharding:
  jdbc:
    datasource:
      names: ds0,ds1
      # 数据源ds0
      ds0:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/simple_dev
        username: root
        password: root
      # 数据源ds1
      ds1:
        type: com.alibaba.druid.pool.DruidDataSource
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/simple_dev2
        username: root
        password: root
    config:
      sharding:
        props:
          sql.show: true
        tables:
          t_user:  #t_user表
            key-generator-column-name: id  #主键
            actual-data-nodes: ds${0..1}.t_user${0..1}    #数据节点,均匀分布
            database-strategy:   #分库策略
              inline: #行表达式
                sharding-column: city_id        #列名称，多个列以逗号分隔
                algorithm-expression: ds${city_id % 2}    #按模运算分配
            table-strategy:  #分表策略
              inline: #行表达式
                sharding-column: sex
                algorithm-expression: t_user${sex % 2}
          t_address:
            key-generator-column-name: id
            actual-data-nodes: ds${0..1}.t_address
            database-strategy:
              inline:
                sharding-column: lit
                algorithm-expression: ds${lit % 2}
      
      
      
                
###官方配置示例：
 数据分片
 
 sharding.jdbc.datasource.names=ds0,ds1
 
 sharding.jdbc.datasource.ds0.type=org.apache.commons.dbcp.BasicDataSource
 sharding.jdbc.datasource.ds0.driver-class-name=com.mysql.jdbc.Driver
 sharding.jdbc.datasource.ds0.url=jdbc:mysql://localhost:3306/ds0
 sharding.jdbc.datasource.ds0.username=root
 sharding.jdbc.datasource.ds0.password=
 
 sharding.jdbc.datasource.ds1.type=org.apache.commons.dbcp.BasicDataSource
 sharding.jdbc.datasource.ds1.driver-class-name=com.mysql.jdbc.Driver
 sharding.jdbc.datasource.ds1.url=jdbc:mysql://localhost:3306/ds1
 sharding.jdbc.datasource.ds1.username=root
 sharding.jdbc.datasource.ds1.password=
 
 sharding.jdbc.config.sharding.tables.t_order.actual-data-nodes=ds$->{0..1}.t_order$->{0..1}
 sharding.jdbc.config.sharding.tables.t_order.table-strategy.inline.sharding-column=order_id
 sharding.jdbc.config.sharding.tables.t_order.table-strategy.inline.algorithm-expression=t_order$->{order_id % 2}
 sharding.jdbc.config.sharding.tables.t_order.key-generator-column-name=order_id
 sharding.jdbc.config.sharding.tables.t_order_item.actual-data-nodes=ds$->{0..1}.t_order_item$->{0..1}
 sharding.jdbc.config.sharding.tables.t_order_item.table-strategy.inline.sharding-column=order_id
 sharding.jdbc.config.sharding.tables.t_order_item.table-strategy.inline.algorithm-expression=t_order_item$->{order_id % 2}
 sharding.jdbc.config.sharding.tables.t_order_item.key-generator-column-name=order_item_id
 sharding.jdbc.config.sharding.binding-tables=t_order,t_order_item
 sharding.jdbc.config.sharding.broadcast-tables=t_config
 
 sharding.jdbc.config.sharding.default-database-strategy.inline.sharding-column=user_id
 sharding.jdbc.config.sharding.default-database-strategy.inline.algorithm-expression=ds$->{user_id % 2}               
                
读写分离

harding.jdbc.datasource.names=master,slave0,slave1

sharding.jdbc.datasource.master.type=org.apache.commons.dbcp.BasicDataSource
sharding.jdbc.datasource.master.driver-class-name=com.mysql.jdbc.Driver
sharding.jdbc.datasource.master.url=jdbc:mysql://localhost:3306/master
sharding.jdbc.datasource.master.username=root
sharding.jdbc.datasource.master.password=

sharding.jdbc.datasource.slave0.type=org.apache.commons.dbcp.BasicDataSource
sharding.jdbc.datasource.slave0.driver-class-name=com.mysql.jdbc.Driver
sharding.jdbc.datasource.slave0.url=jdbc:mysql://localhost:3306/slave0
sharding.jdbc.datasource.slave0.username=root
sharding.jdbc.datasource.slave0.password=

sharding.jdbc.datasource.slave1.type=org.apache.commons.dbcp.BasicDataSource
sharding.jdbc.datasource.slave1.driver-class-name=com.mysql.jdbc.Driver
sharding.jdbc.datasource.slave1.url=jdbc:mysql://localhost:3306/slave1
sharding.jdbc.datasource.slave1.username=root
sharding.jdbc.datasource.slave1.password=

sharding.jdbc.config.masterslave.load-balance-algorithm-type=round_robin
sharding.jdbc.config.masterslave.name=ms
sharding.jdbc.config.masterslave.master-data-source-name=master
sharding.jdbc.config.masterslave.slave-data-source-names=slave0,slave1

sharding.jdbc.config.props.sql.show=true


数据分片 + 读写分离

sharding.jdbc.datasource.names=master0,master1,master0slave0,master0slave1,master1slave0,master1slave1

sharding.jdbc.datasource.master0.type=org.apache.commons.dbcp.BasicDataSource
sharding.jdbc.datasource.master0.driver-class-name=com.mysql.jdbc.Driver
sharding.jdbc.datasource.master0.url=jdbc:mysql://localhost:3306/master0
sharding.jdbc.datasource.master0.username=root
sharding.jdbc.datasource.master0.password=

sharding.jdbc.datasource.master0slave0.type=org.apache.commons.dbcp.BasicDataSource
sharding.jdbc.datasource.master0slave0.driver-class-name=com.mysql.jdbc.Driver
sharding.jdbc.datasource.master0slave0.url=jdbc:mysql://localhost:3306/master0slave0
sharding.jdbc.datasource.master0slave0.username=root
sharding.jdbc.datasource.master0slave0.password=
sharding.jdbc.datasource.master0slave1.type=org.apache.commons.dbcp.BasicDataSource
sharding.jdbc.datasource.master0slave1.driver-class-name=com.mysql.jdbc.Driver
sharding.jdbc.datasource.master0slave1.url=jdbc:mysql://localhost:3306/master0slave1
sharding.jdbc.datasource.master0slave1.username=root
sharding.jdbc.datasource.master0slave1.password=

sharding.jdbc.datasource.master1.type=org.apache.commons.dbcp.BasicDataSource
sharding.jdbc.datasource.master1.driver-class-name=com.mysql.jdbc.Driver
sharding.jdbc.datasource.master1.url=jdbc:mysql://localhost:3306/master1
sharding.jdbc.datasource.master1.username=root
sharding.jdbc.datasource.master1.password=

sharding.jdbc.datasource.master1slave0.type=org.apache.commons.dbcp.BasicDataSource
sharding.jdbc.datasource.master1slave0.driver-class-name=com.mysql.jdbc.Driver
sharding.jdbc.datasource.master1slave0.url=jdbc:mysql://localhost:3306/master1slave0
sharding.jdbc.datasource.master1slave0.username=root
sharding.jdbc.datasource.master1slave0.password=
sharding.jdbc.datasource.master1slave1.type=org.apache.commons.dbcp.BasicDataSource
sharding.jdbc.datasource.master1slave1.driver-class-name=com.mysql.jdbc.Driver
sharding.jdbc.datasource.master1slave1.url=jdbc:mysql://localhost:3306/master1slave1
sharding.jdbc.datasource.master1slave1.username=root
sharding.jdbc.datasource.master1slave1.password=

sharding.jdbc.config.sharding.tables.t_order.actual-data-nodes=ds$->{0..1}.t_order$->{0..1}
sharding.jdbc.config.sharding.tables.t_order.table-strategy.inline.sharding-column=order_id
sharding.jdbc.config.sharding.tables.t_order.table-strategy.inline.algorithm-expression=t_order$->{order_id % 2}
sharding.jdbc.config.sharding.tables.t_order.key-generator-column-name=order_id
sharding.jdbc.config.sharding.tables.t_order_item.actual-data-nodes=ds$->{0..1}.t_order_item$->{0..1}
sharding.jdbc.config.sharding.tables.t_order_item.table-strategy.inline.sharding-column=order_id
sharding.jdbc.config.sharding.tables.t_order_item.table-strategy.inline.algorithm-expression=t_order_item$->{order_id % 2}
sharding.jdbc.config.sharding.tables.t_order_item.key-generator-column-name=order_item_id
sharding.jdbc.config.sharding.binding-tables=t_order,t_order_item
sharding.jdbc.config.sharding.broadcast-tables=t_config

sharding.jdbc.config.sharding.default-database-strategy.inline.sharding-column=user_id
sharding.jdbc.config.sharding.default-database-strategy.inline.algorithm-expression=master$->{user_id % 2}

sharding.jdbc.config.sharding.master-slave-rules.ds0.master-data-source-name=master0
sharding.jdbc.config.sharding.master-slave-rules.ds0.slave-data-source-names=master0slave0, master0slave1
sharding.jdbc.config.sharding.master-slave-rules.ds1.master-data-source-name=master1
sharding.jdbc.config.sharding.master-slave-rules.ds1.slave-data-source-names=master1slave0, master1slave1

数据治理

sharding.jdbc.datasource.names=ds,ds0,ds1
sharding.jdbc.datasource.ds.type=org.apache.commons.dbcp.BasicDataSource
sharding.jdbc.datasource.ds.driver-class-name=org.h2.Driver
sharding.jdbc.datasource.ds.url=jdbc:mysql://localhost:3306/ds
sharding.jdbc.datasource.ds.username=root
sharding.jdbc.datasource.ds.password=

sharding.jdbc.datasource.ds0.type=org.apache.commons.dbcp.BasicDataSource
sharding.jdbc.datasource.ds0.driver-class-name=com.mysql.jdbc.Driver
sharding.jdbc.datasource.ds0.url=jdbc:mysql://localhost:3306/ds0
sharding.jdbc.datasource.ds0.username=root
sharding.jdbc.datasource.ds0.password=

sharding.jdbc.datasource.ds1.type=org.apache.commons.dbcp.BasicDataSource
sharding.jdbc.datasource.ds1.driver-class-name=com.mysql.jdbc.Driver
sharding.jdbc.datasource.ds1.url=jdbc:mysql://localhost:3306/ds1
sharding.jdbc.datasource.ds1.username=root
sharding.jdbc.datasource.ds1.password=

sharding.jdbc.config.sharding.default-data-source-name=ds
sharding.jdbc.config.sharding.default-database-strategy.inline.sharding-column=user_id
sharding.jdbc.config.sharding.default-database-strategy.inline.algorithm-expression=ds$->{user_id % 2}
sharding.jdbc.config.sharding.tables.t_order.actual-data-nodes=ds$->{0..1}.t_order$->{0..1}
sharding.jdbc.config.sharding.tables.t_order.table-strategy.inline.sharding-column=order_id
sharding.jdbc.config.sharding.tables.t_order.table-strategy.inline.algorithm-expression=t_order$->{order_id % 2}
sharding.jdbc.config.sharding.tables.t_order.key-generator-column-name=order_id
sharding.jdbc.config.sharding.tables.t_order_item.actual-data-nodes=ds$->{0..1}.t_order_item$->{0..1}
sharding.jdbc.config.sharding.tables.t_order_item.table-strategy.inline.sharding-column=order_id
sharding.jdbc.config.sharding.tables.t_order_item.table-strategy.inline.algorithm-expression=t_order_item$->{order_id % 2}
sharding.jdbc.config.sharding.tables.t_order_item.key-generator-column-name=order_item_id
sharding.jdbc.config.sharding.binding-tables=t_order,t_order_item
sharding.jdbc.config.sharding.broadcast-tables=t_config

sharding.jdbc.config.sharding.default-database-strategy.inline.sharding-column=user_id
sharding.jdbc.config.sharding.default-database-strategy.inline.algorithm-expression=master$->{user_id % 2}

sharding.jdbc.config.orchestration.name=spring_boot_ds_sharding
sharding.jdbc.config.orchestration.overwrite=true
sharding.jdbc.config.orchestration.registry.namespace=orchestration-spring-boot-sharding-test
sharding.jdbc.config.orchestration.registry.server-lists=localhost:2181

Sharding-JDBC不支持的项
DataSource接口
不支持timeout相关操作
Connection接口
不支持存储过程，函数，游标的操作
不支持执行native的SQL
不支持savepoint相关操作
不支持Schema/Catalog的操作
不支持自定义类型映射
Statement和PreparedStatement接口
不支持返回多结果集的语句（即存储过程，非SELECT多条数据）
不支持国际化字符的操作
对于ResultSet接口
不支持对于结果集指针位置判断
不支持通过非next方法改变结果指针位置
不支持修改结果集内容
不支持获取国际化字符
不支持获取Array

官方文档：
https://shardingsphere.apache.org/document/legacy/3.x/document/cn/overview/