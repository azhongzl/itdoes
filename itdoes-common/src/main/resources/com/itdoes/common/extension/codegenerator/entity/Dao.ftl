package ${config.packageName};

import ${config.entityPackageName}.${config.entityClassName};

/**
 * This code is auto-generated.
 * 
 * @author Jalen Zhong
 */
public interface ${config.className} extends com.itdoes.common.business.dao.<#if config.queryCacheEnabled>BaseQueryCacheDao<#else>BaseDao</#if><${config.entityClassName}, ${config.entityIdType}> {
}