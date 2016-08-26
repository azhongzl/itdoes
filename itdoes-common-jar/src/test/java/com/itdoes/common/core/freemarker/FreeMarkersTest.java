package com.itdoes.common.core.freemarker;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

/**
 * @author Jalen Zhong
 */
public class FreeMarkersTest {
	@Test
	public void render() {
		Map<String, String> model = Maps.newHashMap();
		model.put("username", "Jalen");
		String result = FreeMarkers.render("Hello ${username}", model);
		assertThat(result).isEqualTo("Hello Jalen");

		Map<String, Object> modelList = Maps.newHashMap();
		List<String> friends = Lists.newArrayList("Jalen", "Jie", "Other");
		modelList.put("friends", friends);
		assertThat(FreeMarkers.render("Hello ${friends[0]}", modelList)).isEqualTo("Hello Jalen");
	}

	@Test(expected = RuntimeException.class)
	public void renderWithErrorTemplate() {
		Map<String, String> model = Maps.newHashMap();
		model.put("username", "Jalen");
		FreeMarkers.render("Hello ${}", model);
	}

	@Test
	public void renderFile()
			throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
		Map<String, String> model = Maps.newHashMap();
		model.put("username", "Jalen");
		Configuration cfg = FreeMarkers.buildConfiguration("classpath:/");
		Template template = cfg.getTemplate("testTemplate.ftl");
		String result = FreeMarkers.render(template, model);
		assertThat(result).isEqualTo("Hello Jalen");
	}
}
