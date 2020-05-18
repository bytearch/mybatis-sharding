package com.bytearch.mybatis.sharding.strategy;

/**
 * 分表策略
 *
 * @author yarw
 */
public interface ITableShardingStrategy<T> {

    /**
     * @param table         逻辑表名
     * @param shardingParam
     * @return
     */
    String getTargetTable(String table, T shardingParam);
}
