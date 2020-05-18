package com.bytearch.mybatis.sharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author yarw
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.bytearch.mybatis.sharding.dao"})
@EnableConfigurationProperties
public class ShardingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShardingApplication.class, args);
    }
}