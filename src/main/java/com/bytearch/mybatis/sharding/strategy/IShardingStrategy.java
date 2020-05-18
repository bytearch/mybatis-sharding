package com.bytearch.mybatis.sharding.strategy;


/**
 * 表和库切分策略
 * @author yarw
 * @param <D>
 * @param <T>
 */
public interface IShardingStrategy<D, T> extends IDatabaseShardingStrategy<D>, ITableShardingStrategy<T> {
}
