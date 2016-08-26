package com.itdoes.common.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class ReflectionsTest {
	@Test
	public void getAndSetFieldValue() {
		ChildBean bean = new ChildBean();

		assertThat(Reflections.getFieldValue(bean, "propField")).isEqualTo(1);
		assertThat(Reflections.getFieldValue(bean, "nonPropField")).isEqualTo(1);

		Reflections.setFieldValue(bean, "propField", 2);
		assertThat(bean.viewPropField()).isEqualTo(2);

		Reflections.setFieldValue(bean, "nonPropField", 2);
		assertThat(bean.viewNonPropField()).isEqualTo(2);

		try {
			Reflections.getFieldValue(bean, "notExist");
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
		}

		try {
			Reflections.setFieldValue(bean, "notExist", 2);
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void invokeGetAndSet() {
		ChildBean bean = new ChildBean();

		assertThat(Reflections.invokeGet(bean, "propField")).isEqualTo(bean.viewPropField() + 1);

		bean = new ChildBean();
		Reflections.invokeSet(bean, "propField", 10);
		assertThat(bean.viewPropField()).isEqualTo(10 + 1);
	}

	@Test
	public void invokeMethod() {
		ChildBean bean = new ChildBean();

		assertThat(
				Reflections.invokeMethod(bean, "privateMethod", new Class[] { String.class }, new Object[] { "Jalen" }))
						.isEqualTo("Hello Jalen");

		assertThat(Reflections.invokeMethodByName(bean, "privateMethod", new Object[] { "Jalen" }))
				.isEqualTo("Hello Jalen");

		try {
			Reflections.invokeMethod(bean, "notExistMethod", new Class[] { String.class }, new Object[] { "Jalen" });
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
		}

		try {
			Reflections.invokeMethod(bean, "privateMethod", new Class[] { Integer.class }, new Object[] { "Jalen" });
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
		}

		try {
			Reflections.invokeMethodByName(bean, "notExistMethod", new Object[] { "Jalen" });
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void getGenericType() {
		assertThat(Reflections.getGenericType(ChildBean.class)).isEqualTo(String.class);
		assertThat(Reflections.getGenericType(ChildBean.class, 1)).isEqualTo(Long.class);

		assertThat(Reflections.getGenericType(ChildBean.class, -1)).isEqualTo(Object.class);
		assertThat(Reflections.getGenericType(ChildBean.class, 10)).isEqualTo(Object.class);
		assertThat(Reflections.getGenericType(ChildBeanNonGeneric.class)).isEqualTo(Object.class);
		assertThat(Reflections.getGenericType(OtherBean.class)).isEqualTo(Object.class);
	}

	public static class ParentBean<T, ID> {
	}

	public static class ChildBean extends ParentBean<String, Long> {
		private int propField = 1;
		private int nonPropField = 1;

		public int getPropField() {
			return propField + 1;
		}

		public void setPropField(int propField) {
			this.propField = propField + 1;
		}

		public int viewPropField() {
			return propField;
		}

		public int viewNonPropField() {
			return nonPropField;
		}

		@SuppressWarnings("unused")
		private String privateMethod(String name) {
			return "Hello " + name;
		}
	}

	@SuppressWarnings("rawtypes")
	public static class ChildBeanNonGeneric extends ParentBean {
	}

	public static class OtherBean {
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}
}
