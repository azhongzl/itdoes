package com.itdoes.common.jackson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
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

	@Test
	public void withJsonAnnotation() {
		TestBeanWithJsonAnnotation bean = new TestBeanWithJsonAnnotation(1, "foo", 18);
		assertThat(NON_DEFAULT.toJson(bean)).isEqualTo("{\"productName\":\"foo\",\"id\":1}");
	}

	@JsonPropertyOrder({ "name", "id" })
	public static class TestBeanWithJsonAnnotation {
		public long id;
		@JsonProperty("productName")
		public String name;
		@JsonIgnore
		public int age;

		public TestBeanWithJsonAnnotation() {
		}

		public TestBeanWithJsonAnnotation(long id, String name, int age) {
			this.id = id;
			this.name = name;
			this.age = age;
		}
	}

	@Test
	public void withJaxbAnnotation() {
		JsonMapper mapper = JsonMapperBuilder.newBuilder().nonEmpty().registerModuleJaxb().build();
		TestBeanWithJaxbAnnotation bean = new TestBeanWithJaxbAnnotation(1, "foo", 18);
		assertThat(mapper.toJson(bean)).isEqualTo("{\"productName\":\"foo\",\"id\":1}");
	}

	@XmlType(propOrder = { "name", "id" })
	public static class TestBeanWithJaxbAnnotation {
		public long id;
		@XmlElement(name = "productName")
		public String name;
		@XmlTransient
		public int age;

		public TestBeanWithJaxbAnnotation() {
		}

		public TestBeanWithJaxbAnnotation(long id, String name, int age) {
			this.id = id;
			this.name = name;
			this.age = age;
		}
	}

	@Test
	public void updateBean() {
		String jsonString = "{\"name\":\"A\"}";
		TestBean bean = new TestBean();
		bean.setDefaultValue("newDefaultValue");
		NON_DEFAULT.update(jsonString, bean);
		assertThat(bean.getName()).isEqualTo("A");
		assertThat(bean.getDefaultValue()).isEqualTo("newDefaultValue");
	}

	@Test
	public void jsonp() {
		TestBean bean = new TestBean("foo");
		assertThat(NON_DEFAULT.toJsonP("callback", bean)).isEqualTo("callback({\"name\":\"foo\"})");
	}

	@Test
	public void enumType() {
		assertThat(NON_DEFAULT.toJson(TestEnum.One)).isEqualTo("\"One\"");
		assertThat(NON_DEFAULT.fromJson("\"One\"", TestEnum.class)).isEqualTo(TestEnum.One);
		assertThat(NON_DEFAULT.fromJson("0", TestEnum.class)).isEqualTo(TestEnum.One);

		JsonMapper mapper = JsonMapperBuilder.newBuilder().nonEmpty().enableEnumsUsingToString().build();
		assertThat(mapper.toJson(TestEnum.One)).isEqualTo("\"1\"");
		assertThat(mapper.fromJson("\"1\"", TestEnum.class)).isEqualTo(TestEnum.One);
	}

	public static enum TestEnum {
		One(1), Two(2), Three(3);
		private final int no;

		TestEnum(int no) {
			this.no = no;
		}

		@Override
		public String toString() {
			return String.valueOf(no);
		}
	}

	@Test
	public void dateType() throws ParseException {
		Date date = new Date();
		String timestampString = String.valueOf(date.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String formattedString = formatter.format(date);

		DateBean bean = new DateBean();
		bean.startDate = date;
		bean.endDate = date;

		String expectedJson = "{\"startDate\":" + timestampString + ",\"endDate\":\"" + formattedString + "\"}";
		assertThat(NON_DEFAULT.toJson(bean)).isEqualTo(expectedJson);

		Date expectedDate = formatter.parse(formattedString);
		DateBean resultBean = NON_DEFAULT.fromJson(expectedJson, DateBean.class);
		assertThat(resultBean.endDate).isEqualTo(expectedDate);
	}

	public static class DateBean {
		public Date startDate;
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
		public Date endDate;
	}

	@Test
	public void cycleReferenceBean() {
		CycleReferenceBean parent = new CycleReferenceBean("parent");

		CycleReferenceBean child1 = new CycleReferenceBean("child1");
		child1.setParent(parent);
		parent.getChildren().add(child1);

		CycleReferenceBean child2 = new CycleReferenceBean("child2");
		child2.setParent(parent);
		parent.getChildren().add(child2);

		String jsonString = "{\"name\":\"parent\",\"children\":[{\"name\":\"child1\"},{\"name\":\"child2\"}]}";
		assertThat(NON_DEFAULT.toJson(parent)).isEqualTo(jsonString);
		assertThat(NON_DEFAULT.toJson(child1)).isEqualTo("{\"name\":\"child1\"}");

		CycleReferenceBean parentResult = NON_DEFAULT.fromJson(jsonString, CycleReferenceBean.class);
		assertThat(parentResult.getChildren().get(0).getParent().getName()).isEqualTo(parent.getName());

		CycleReferenceBean child1Result = NON_DEFAULT.fromJson("{\"name\":\"child1\"}", CycleReferenceBean.class);
		assertThat(child1Result.getParent()).isNull();
		assertThat(child1Result.getName()).isEqualTo(child1.getName());
	}

	public static class CycleReferenceBean {
		private String name;
		private CycleReferenceBean parent;
		private List<CycleReferenceBean> children = Lists.newArrayList();

		public CycleReferenceBean() {
		}

		public CycleReferenceBean(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@JsonBackReference
		public CycleReferenceBean getParent() {
			return parent;
		}

		@JsonBackReference
		public void setParent(CycleReferenceBean parent) {
			this.parent = parent;
		}

		@JsonManagedReference
		public List<CycleReferenceBean> getChildren() {
			return children;
		}

		@JsonManagedReference
		public void setChildren(List<CycleReferenceBean> children) {
			this.children = children;
		}
	}

	@Test
	public void extensibleBean() {
		String jsonString = "{\"name\":\"Foo\",\"age\":40,\"occupation\":\"software engineer\"}";
		ExtensibleBean bean = NON_DEFAULT.fromJson(jsonString, ExtensibleBean.class);
		assertThat(bean.getName()).isEqualTo("Foo");
		assertThat(bean.getProperties().get("name")).isNull();
		assertThat(bean.getProperties().get("occupation")).isEqualTo("software engineer");
	}

	public static class ExtensibleBean {
		private String name;
		private final Map<String, String> properties = Maps.newHashMap();

		public ExtensibleBean() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@JsonAnySetter
		public void add(String key, String value) {
			properties.put(key, value);
		}

		@JsonAnyGetter
		public Map<String, String> getProperties() {
			return properties;
		}
	}

	@Test
	public void mutliViewBean() throws JsonProcessingException {
		MultiViewBean bean = new MultiViewBean();
		bean.setName("Foo");
		bean.setAge(40);
		bean.setOtherValue("other value");

		ObjectWriter publicWriter = NON_DEFAULT.getMapper().writerWithView(Views.Public.class);
		assertThat(publicWriter.writeValueAsString(bean))
				.isEqualTo("{\"name\":\"Foo\",\"otherValue\":\"other value\"}");

		ObjectWriter privateWriter = NON_DEFAULT.getMapper().writerWithView(Views.Private.class);
		assertThat(privateWriter.writeValueAsString(bean)).isEqualTo("{\"age\":40,\"otherValue\":\"other value\"}");
	}

	public static class Views {
		static class Public {
		}

		static class Private {
		}
	}

	public static class MultiViewBean {
		private String name;
		private int age;
		private String otherValue;

		@JsonView(Views.Public.class)
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@JsonView(Views.Private.class)
		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		public String getOtherValue() {
			return otherValue;
		}

		public void setOtherValue(String otherValue) {
			this.otherValue = otherValue;
		}
	}

	@Test
	public void customConverter() {
		SimpleModule module = new SimpleModule("MoneyModule");
		module.addSerializer(new MoneySerializer());
		module.addDeserializer(Money.class, new MoneyDeserializer());
		JsonMapper mapper = JsonMapperBuilder.newBuilder().nonDefault().registerModule(module).build();

		User user = new User();
		user.setName("Foo");
		user.setSalary(new Money(40.1));

		String jsonString = mapper.toJson(user);
		assertThat(jsonString).isEqualTo("{\"name\":\"Foo\",\"salary\":\"40.1\"}");

		User resultUser = mapper.fromJson(jsonString, User.class);
		assertThat(resultUser.getSalary().value).isEqualTo(user.getSalary().value);
	}

	public static class MoneySerializer extends StdSerializer<Money> {
		private static final long serialVersionUID = -2774049198965572234L;

		public MoneySerializer() {
			super(Money.class);
		}

		@Override
		public void serialize(Money money, JsonGenerator generator, SerializerProvider provider) throws IOException {
			generator.writeString(money.toString());
		}
	}

	public static class MoneyDeserializer extends StdDeserializer<Money> {
		private static final long serialVersionUID = -5259930632086209721L;

		public MoneyDeserializer() {
			super(Money.class);
		}

		@Override
		public Money deserialize(JsonParser parser, DeserializationContext context)
				throws IOException, JsonProcessingException {
			return Money.valueOf(parser.getText());
		}
	}

	public static class Money {
		public static Money valueOf(String value) {
			Double srcValue = Double.valueOf(value);
			return new Money(srcValue);
		}

		private final Double value;

		public Money(Double value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value.toString();
		}
	}

	public static class User {
		private String name;
		private Money salary;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Money getSalary() {
			return salary;
		}

		public void setSalary(Money salary) {
			this.salary = salary;
		}
	}

	@Test
	public void customPropertyNaming() {
		TestBean bean = new TestBean("Foo");
		bean.setDefaultValue("Bar");

		JsonMapper mapper = JsonMapperBuilder.newBuilder().nonDefault().build();
		mapper.getMapper().setPropertyNamingStrategy(new LowerCaseNaming());
		assertThat(mapper.toJson(bean)).isEqualTo("{\"name\":\"Foo\",\"defaultvalue\":\"Bar\"}");
	}

	public static class LowerCaseNaming extends PropertyNamingStrategy {
		private static final long serialVersionUID = -1186118678320440505L;

		@Override
		public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
			return defaultName.toLowerCase();
		}
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
