package com.bytearch.mybatis.sharding.strategy.impl;


import com.bytearch.mybatis.sharding.exception.ShardingException;
import com.bytearch.mybatis.sharding.strategy.IDatabaseShardingStrategy;

/**
 * @author yarw
 */
public final class NotUseDatabaseShardingStrategy implements IDatabaseShardingStrategy {
    @Override
    public String getSchemaName(String originSchema, Object partitionKey) {
        throw new ShardingException("error, unreachable code!");
    }

    @Override
    public void changeDatasourceByPartitionKey(Object partitionKey, boolean useMaster) {
        throw new ShardingException("error, unreachable code!");
    }
}
