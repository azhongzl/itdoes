package com.itdoes.common.core.test.business.functional;

import org.junit.Rule;
import org.junit.rules.TestRule;

import com.itdoes.common.core.selenium.Selenium2;
import com.itdoes.common.core.test.selenium.SeleniumScreenshotRule;

/**
 * @author Jalen Zhong
 */
public abstract class BaseSeleniumTestCase extends BaseFunctionalTestCase {
	protected static final Selenium2 s = new Selenium2(PL.getProperty("selenium.driver"), URL_BASE,
			PL.getInteger("selenium.timeout", -1));

	@Rule
	public TestRule screenshotRule = new SeleniumScreenshotRule(s);
}
