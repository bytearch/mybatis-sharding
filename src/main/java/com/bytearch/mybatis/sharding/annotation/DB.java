package com.bytearch.mybatis.sharding.annotation;

import com.bytearch.mybatis.sharding.strategy.IDatabaseShardingStrategy;
import com.bytearch.mybatis.sharding.strategy.IShardingStrategy;
import com.bytearch.mybatis.sharding.strategy.ITableShardingStrategy;
import com.bytearch.mybatis.sharding.strategy.impl.NotUseDatabaseShardingStrategy;
import com.bytearch.mybatis.sharding.strategy.impl.NotUseShardingStrategy;
import com.bytearch.mybatis.sharding.strategy.impl.NotUseTableShardingStrategy;

import java.lang.annotation.*;

/**
 * @author yarw
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DB {
    /**
     * 分表切分策略
     *
     * @return
     */
    Class<? extends ITableShardingStrategy> tableShardingStrategy() default NotUseTableShardingStrategy.class;

    /**
     * 分库切分策略
     *
     * @return
     */
    Class<? extends IDatabaseShardingStrategy> databaseShardingStrategy() default NotUseDatabaseShardingStrategy.class;

    /**
     * 分库&分表切分策略
     * @return
     */
    Class<? extends IShardingStrategy> shardingStrategy() default NotUseShardingStrategy.class;

    /**
     * 逻辑表名
     *
     * @return
     */
    String tableName();

    /**
     * 逻辑库名
     *
     * @return
     */
    String schema();

}