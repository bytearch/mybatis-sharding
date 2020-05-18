package com.bytearch.mybatis.sharding.strategy.impl;

import com.bytearch.mybatis.sharding.common.ShardingConstant;
import com.bytearch.mybatis.sharding.strategy.ITableShardingStrategy;

/**
 * hash策略
 *
 * @author yarw
 */
public class LongHashTableShardingStrategy implements ITableShardingStrategy<Long> {
    @Override
    public String getTargetTable(String table, Long partitionKey) {
        if (partitionKey != null) {
            table = String.format(table + ShardingConstant.tableSeparator + "%03d", partitionKey % ShardingConstant.tableNum);
        }
        return table;
    }
}
