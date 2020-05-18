package com.bytearch.mybatis.sharding.dto;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;

import java.util.List;

/**
 * 一主多从数据源配置结构
 * @author yarw
 */
@Data
public class DataSourceNodeDTO {
    private String name;
    private HikariConfig master;
    private List<HikariConfig> slaves;
}