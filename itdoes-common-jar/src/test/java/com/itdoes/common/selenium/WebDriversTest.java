package com.itdoes.common.selenium;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Jalen Zhong
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ WebDrivers.class, FirefoxDriver.class, InternetExplorerDriver.class, ChromeDriver.class,
		RemoteWebDriver.class })
public class WebDriversTest {
	@Mock
	FirefoxDriver firefox;
	@Mock
	InternetExplorerDriver ie;
	@Mock
	ChromeDriver chrome;
	@Mock
	RemoteWebDriver remote;

	@Test
	public void createWebDriver() throws Exception {
		MockitoAnnotations.initMocks(this);

		PowerMockito.whenNew(FirefoxDriver.class).withNoArguments().thenReturn(firefox);
		WebDriver driver = WebDrivers.createDriver("firefox");
		assertThat(driver).isInstanceOf(FirefoxDriver.class);
		assertThat(driver).isSameAs(firefox);

		PowerMockito.whenNew(InternetExplorerDriver.class).withNoArguments().thenReturn(ie);
		driver = WebDrivers.createDriver("ie");
		assertThat(driver).isInstanceOf(InternetExplorerDriver.class);
		assertThat(driver).isSameAs(ie);

		PowerMockito.whenNew(ChromeDriver.class).withNoArguments().thenReturn(chrome);
		driver = WebDrivers.createDriver("chrome");
		assertThat(driver).isInstanceOf(ChromeDriver.class);
		assertThat(driver).isSameAs(chrome);

		PowerMockito.whenNew(RemoteWebDriver.class)
				.withArguments(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.firefox())
				.thenReturn(remote);
		driver = WebDrivers.createDriver("remote:localhost:4444:firefox");
		assertThat(driver).isInstanceOf(RemoteWebDriver.class);
		assertThat(driver).isSameAs(remote);
	}
}
