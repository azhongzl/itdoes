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
		assertThat(Permissions.getEntityAllPermission()).isEqualTo("itdoes:entity");
		assertThat(Permissions.getEntityOneEntityAllPermission(entityName)).isEqualTo("itdoes:entity:" + entityName);
		assertThat(Permissions.getEntityOneEntityClassAllPermission(entityName))
				.isEqualTo("itdoes:entity:" + entityName + ":class");
		assertThat(Permissions.getEntityOneEntityClassPermission(entityName, "post"))
				.isEqualTo("itdoes:entity:" + entityName + ":class:post");
		assertThat(Permissions.getEntityOneEntityAllFieldsAllPermission(entityName))
				.isEqualTo("itdoes:entity:" + entityName + ":field");
		assertThat(Permissions.getEntityOneEntityOneFieldAllPermission(entityName, fieldName))
				.isEqualTo("itdoes:entity:" + entityName + ":field:" + fieldName);
		assertThat(Permissions.getEntityOneEntityOneFieldReadPermission(entityName, fieldName))
				.isEqualTo("itdoes:entity:" + entityName + ":field:" + fieldName + ":read");
		assertThat(Permissions.getEntityOneEntityOneFieldWritePermission(entityName, fieldName))
				.isEqualTo("itdoes:entity:" + entityName + ":field:" + fieldName + ":write");
	}
}
