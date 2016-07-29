package com.itdoes.common.jaxb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itdoes.common.jaxb.JaxbMapperTest.HouseMapAdapter.HouseMap.HouseEntry;

/**
 * @author Jalen Zhong
 */
public class JaxbMapperTest {
	@Test
	public void toXml() throws DocumentException {
		User user = new User();
		user.setId(1L);
		user.setName("jalen");
		user.getInterests().add("movie");
		user.getInterests().add("sports");

		String xml = JaxbMapper.toXml(user, "UTF-8");
		assertXml(xml);
	}

	@Test
	public void toXmlWithListAsRoot() throws DocumentException {
		User user1 = new User();
		user1.setId(1L);
		user1.setName("jalen");
		User user2 = new User();
		user2.setId(2L);
		user2.setName("jie");
		List<User> userList = Lists.newArrayList(user1, user2);

		String xml = JaxbMapper.toXml(userList, "userList", User.class, "UTF-8");
		assertXmlWithListAsRoot(xml);
	}

	@Test
	public void fromXml() {
		String xml = generateXml();

		User user = JaxbMapper.fromXml(xml, User.class);
		assertThat(user.getId()).isEqualTo(1L);
		assertThat(user.getInterests()).containsOnly("movie", "sports");
	}

	@SuppressWarnings("unchecked")
	private static void assertXml(String xml) throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);

		Element user = doc.getRootElement();
		assertThat(user.attribute("id").getValue()).isEqualTo("1");

		Element interests = (Element) doc.selectSingleNode("//interests");
		assertThat(interests.elements()).hasSize(2);
		assertThat(((Element) interests.elements().get(0)).getText()).isEqualTo("movie");
	}

	@SuppressWarnings("unchecked")
	private static void assertXmlWithListAsRoot(String xml) throws DocumentException {
		Document doc = DocumentHelper.parseText(xml);

		Element userList = doc.getRootElement();
		assertThat(userList.elements()).hasSize(2);
		Element user1 = (Element) userList.elements().get(0);
		assertThat(user1.attribute("id").getValue()).isEqualTo("1");
		Element user2 = (Element) userList.elements().get(1);
		assertThat(user2.attribute("id").getValue()).isEqualTo("2");
	}

	private static String generateXml() {
		Document doc = DocumentHelper.createDocument();

		Element root = doc.addElement("user").addAttribute("id", "1");
		root.addElement("name").setText("jalen");
		Element interests = root.addElement("interests");
		interests.addElement("interest").addText("movie");
		interests.addElement("interest").addText("sports");

		return doc.asXML();
	}

	@XmlRootElement
	@XmlType(propOrder = { "name", "roles", "interests", "houses" })
	public static class User {
		private Long id;
		private String name;
		private String password;

		private List<Role> roles = Lists.newArrayList();
		private List<String> interests = Lists.newArrayList();
		private Map<String, String> houses = Maps.newHashMap();

		@XmlAttribute
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@XmlTransient
		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		@XmlElementWrapper
		@XmlElement(name = "role")
		public List<Role> getRoles() {
			return roles;
		}

		public void setRoles(List<Role> roles) {
			this.roles = roles;
		}

		@XmlElementWrapper(name = "interests")
		@XmlElement(name = "interest")
		public List<String> getInterests() {
			return interests;
		}

		public void setInterests(List<String> interests) {
			this.interests = interests;
		}

		@XmlJavaTypeAdapter(HouseMapAdapter.class)
		public Map<String, String> getHouses() {
			return houses;
		}

		public void setHouses(Map<String, String> houses) {
			this.houses = houses;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
	}

	public static class Role {
		private Long id;
		private String name;

		public Role() {
		}

		public Role(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		@XmlAttribute
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		@XmlAttribute
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
	}

	public static class HouseMapAdapter extends XmlAdapter<HouseMapAdapter.HouseMap, Map<String, String>> {
		@Override
		public HouseMap marshal(Map<String, String> map) throws Exception {
			HouseMap houseMap = new HouseMap();
			for (Entry<String, String> e : map.entrySet()) {
				houseMap.entries.add(new HouseEntry(e));
			}
			return houseMap;
		}

		@Override
		public Map<String, String> unmarshal(HouseMap houseMap) throws Exception {
			Map<String, String> map = Maps.newLinkedHashMap();
			for (HouseEntry e : houseMap.entries) {
				map.put(e.key, e.value);
			}
			return map;
		}

		@XmlType(name = "houses")
		public static class HouseMap {
			@XmlElement(name = "house")
			private List<HouseEntry> entries = Lists.newArrayList();

			static class HouseEntry {
				@XmlAttribute
				private String key;

				@XmlValue
				private String value;

				public HouseEntry() {
				}

				public HouseEntry(Entry<String, String> e) {
					key = e.getKey();
					value = e.getValue();
				}
			}
		}
	}
}
