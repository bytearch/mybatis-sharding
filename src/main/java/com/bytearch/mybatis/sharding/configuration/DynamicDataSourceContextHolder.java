package com.bytearch.mybatis.sharding.configuration;

import com.bytearch.mybatis.sharding.common.NodeNameEnum;
import com.bytearch.mybatis.sharding.dto.DataSourceKeyNodeDTO;
import com.bytearch.mybatis.sharding.exception.ShardingException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yarw
 */
@Slf4j
public class DynamicDataSourceContextHolder {
    public static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * 锁
     * 切换数据源时不会被其它线程修改
     */
    private static Lock lock = new ReentrantLock();

    /**
     * 计数器
     */
    private static Map<NodeNameEnum, Integer> nodeSlaveReqCount = new HashMap<>();

    /**
     * 维护node的节点池
     */
    private static Map<NodeNameEnum, DataSourceKeyNodeDTO> nodeMap = new HashMap<>();

    public static String getDataSourceKey() {
        return contextHolder.get();
    }

    public synchronized static void putNode(NodeNameEnum nodeNameEnum, DataSourceKeyNodeDTO dataSourceKeyNodeDTO) {
        log.info("[node put DynamicDataSourceContextHolder] nodeNameEnum:{}, nodeKeyConfig:{}", nodeNameEnum, dataSourceKeyNodeDTO);
        nodeMap.put(nodeNameEnum, dataSourceKeyNodeDTO);
    }

    public static void useDataSourceByNodeIndex(int index, boolean useMaster) {
        NodeNameEnum nodeEnumByIndex = NodeNameEnum.getNodeEnumByIndex(index);
        if (nodeEnumByIndex == null) {
            throw new ShardingException("node datasource is not exists!");
        }
        useDataSourceByNodeNum(nodeEnumByIndex, useMaster);
    }

    /**
     * 通过节点切换数据源
     * @param nodeName
     * @param useMaster
     */
    public static void useDataSourceByNodeNum(NodeNameEnum nodeName, boolean useMaster) {
        lock.lock();
        try {
            //判断节点数据源是否存在
            DataSourceKeyNodeDTO nodeConfig = nodeMap.get(nodeName);
            if (nodeConfig == null) {
                throw new ShardingException("datasource node is not exists!");
            }
            log.info("[切换数据源] nodeName:{}, useMaster:{}", nodeName, useMaster);
            //从数据源为空时 使用主库
            if (useMaster || nodeConfig.getSlaves() == null || nodeConfig.getSlaves().isEmpty()) {
                contextHolder.set(nodeConfig.getMaster());
            } else {
                //从库
                Integer count = nodeSlaveReqCount.getOrDefault(nodeName, 0);
                if (count == Integer.MAX_VALUE) {
                    count = 0;
                }
                Integer slaveIndex = count % nodeConfig.getSlaves().size();
                contextHolder.set(nodeConfig.getSlaves().get(slaveIndex));
                nodeSlaveReqCount.put(nodeName, ++count);
            }
        } catch (Exception e) {
            log.error("switch master database error! e:{}", e.getMessage());
            throw e;
        } finally {
            lock.unlock();
        }
    }
}
