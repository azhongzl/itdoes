package com.itdoes.common.test.business.functional;

import org.junit.Rule;
import org.junit.rules.TestRule;

import com.itdoes.common.test.Tests;
import com.itdoes.common.test.selenium.Selenium2;
import com.itdoes.common.test.selenium.SeleniumScreenshotRule;

/**
 * @author Jalen Zhong
 */
public abstract class BaseSeleniumTestCase extends BaseFunctionalTestCase {
	protected static final Selenium2 s = Tests.createSelenium(PL.getProperty("selenium.driver"), URL_BASE,
			PL.getInteger("selenium.timeout", -1));

	@Rule
	public TestRule screenshotRule = new SeleniumScreenshotRule(s);
}
