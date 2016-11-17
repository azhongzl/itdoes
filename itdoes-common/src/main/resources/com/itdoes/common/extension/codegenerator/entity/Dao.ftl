package ${packageName};

import ${entityPackageName}.${entityClassName};

/**
 * This code is auto-generated.
 * 
 * @author Jalen Zhong
 */
public interface ${className} extends com.itdoes.common.business.dao.<#if queryCacheEnabled>BaseQueryCacheDao<#else>BaseDao</#if><${entityClassName}, ${entityIdType}> {
<#if extension??>

${extension}
</#if>
}