package com.bytearch.mybatis.sharding.configuration;

import com.bytearch.mybatis.sharding.dto.DataSourceNodeDTO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 默认(不分库)数据源配置
 * @author yarw
 */
@ConfigurationProperties(prefix = "spring.bytearch.normal", ignoreUnknownFields = false)
@Configuration
@Data
public class NormalDateSourceConfig {
    private List<DataSourceNodeDTO> datasources;
}
