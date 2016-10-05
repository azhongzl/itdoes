package com.itdoes.common.business;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class PermissionsTest {
	@Test
	public void getPermission() {
		assertThat(Permissions.getAllPermission()).isEqualTo("itdoes");

		assertThat(Permissions.getSearchAllPermission()).isEqualTo("itdoes:search");
		assertThat(Permissions.getSearchPermission("buildIndex")).isEqualTo("itdoes:search:buildIndex");

		assertThat(Permissions.getUploadAllPermission()).isEqualTo("itdoes:upload");
		assertThat(Permissions.getUploadPermission("myResource")).isEqualTo("itdoes:upload:myResource");

		String entityName = "MyEntity";
		String fieldName = "myField";
		assertThat(Permissions.getFacadeAllPermission()).isEqualTo("itdoes:facade");
		assertThat(Permissions.getFacadeOneEntityAllPermission(entityName)).isEqualTo("itdoes:facade:" + entityName);
		assertThat(Permissions.getFacadeOneEntityClassAllPermission(entityName))
				.isEqualTo("itdoes:facade:" + entityName + ":class");
		assertThat(Permissions.getFacadeOneEntityClassPermission(entityName, "post"))
				.isEqualTo("itdoes:facade:" + entityName + ":class:post");
		assertThat(Permissions.getFacadeOneEntityAllFieldsAllPermission(entityName))
				.isEqualTo("itdoes:facade:" + entityName + ":field");
		assertThat(Permissions.getFacadeOneEntityOneFieldAllPermission(entityName, fieldName))
				.isEqualTo("itdoes:facade:" + entityName + ":field:" + fieldName);
		assertThat(Permissions.getFacadeOneEntityOneFieldReadPermission(entityName, fieldName))
				.isEqualTo("itdoes:facade:" + entityName + ":field:" + fieldName + ":read");
		assertThat(Permissions.getFacadeOneEntityOneFieldWritePermission(entityName, fieldName))
				.isEqualTo("itdoes:facade:" + entityName + ":field:" + fieldName + ":write");
	}
}
