package com.itdoes.common.test.selenium;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.openqa.selenium.WebDriver;

/**
 * @author Jalen Zhong
 */
public abstract class BaseSeleniumTestCase {
	protected static Selenium2 s;

	protected static void initSelenium(String driverName, String baseUrl) {
		if (s != null) {
			return;
		}

		final WebDriver driver = WebDrivers.createDriver(driverName);
		s = new Selenium2(driver, baseUrl);
		s.setStopAtShutdown();
	}

	@Rule
	public TestRule screenshotRule = new SeleniumScreenshotRule(s);
}
