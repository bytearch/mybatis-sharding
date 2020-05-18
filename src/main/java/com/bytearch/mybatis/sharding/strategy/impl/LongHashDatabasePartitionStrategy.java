package com.bytearch.mybatis.sharding.strategy.impl;

import com.bytearch.mybatis.sharding.common.ShardingConstant;
import com.bytearch.mybatis.sharding.configuration.DynamicDataSourceContextHolder;
import com.bytearch.mybatis.sharding.strategy.IDatabaseShardingStrategy;
/**
 * 分库策略 基于hash分库
 * @author yarw
 */
public class LongHashDatabasePartitionStrategy implements IDatabaseShardingStrategy<Long> {

    @Override
    public String getSchemaName(String originSchema, Long partitionKey) {
        if (partitionKey == null) {
            return originSchema;
        }
        return  String.format(originSchema + ShardingConstant.schemaSeparator + "%03d", partitionKey % ShardingConstant.dbNum);
    }

    @Override
    public void changeDatasourceByPartitionKey(Long partitionKey, boolean useMaster) {
        //计算节点 节点从1开始计数
        int index = (int)Math.floorDiv(partitionKey % ShardingConstant.dbNum, ShardingConstant.nodeNum) + 1;
        DynamicDataSourceContextHolder.useDataSourceByNodeIndex(index, useMaster);
    }
}
