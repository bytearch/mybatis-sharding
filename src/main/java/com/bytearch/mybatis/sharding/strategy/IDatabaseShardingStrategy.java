package com.bytearch.mybatis.sharding.strategy;

/**
 * 分表策略
 *
 * @author yarw
 */
public interface IDatabaseShardingStrategy<D> {
    /**
     * 获取实际库名
     *
     * @param originSchema 原始库名(逻辑库名)
     * @param partitionKey 分库key
     * @return
     */
    String getSchemaName(String originSchema, D partitionKey);

    /**
     * 数据源切换逻辑
     * @param partitionKey
     * @param useMaster
     */
    public void changeDatasourceByPartitionKey(D partitionKey, boolean useMaster);
}