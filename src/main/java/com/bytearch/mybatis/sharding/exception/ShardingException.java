package com.bytearch.mybatis.sharding.exception;

/**
 * 分片异常类
 */
public class ShardingException extends RuntimeException {
    public ShardingException(Throwable e) {
        super(e);
    }

    public ShardingException(Exception e) {
        super(e);
    }

    public ShardingException(String message) {
        super(message);
    }

    public ShardingException(String format, Object... args) {
        super(String.format(format, args));
    }
}
