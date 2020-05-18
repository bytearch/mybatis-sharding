package com.bytearch.mybatis.sharding.dao;

import com.bytearch.mybatis.sharding.annotation.DB;
import com.bytearch.mybatis.sharding.annotation.ShardingBy;
import com.bytearch.mybatis.sharding.entity.Article;
import com.bytearch.mybatis.sharding.strategy.impl.LongHashDatabasePartitionStrategy;
import org.apache.ibatis.annotations.*;

/**
 * @author yarw
 */
@DB(databaseShardingStrategy = LongHashDatabasePartitionStrategy.class, schema = "blog", tableName = "article")
@Mapper
public interface ArticleShardingMapper {
    @Select("select * from article where id = #{id}")
    @ShardingBy("shardingKey")
    Article selectById(@Param("id") Long id, @Param("shardingKey") Long shardingKey);

    @Insert("insert into article (id, user_id, status,create_time,update_time) value(#{id}, #{userId}, #{status}, #{createTime}, #{updateTime})")
    int insert(Article kv);
}