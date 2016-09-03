package ${packageName};

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.itdoes.common.business.BaseEntity;

/**
 * @author Jalen Zhong
 */
@Entity
@Table(name = "${tableName}")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ${className} extends BaseEntity {
	private static final long serialVersionUID = -8714695363626027841L;

<#list fieldList as field>
  <#if field.pk>
	@Id
	${idGeneratedValue}
  </#if>
	private ${field.type} ${field.name};
</#list>

<#list fieldList as field>
	public ${field.type} get${field.upperName}() {
		return ${field.name};
	}

	public void set${field.upperName}(${field.type} ${field.name}) {
		this.${field.name} = ${field.name};
	}
</#list>
}