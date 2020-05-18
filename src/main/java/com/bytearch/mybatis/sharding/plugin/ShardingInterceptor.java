package com.bytearch.mybatis.sharding.plugin;

import com.bytearch.mybatis.sharding.annotation.DB;
import com.bytearch.mybatis.sharding.annotation.ShardingBy;
import com.bytearch.mybatis.sharding.annotation.UseMaster;
import com.bytearch.mybatis.sharding.common.NodeNameEnum;
import com.bytearch.mybatis.sharding.configuration.DynamicDataSourceContextHolder;
import com.bytearch.mybatis.sharding.exception.ShardingException;
import com.bytearch.mybatis.sharding.strategy.IDatabaseShardingStrategy;
import com.bytearch.mybatis.sharding.strategy.IShardingStrategy;
import com.bytearch.mybatis.sharding.strategy.ITableShardingStrategy;
import com.bytearch.mybatis.sharding.strategy.ShardingStrategyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.StringUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * @author yarw
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),

        })
@Slf4j
public class ShardingInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        if (Arrays.asList(SqlCommandType.INSERT, SqlCommandType.UPDATE, SqlCommandType.DELETE, SqlCommandType.SELECT).contains(ms.getSqlCommandType())) {
            // 读请求: 默认使用从库
            // 写请求(INSERT,UPDATE,DELETE): 使用主库
            boolean useMaster = !SqlCommandType.SELECT.equals(ms.getSqlCommandType());
            boolean hasChangeDatasource = false;
            DB DB = null;
            String methodId = ms.getId();
            String className = methodId.substring(0, methodId.lastIndexOf('.'));
            String methodName = methodId.substring(methodId.lastIndexOf('.') + 1);
            //是否使用了分库分表策略
            Class clz = Class.forName(className);
            Annotation dbAnno = clz.getAnnotation(DB.class);
            if (dbAnno != null) {
                DB = (DB) dbAnno;
            }
            if (DB != null) {
                //方法是否使用了@UseMaster注解 @PartitionBy注解
                String partitionName = null;
                for (Method declaredMethod : clz.getDeclaredMethods()) {
                    if (!declaredMethod.getName().equals(methodName)) {
                        continue;
                    }
                    if (declaredMethod.getAnnotation(UseMaster.class) != null) {
                        useMaster = true;
                    }
                    ShardingBy shardingByAnno = declaredMethod.getAnnotation(ShardingBy.class);
                    if (shardingByAnno != null) {
                        partitionName = shardingByAnno.value();
                        if (DB == null) {
                            throw new ShardingException("error! must @DB on :{}", clz);
                        }
                    }
                }
                //记录sql是否需要改变
                boolean sqlNeedChanged = false;
                Object partitionKey = null;
                String schema = DB.schema();
                String tableName = DB.tableName();
                //获取partition
                Object pa = args[1];
                if (pa instanceof Map) {
                    //params中获取partitionKey
                    Map<String, Object> paMap = (Map<String, Object>) pa;
                    if (!StringUtils.isEmpty(partitionName)) {
                        partitionKey = paMap.get(partitionName);
                    }
                } else if (pa instanceof Object && partitionKey == null) {
                    //Bean对象中获取partitionKey
                    for (Field declaredField : pa.getClass().getDeclaredFields()) {
                        ShardingBy annotation = declaredField.getAnnotation(ShardingBy.class);
                        if (annotation != null) {
                            declaredField.setAccessible(true);
                            partitionKey = declaredField.get(pa);
                        }
                    }
                }
                if (partitionKey != null) {
                    log.info("[获取到partitionKey:{}]", partitionKey);
                    //权重 分库 < 分表 < 分库分表(原则上同一Mapper策略只配置一种,如果配置多种依次覆盖)
                    //分库
                    IDatabaseShardingStrategy databaseShardingStrategy = ShardingStrategyUtils.getDatabaseShardingStrategy(DB);
                    if (databaseShardingStrategy != null) {
                        schema = databaseShardingStrategy.getSchemaName(DB.schema(), partitionKey);
                        databaseShardingStrategy.changeDatasourceByPartitionKey(partitionKey, useMaster);
                        sqlNeedChanged = true;
                    }
                    //分表
                    ITableShardingStrategy ITableShardingStrategy = ShardingStrategyUtils.getTableShardingStrategy(DB);
                    if (ITableShardingStrategy != null) {
                        tableName = ITableShardingStrategy.getTargetTable(DB.tableName(), partitionKey);
                        sqlNeedChanged = true;
                        NodeNameEnum nodeNameEnum = NodeNameEnum.valueOf(DB.schema());
                        if (nodeNameEnum != null) {
                            DynamicDataSourceContextHolder.useDataSourceByNodeNum(nodeNameEnum, useMaster);
                        }
                    }
                    //分库分表
                    IShardingStrategy shardingStategy = ShardingStrategyUtils.getShardingStategy(DB);
                    if (shardingStategy != null) {
                        schema = shardingStategy.getSchemaName(DB.schema(), partitionKey);
                        tableName = shardingStategy.getTargetTable(DB.tableName(), partitionKey);
                        databaseShardingStrategy.changeDatasourceByPartitionKey(partitionKey, useMaster);
                        sqlNeedChanged = true;
                    }
                } else {
                    //不分库也不分表
                    NodeNameEnum nodeNameEnum = NodeNameEnum.valueOf(DB.schema());
                    if (nodeNameEnum != null) {
                        DynamicDataSourceContextHolder.useDataSourceByNodeNum(nodeNameEnum, useMaster);
                    }
                }
                if (sqlNeedChanged) {
                    BoundSql boundSql = ms.getBoundSql(pa);
                    String originSql = boundSql.getSql();
                    String sql = originSql.replaceAll(DB.tableName(), schema + '.' + tableName);
                    log.info("[更改SQL] sql:{}", sql);
                    BoundSql boundSqlNew = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
                    MappedStatement mappedStatement = copyFromMappedStatement(ms, new BoundSqlSqlSource(boundSqlNew));
                    args[0] = mappedStatement;
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}
