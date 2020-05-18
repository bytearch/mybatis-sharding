package com.bytearch.mybatis.sharding.entity;

import java.util.Date;

import com.bytearch.mybatis.sharding.annotation.ShardingBy;
import lombok.Data;

@Data
public class Article {
    /**
     * 文章id
     */
    private Long id;

    /**
     * 作者id
     */
    @ShardingBy
    private Long userId;

    /**
     * 文章状态 -1: 删除 1:草稿 2:已发布
     */
    private Byte status;

    private Date createTime;

    private Date updateTime;
}