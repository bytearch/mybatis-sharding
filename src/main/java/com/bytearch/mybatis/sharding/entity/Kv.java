package com.bytearch.mybatis.sharding.entity;

import com.bytearch.mybatis.sharding.annotation.ShardingBy;
import lombok.Data;

@Data
public class Kv {
    @ShardingBy
    private Long id;

    /**
     * 存储name
     */
    private String name;

    /**
     * 存储value
     */
    private String value;

    /**
     * 字段类型: 1: string , 2: json
     */
    private Integer type;
}