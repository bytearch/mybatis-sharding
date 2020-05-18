package com.bytearch.mybatis.sharding.annotation;


import com.bytearch.mybatis.sharding.common.ShardingConstant;

import java.lang.annotation.*;

/**
 *
 * @ShardingBy作用于方法 和 Bean属性  优先级 方法 > 属性
 * @author yarw
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ShardingBy {
    /**
     * 指定分片参数
     * @return
     */
    String value() default ShardingConstant.DEFAULT_PARTITION_KEY_NAME;
}
