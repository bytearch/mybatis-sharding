package com.bytearch.mybatis.sharding.annotation;

import java.lang.annotation.*;

/**
 * @author yarw
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface UseMaster {
}
