package com.bytearch.mybatis.sharding.dao;

import com.bytearch.mybatis.sharding.annotation.DB;
import com.bytearch.mybatis.sharding.annotation.ShardingBy;
import com.bytearch.mybatis.sharding.entity.Kv;
import com.bytearch.mybatis.sharding.strategy.impl.LongHashTableShardingStrategy;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author yarw
 */
@DB(tableShardingStrategy = LongHashTableShardingStrategy.class, schema = "ba_test", tableName = "kv")
@Mapper
public interface KVShardingMapper {
    @Select("select * from kv where id = #{id}")
    @ShardingBy("id")
    List<Kv> selectById(@Param("id") Long id);

    @Update("update kv set value = #{value} where id = #{id} and name = #{name}")
    int update(Kv kv);

    @Insert("insert into kv(id,name,value,type) value(#{id}, #{name}, #{value}, #{type})")
    int insert(Kv kv);
}