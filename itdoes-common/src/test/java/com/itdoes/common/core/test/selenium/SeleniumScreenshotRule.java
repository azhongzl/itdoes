package com.itdoes.common.core.test.selenium;

import org.apache.commons.lang3.Validate;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.itdoes.common.core.selenium.Selenium2;

/**
 * @author Jalen Zhong
 */
public class SeleniumScreenshotRule extends TestWatcher {
	private static final String PATH = "target/screenshot/";

	private final Selenium2 s;

	public SeleniumScreenshotRule(Selenium2 s) {
		Validate.notNull(s, "Selenium2 is null");

		this.s = s;
	}

	@Override
	protected void failed(Throwable t, Description description) {
		final String outputFilename = description.getClassName() + "_" + description.getMethodName() + ".png";
		s.screenshot(PATH, outputFilename);
	}
}
