package com.itdoes.common.core.selenium;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.itdoes.common.core.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class WebDrivers {
	public static enum BrowserType {
		firefox, ie, chrome, remote
	}

	public static WebDriver createDriver(String driverName) {
		if (BrowserType.firefox.name().equals(driverName)) {
			return new FirefoxDriver();
		} else if (BrowserType.ie.name().equals(driverName)) {
			return new InternetExplorerDriver();
		} else if (BrowserType.chrome.name().equals(driverName)) {
			return new ChromeDriver();
		} else if (driverName.startsWith(BrowserType.remote.name())) {
			final String[] params = StringUtils.split(driverName, ":");
			Validate.isTrue(params.length == 4,
					"Remote driver is wrong, acceptible format is \"remote:localhost:4444:firefox\", but the actual is \""
							+ driverName + "\"");
			final String remoteHost = params[1];
			final String remotePort = params[2];
			final String driverType = params[3];
			final String remoteUrl = "http://" + remoteHost + ":" + remotePort + "/wd/hub";

			DesiredCapabilities cap = null;
			if (BrowserType.firefox.name().equals(driverType)) {
				cap = DesiredCapabilities.firefox();
			} else if (BrowserType.ie.name().equals(driverType)) {
				cap = DesiredCapabilities.internetExplorer();
			} else if (BrowserType.chrome.name().equals(driverType)) {
				cap = DesiredCapabilities.chrome();
			}
			if (cap != null) {
				try {
					return new RemoteWebDriver(new URL(remoteUrl), cap);
				} catch (MalformedURLException e) {
					throw Exceptions.unchecked(e);
				}
			}
		}

		throw new IllegalArgumentException("Create WebDriver failed for \"\"" + driverName + "\"");
	}

	private WebDrivers() {
	}
}
