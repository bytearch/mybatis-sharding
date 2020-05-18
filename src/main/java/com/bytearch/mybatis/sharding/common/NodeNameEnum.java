package com.bytearch.mybatis.sharding.common;

/**
 * @author yarw
 * 分片数据源节点
 */
public enum NodeNameEnum {
    /**
     * 常规节点 -1 ..... -N
     */
    ba_test(-1),
    /**
     * 分库节点 1 ..... N
     */
    node1(1), node2(2);
    private int index;

    NodeNameEnum(int index) {
        this.index = index;
    }

    /**
     * 通过索引获取节点枚举
     *
     * @param index
     * @return
     */
    public static NodeNameEnum getNodeEnumByIndex(int index) {
        for (NodeNameEnum nodeEnum : NodeNameEnum.values()) {
            if (nodeEnum.index == index) {
                return nodeEnum;
            }
        }
        return null;
    }
}