package com.bytearch.mybatis.sharding;

import com.bytearch.mybatis.sharding.configuration.ShardingDateSourceConfig;
import com.bytearch.mybatis.sharding.dao.ArticleShardingMapper;
import com.bytearch.mybatis.sharding.dao.KVShardingMapper;
import com.bytearch.mybatis.sharding.entity.Article;
import com.bytearch.mybatis.sharding.entity.Kv;
import com.bytearch.mybatis.sharding.sequence.SeqIdUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
public class DBApplicationTests {
    @Resource
    KVShardingMapper KVShardingMapper;

    @Autowired
    ShardingDateSourceConfig shardingDateSourceConfig;

    @Autowired
    ArticleShardingMapper articleShardingMapper;

    @Test
    public void testShardConfig() {
        System.out.println(shardingDateSourceConfig);
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUpdateKV() {
        Kv kv = new Kv();
        kv.setId(SeqIdUtil.nextId(2L));
        kv.setName("bytearch");
        kv.setValue("浅谈架构");
        try {
            KVShardingMapper.update(kv);
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertKVTest() {
        Kv kv = new Kv();
        kv.setId(SeqIdUtil.nextId(2L));
        kv.setName("bytearch");
        kv.setValue("浅谈架构");
        kv.setType(1);
        KVShardingMapper.insert(kv);
    }

    @Test
    public void insertArticleTest() {
        Article article = new Article();
        Long userId = 1L;
        article.setId(SeqIdUtil.nextId(userId));
        article.setUserId(userId);
        article.setStatus((byte)1);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        articleShardingMapper.insert(article);
    }
    @Test
    public void selectArticleTest() {
        Article article = articleShardingMapper.selectById(201364919411081472L, SeqIdUtil.decodeId(201364919411081472L).getExtraId());
        System.out.println(article);
    }
}
