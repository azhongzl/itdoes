package com.itdoes.common.core.test.functional;

import org.junit.Rule;
import org.junit.rules.TestRule;

import com.itdoes.common.core.selenium.Selenium2;
import com.itdoes.common.core.test.selenium.SeleniumScreenshotRule;

/**
 * @author Jalen Zhong
 */
public abstract class BaseSeleniumTestCase extends BaseFunctionalTestCase {
	protected static final Selenium2 s = new Selenium2(PL.getStringMust("selenium.driver"), URL_BASE,
			PL.getIntegerMay("selenium.timeout", -1));

	@Rule
	public TestRule screenshotRule = new SeleniumScreenshotRule(s);
}
