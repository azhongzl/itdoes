package ${packageName};

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.itdoes.common.business.entity.BaseEntity;

/**
 * This code is auto-generated.
 * 
 * @author Jalen Zhong
 */
@Entity
@Table(name = "${tableName}")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
<#if search??>
${search}
</#if>
public class ${className} extends BaseEntity {
	private static final long serialVersionUID = ${serialVersionUID}L;

<#list fieldList as field>
  <#if field.column.pk>
	@Id
	${idGeneratedValue}
  </#if>
  <#if field.constraint??>
	${field.constraint}
  </#if>
  <#if field.notNull??>
	${field.notNull}
  </#if>
  <#if field.perm??>
	${field.perm}
  </#if>
  <#if field.search??>
	${field.search}
  </#if>
  <#if field.upload??>
	${field.upload}
  </#if>
	@Column(name = "${field.column.name}")
	private ${field.type} ${field.name};
</#list>
<#list fieldList as field>

	public ${field.type} <#if field.type == "boolean">is<#else>get</#if>${field.upperName}() {
		return ${field.name};
	}

	public void set${field.upperName}(${field.type} ${field.name}) {
		this.${field.name} = ${field.name};
	}
</#list>
<#if extension??>

${extension}
</#if>
}