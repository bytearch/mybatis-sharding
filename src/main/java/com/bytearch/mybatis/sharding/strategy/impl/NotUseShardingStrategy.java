package com.bytearch.mybatis.sharding.strategy.impl;

import com.bytearch.mybatis.sharding.exception.ShardingException;
import com.bytearch.mybatis.sharding.strategy.IShardingStrategy;

/**
 *
 * @author yarw
 */
public class NotUseShardingStrategy implements IShardingStrategy {
    @Override
    public String getSchemaName(String originSchema, Object partitionKey) {
        throw new ShardingException("error, unreachable code!");
    }

    @Override
    public void changeDatasourceByPartitionKey(Object partitionKey, boolean useMaster) {
        throw new ShardingException("error, unreachable code!");
    }

    @Override
    public String getTargetTable(String table, Object shardingParam) {
        throw new ShardingException("error, unreachable code!");
    }
}
