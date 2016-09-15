package com.itdoes.common.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

import java.util.NoSuchElementException;
import java.util.Properties;

import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class PropertiesLoaderTest {
	@Test
	public void multiProperties() {
		Properties p = new PropertiesLoader("classpath:/test1.properties", "classpath:/test2.properties")
				.getProperties();
		assertThat(p.getProperty("p1")).isEqualTo("1");
		assertThat(p.getProperty("p2")).isEqualTo("22");
		assertThat(p.getProperty("p3")).isEqualTo("3");
	}

	@Test
	public void notExistProperty() {
		try {
			new PropertiesLoader("classpath:/notExist.properties");
			failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
		}

		PropertiesLoader pl = new PropertiesLoader("classpath:/test1.properties");

		try {
			pl.getStringMust("notExist");
			failBecauseExceptionWasNotThrown(NoSuchElementException.class);
		} catch (NoSuchElementException e) {
		}

		assertThat(pl.getStringMay("notExist", "defaultValue")).isEqualTo("defaultValue");
	}

	@Test
	public void primitiveTypes() {
		PropertiesLoader pl = new PropertiesLoader("classpath:/test1.properties", "classpath:/test2.properties");

		assertThat(pl.getIntegerMust("p1")).isEqualTo(1);
		try {
			pl.getIntegerMust("notExist");
			failBecauseExceptionWasNotThrown(NoSuchElementException.class);
		} catch (NoSuchElementException e) {
		}
		assertThat(pl.getIntegerMay("notExist", 100)).isEqualTo(100);

		assertThat(pl.getLongMust("p1")).isEqualTo(1L);
		try {
			pl.getLongMust("notExist");
			failBecauseExceptionWasNotThrown(NoSuchElementException.class);
		} catch (NoSuchElementException e) {
		}
		assertThat(pl.getLongMay("notExist", 100L)).isEqualTo(100L);

		assertThat(pl.getDoubleMust("p1")).isEqualTo(1D);
		try {
			pl.getDoubleMust("notExist");
			failBecauseExceptionWasNotThrown(NoSuchElementException.class);
		} catch (NoSuchElementException e) {
		}
		assertThat(pl.getDoubleMay("notExist", 100D)).isEqualTo(100D);

		assertThat(pl.getBooleanMust("p4")).isEqualTo(true);
		try {
			pl.getBooleanMust("notExist");
			failBecauseExceptionWasNotThrown(NoSuchElementException.class);
		} catch (NoSuchElementException e) {
		}
		assertThat(pl.getBooleanMay("notExist", true)).isEqualTo(true);
	}

	@Test
	public void systemProperty() {
		System.setProperty("p1", "sys");

		PropertiesLoader plDefault = new PropertiesLoader("classpath:/test1.properties", "classpath:/test2.properties");
		PropertiesLoader plPreferSystem = new PropertiesLoader(true, false, "classpath:/test1.properties",
				"classpath:/test2.properties");
		PropertiesLoader plPreferLocal = new PropertiesLoader(false, false, "classpath:/test1.properties",
				"classpath:/test2.properties");

		assertThat(plDefault.getStringMust("p1")).isEqualTo("sys");
		assertThat(plPreferSystem.getStringMust("p1")).isEqualTo("sys");
		assertThat(plPreferLocal.getStringMust("p1")).isEqualTo("1");

		System.clearProperty("p1");
	}
}
