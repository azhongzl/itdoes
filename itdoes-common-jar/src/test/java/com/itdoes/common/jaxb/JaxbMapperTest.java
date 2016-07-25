package com.itdoes.common.jaxb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import com.google.common.collect.Lists;

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
	@XmlType(propOrder = { "name", "interests" })
	private static class User {
		private Long id;
		private String name;
		private String password;
		private List<String> interests = Lists.newArrayList();

		@XmlAttribute
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		@SuppressWarnings("unused")
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

		@SuppressWarnings("unused")
		public void setPassword(String password) {
			this.password = password;
		}

		@XmlElementWrapper(name = "interests")
		@XmlElement(name = "interest")
		public List<String> getInterests() {
			return interests;
		}

		@SuppressWarnings("unused")
		public void setInterests(List<String> interests) {
			this.interests = interests;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
	}
}
