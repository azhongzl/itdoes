package com.itdoes.common.business;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class PermsTest {
	@Test
	public void getPermission() {
		assertThat(Perms.getAllPerm()).isEqualTo("itdoes");

		assertThat(Perms.getSearchAllPerm()).isEqualTo("itdoes:search");
		assertThat(Perms.getSearchPerm("buildIndex")).isEqualTo("itdoes:search:buildIndex");

		assertThat(Perms.getUploadAllPerm()).isEqualTo("itdoes:upload");
		assertThat(Perms.getUploadPerm("myResource")).isEqualTo("itdoes:upload:myResource");

		String entityName = "MyEntity";
		String fieldName = "myField";
		assertThat(Perms.getEntityAllPerm()).isEqualTo("itdoes:entity");
		assertThat(Perms.getEntityOneEntityAllPerm(entityName)).isEqualTo("itdoes:entity:" + entityName);
		assertThat(Perms.getEntityOneEntityClassAllPerm(entityName))
				.isEqualTo("itdoes:entity:" + entityName + ":class");
		assertThat(Perms.getEntityOneEntityClassPerm(entityName, "post"))
				.isEqualTo("itdoes:entity:" + entityName + ":class:post");
		assertThat(Perms.getEntityOneEntityAllFieldsAllPerm(entityName))
				.isEqualTo("itdoes:entity:" + entityName + ":field");
		assertThat(Perms.getEntityOneEntityOneFieldAllPerm(entityName, fieldName))
				.isEqualTo("itdoes:entity:" + entityName + ":field:" + fieldName);
		assertThat(Perms.getEntityOneEntityOneFieldReadPerm(entityName, fieldName))
				.isEqualTo("itdoes:entity:" + entityName + ":field:" + fieldName + ":read");
		assertThat(Perms.getEntityOneEntityOneFieldWritePerm(entityName, fieldName))
				.isEqualTo("itdoes:entity:" + entityName + ":field:" + fieldName + ":write");
	}
}
