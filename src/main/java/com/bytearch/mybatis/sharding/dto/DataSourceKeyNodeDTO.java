package com.bytearch.mybatis.sharding.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yarw
 */
@Data
public class DataSourceKeyNodeDTO {
    private String master;
    private List<String> slaves = new ArrayList<>();
}