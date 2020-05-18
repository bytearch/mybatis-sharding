package com.bytearch.mybatis.sharding.strategy;

import com.bytearch.mybatis.sharding.annotation.DB;
import com.bytearch.mybatis.sharding.strategy.impl.NotUseDatabaseShardingStrategy;
import com.bytearch.mybatis.sharding.strategy.impl.NotUseShardingStrategy;
import com.bytearch.mybatis.sharding.strategy.impl.NotUseTableShardingStrategy;
import org.springframework.lang.Nullable;

/**
 * @author yarw
 */
public class ShardingStrategyUtils {
    public static IDatabaseShardingStrategy getDatabaseShardingStrategy(@Nullable DB DB) throws IllegalAccessException, InstantiationException {
        if (DB == null) {
            return null;
        }
        Class<? extends IDatabaseShardingStrategy> clz = DB.databaseShardingStrategy();
        if (!clz.equals(NotUseDatabaseShardingStrategy.class)) {
            return clz.newInstance();
        }
        return null;
    }

    public static ITableShardingStrategy getTableShardingStrategy(@Nullable DB DB) throws IllegalAccessException, InstantiationException {
        if (DB == null) {
            return null;
        }
        Class<? extends ITableShardingStrategy> clz = DB.tableShardingStrategy();
        if (!clz.equals(NotUseTableShardingStrategy.class)) {
            return clz.newInstance();
        }
        return null;
    }

    public static IShardingStrategy getShardingStategy(@Nullable DB DB) throws IllegalAccessException, InstantiationException {
        if (DB == null) {
            return null;
        }
        Class<? extends IShardingStrategy> clz = DB.shardingStrategy();
        if (!clz.equals(NotUseShardingStrategy.class)) {
            return clz.newInstance();
        }
        return null;
    }


}
