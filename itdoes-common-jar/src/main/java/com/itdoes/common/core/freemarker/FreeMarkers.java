package com.itdoes.common.core.freemarker;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.itdoes.common.core.util.Exceptions;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author Jalen Zhong
 */
public class FreeMarkers {
	private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

	public static String render(String templateString, Object model) {
		try {
			final Template template = new Template("default", new StringReader(templateString),
					new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));
			return render(template, model);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}

	}

	public static String render(Template template, Object model) {
		try {
			final StringWriter writer = new StringWriter();
			template.process(model, writer);
			return writer.toString();
		} catch (TemplateException | IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static Configuration buildConfiguration(String directory) {
		try {
			final Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
			Resource resource = RESOURCE_LOADER.getResource(directory);
			cfg.setDirectoryForTemplateLoading(resource.getFile());
			return cfg;
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	private FreeMarkers() {
	}
}
