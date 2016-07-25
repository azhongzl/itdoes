package com.itdoes.common.jackson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author Jalen Zhong
 */
public class JsonMapperTest {
	private static final JsonMapper NON_DEFAULT = JsonMapperBuilder.newBuilder().nonDefault().build();
	private static final JsonMapper NON_EMPTY = JsonMapperBuilder.newBuilder().nonEmpty().build();
	private static final JsonMapper ALL = JsonMapperBuilder.newBuilder().build();

	@Test
	public void toJson() {
		TestBean bean = new TestBean("A");
		String beanString = NON_DEFAULT.toJson(bean);
		assertThat(beanString).isEqualTo("{\"name\":\"A\"}");

		Map<String, Object> map = Maps.newLinkedHashMap();
		map.put("name", "A");
		map.put("age", 2);
		String mapString = NON_DEFAULT.toJson(map);
		assertThat(mapString).isEqualTo("{\"name\":\"A\",\"age\":2}");

		List<String> stringList = Lists.newArrayList("A", "B", "C");
		String listString = NON_DEFAULT.toJson(stringList);
		assertThat(listString).isEqualTo("[\"A\",\"B\",\"C\"]");

		List<TestBean> beanList = Lists.newArrayList(new TestBean("A"), new TestBean("B"));
		String beanListString = NON_DEFAULT.toJson(beanList);
		assertThat(beanListString).isEqualTo("[{\"name\":\"A\"},{\"name\":\"B\"}]");

		TestBean[] beanArray = new TestBean[] { new TestBean("A"), new TestBean("B") };
		String beanArrayString = NON_DEFAULT.toJson(beanArray);
		assertThat(beanArrayString).isEqualTo("[{\"name\":\"A\"},{\"name\":\"B\"}]");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void fromJson() {
		String beanString = "{\"name\":\"A\"}";
		TestBean bean = NON_DEFAULT.fromJson(beanString, TestBean.class);
		assertThat(bean.getName()).isEqualTo("A");

		String mapString = "{\"name\":\"A\",\"age\":2}";
		Map<String, Object> map = NON_DEFAULT.fromJson(mapString, Map.class);
		assertThat(map).contains(entry("name", "A"), entry("age", 2));

		String listString = "[\"A\",\"B\",\"C\"]";
		List<String> stringList = NON_DEFAULT.fromJson(listString, List.class);
		assertThat(stringList).contains("A", "B", "C");

		String beanListString = "[{\"name\":\"A\"},{\"name\":\"B\"}]";
		List<TestBean> beanList = NON_DEFAULT.fromJson(beanListString,
				NON_DEFAULT.buildCollectionType(List.class, TestBean.class));
		assertThat(beanList).hasSize(2);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void nullEmpty() {
		TestBean nullBean = null;
		String nullBeanString = NON_DEFAULT.toJson(nullBean);
		assertThat(nullBeanString).isEqualTo("null");

		List<String> emptyList = Lists.newArrayList();
		String emptyListString = NON_DEFAULT.toJson(emptyList);
		assertThat(emptyListString).isEqualTo("[]");

		TestBean nullBeanResult = NON_DEFAULT.fromJson(null, TestBean.class);
		assertThat(nullBeanResult).isNull();

		nullBeanResult = NON_DEFAULT.fromJson("null", TestBean.class);
		assertThat(nullBeanResult).isNull();

		List nullListResult = NON_DEFAULT.fromJson(null, List.class);
		assertThat(nullListResult).isNull();

		nullListResult = NON_DEFAULT.fromJson("null", List.class);
		assertThat(nullListResult).isNull();

		nullListResult = NON_DEFAULT.fromJson("[]", List.class);
		assertThat(nullListResult).isEmpty();
	}

	@Test
	public void inclusions() {
		TestBean bean = new TestBean("A");

		assertThat(ALL.toJson(bean)).isEqualTo("{\"name\":\"A\",\"defaultValue\":\"defaultValue\",\"nullValue\":null}");

		assertThat(NON_EMPTY.toJson(bean)).isEqualTo("{\"name\":\"A\",\"defaultValue\":\"defaultValue\"}");

		assertThat(NON_DEFAULT.toJson(bean)).isEqualTo("{\"name\":\"A\"}");
	}

	public static class TestBean {
		private String name;
		private String defaultValue = "defaultValue";
		private String nullValue = null;

		public TestBean() {
		}

		public TestBean(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		public String getNullValue() {
			return nullValue;
		}

		public void setNullValue(String nullValue) {
			this.nullValue = nullValue;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
	}
}
