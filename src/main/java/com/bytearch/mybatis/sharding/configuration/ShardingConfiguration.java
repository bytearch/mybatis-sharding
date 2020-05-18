package com.bytearch.mybatis.sharding.configuration;

import com.bytearch.mybatis.sharding.common.NodeNameEnum;
import com.bytearch.mybatis.sharding.common.ShardingConstant;
import com.bytearch.mybatis.sharding.dto.DataSourceKeyNodeDTO;
import com.bytearch.mybatis.sharding.dto.DataSourceNodeDTO;
import com.bytearch.mybatis.sharding.exception.ShardingException;
import com.bytearch.mybatis.sharding.plugin.ShardingInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 分片配置注入
 *
 * @author yarw
 */
@Configuration
@Slf4j
public class ShardingConfiguration {
    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    @Resource
    ShardingDateSourceConfig shardingDateSourceConfig;

    @Resource
    NormalDateSourceConfig normalDateSourceConfig;

    @Bean("dynamicDataSource")
    @Primary
    public DynamicDatasource datasource() {
        Map<Object, Object> datasourceMap = new HashMap<>();
        List<DataSourceNodeDTO> datasources = new ArrayList<>();
        //添加分库节点
        if (!CollectionUtils.isEmpty(shardingDateSourceConfig.getDatasources())) {
            datasources.addAll(shardingDateSourceConfig.getDatasources());
        }
        //添加默认节点
        if (!CollectionUtils.isEmpty(normalDateSourceConfig.getDatasources())) {
            datasources.addAll(normalDateSourceConfig.getDatasources());
        }
        //设计分库动态节点数据源
        if (datasources.isEmpty()) {
            throw new ShardingException("shard config is not exists!");
        }
        for (DataSourceNodeDTO datasource : datasources) {
            DataSourceKeyNodeDTO dataSourceKeyNodeDTO = new DataSourceKeyNodeDTO();
            NodeNameEnum nodeNameEnum = NodeNameEnum.valueOf(datasource.getName());
            String masterDatasourceKey = datasource.getName() + ShardingConstant.nodeKeyMaster;
            //设置主节点key
            dataSourceKeyNodeDTO.setMaster(masterDatasourceKey);
            //设置主节点数据源
            datasourceMap.put(masterDatasourceKey, new HikariDataSource(datasource.getMaster()));

            List<HikariConfig> slavesHikariConfig = datasource.getSlaves();
            if (!CollectionUtils.isEmpty(slavesHikariConfig)) {
                List<String> nodeKeySlaves = new ArrayList<>();
                for (int index = 0; index < slavesHikariConfig.size(); index++) {
                    String slaveDatasourceKey = datasource.getName() + ShardingConstant.nodeKeySlave + index;
                    nodeKeySlaves.add(slaveDatasourceKey);
                    //设置从节点数据源
                    datasourceMap.put(slaveDatasourceKey, new HikariDataSource(slavesHikariConfig.get(index)));
                }
                dataSourceKeyNodeDTO.setSlaves(nodeKeySlaves);
            }
            DynamicDataSourceContextHolder.putNode(nodeNameEnum, dataSourceKeyNodeDTO);
        }
        if (CollectionUtils.isEmpty(datasourceMap)) {
            throw new ShardingException("datasource con't be empty!");
        }
        DynamicDatasource dynamicDatasource = new DynamicDatasource();
        //设置动态数据源
        dynamicDatasource.setTargetDataSources(datasourceMap);
        return dynamicDatasource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Autowired DynamicDatasource dynamicDatasource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDatasource);
        //添加插件
        sqlSessionFactoryBean.setPlugins(new ShardingInterceptor());
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 注入事务管理器
     *
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(@Autowired DynamicDatasource dynamicDatasource) {
        return new DataSourceTransactionManager(dynamicDatasource);
    }
}
