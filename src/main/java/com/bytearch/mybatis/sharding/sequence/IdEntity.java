package com.bytearch.mybatis.sharding.sequence;

import lombok.Data;

/**
 * @author yarw
 */
@Data
public class IdEntity {
    private long createTime;
    private long workerId;
    private long extraId;
    private long sequenceId;

    @Override
    public String toString() {
        return "IdEntity{" +
                "createTime=" + createTime +
                ", workerId=" + workerId +
                ", extraId=" + extraId +
                ", sequenceId=" + sequenceId +
                '}';
    }
}