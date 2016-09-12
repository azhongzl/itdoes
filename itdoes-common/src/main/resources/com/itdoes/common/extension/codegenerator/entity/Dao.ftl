package ${config.packageName};

<#if config.queryCacheEnabled>
import com.itdoes.common.business.dao.BaseQueryCacheDao;
<#else>
import com.itdoes.common.business.dao.BaseDao;
</#if>
import ${config.entityPackageName}.${config.entityClassName};

/**
 * This code is auto-generated.
 * 
 * @author Jalen Zhong
 */
public interface ${config.className} extends <#if config.queryCacheEnabled>BaseQueryCacheDao<#else>BaseDao</#if><${config.entityClassName}, ${config.entityIdType}> {
}