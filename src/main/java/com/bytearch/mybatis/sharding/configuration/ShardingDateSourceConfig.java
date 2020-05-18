package com.bytearch.mybatis.sharding.configuration;

import com.bytearch.mybatis.sharding.dto.DataSourceNodeDTO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 多节点数据源
 *
 * @author yarw
 */
@ConfigurationProperties(prefix = "spring.bytearch.sharding", ignoreUnknownFields = false)
@Configuration
@Data
public class ShardingDateSourceConfig {
    private List<DataSourceNodeDTO> datasources;
}