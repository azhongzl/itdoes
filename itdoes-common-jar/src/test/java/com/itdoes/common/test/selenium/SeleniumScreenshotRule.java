package com.itdoes.common.test.selenium;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * @author Jalen Zhong
 */
public class SeleniumScreenshotRule extends TestWatcher {
	private static final String PATH = "target/screenshot/";

	private final Selenium2 s;

	public SeleniumScreenshotRule(Selenium2 s) {
		this.s = s;
	}

	@Override
	protected void failed(Throwable t, Description description) {
		final String outputFilename = description.getClassName() + "_" + description.getMethodName() + ".png";
		s.screenshot(PATH, outputFilename);
	}
}
