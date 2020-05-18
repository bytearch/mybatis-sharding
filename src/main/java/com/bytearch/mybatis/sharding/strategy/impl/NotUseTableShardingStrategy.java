package com.bytearch.mybatis.sharding.strategy.impl;


import com.bytearch.mybatis.sharding.strategy.ITableShardingStrategy;

/**
 * 不使用分表策略
 *
 * @author yarw
 */
public final class NotUseTableShardingStrategy implements ITableShardingStrategy {
    @Override
    public String getTargetTable(String table, Object shardingParam) {
        throw new UnsupportedOperationException("error, unreachable code!");
    }
}
