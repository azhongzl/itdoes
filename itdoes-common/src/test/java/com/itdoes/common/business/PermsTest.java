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

		assertThat(Perms.getResourceAllPerm("myResource")).isEqualTo("itdoes:myResource");
		assertThat(Perms.getResourcePerm("myResource", "myCommand")).isEqualTo("itdoes:myResource:myCommand");

		String entityName = "MyEntity";
		String fieldName = "myField";
		assertThat(Perms.getEntityAllFieldsAllPerm(entityName))
				.isEqualTo("itdoes:entity:" + entityName + ":field");
		assertThat(Perms.getEntityOneFieldAllPerm(entityName, fieldName))
				.isEqualTo("itdoes:entity:" + entityName + ":field:" + fieldName);
		assertThat(Perms.getEntityOneFieldReadPerm(entityName, fieldName))
				.isEqualTo("itdoes:entity:" + entityName + ":field:" + fieldName + ":read");
		assertThat(Perms.getEntityOneFieldWritePerm(entityName, fieldName))
				.isEqualTo("itdoes:entity:" + entityName + ":field:" + fieldName + ":write");
	}
}
