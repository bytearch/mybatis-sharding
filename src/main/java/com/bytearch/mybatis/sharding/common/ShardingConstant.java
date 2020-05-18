package com.bytearch.mybatis.sharding.common;

/***
 * 分片常量配置
 * @author yarw
 */
public class ShardingConstant {
    /**
     * 库分片数
     */
    public static final int dbNum = 1024;

    /**
     * 表分片数
     */
    public static final int tableNum = 512;

    /**
     * 节点数  一个节点包括一主多从数据源
     */
    public static final int nodeNum = 2;

    /**
     * 数据源key后缀 配置
     */
    public static final String nodeKeyMaster = "@master";
    public static final String nodeKeySlave = "@slave_";

    /**
     * 数据库名分隔符
     */
    public static final String schemaSeparator = "_";
    /**
     * 表名分隔符
     */
    public static final String tableSeparator = "_";

    /**
     * 默认的分片key参数名称
     */
    public static final String DEFAULT_PARTITION_KEY_NAME = "PARTITION";

}
