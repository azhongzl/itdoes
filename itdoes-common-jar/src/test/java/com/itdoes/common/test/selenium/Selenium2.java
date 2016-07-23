package com.itdoes.common.test.selenium;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class Selenium2 {
	private static final int DEFAULT_TIMEOUT = 20;

	private static final Logger LOGGER = LoggerFactory.getLogger(Selenium2.class);

	private final WebDriver driver;
	private final String baseUrl;
	private final int defaultTimeout;

	public Selenium2(WebDriver driver, String baseUrl, int defaultTimeout) {
		this.driver = driver;
		this.baseUrl = baseUrl;
		this.defaultTimeout = defaultTimeout;
		driver.manage().timeouts().implicitlyWait(defaultTimeout, TimeUnit.SECONDS);
	}

	public Selenium2(WebDriver driver, String baseUrl) {
		this(driver, baseUrl, DEFAULT_TIMEOUT);
	}

	public Selenium2(WebDriver driver, int defaultTimeout) {
		this(driver, "", defaultTimeout);
	}

	public void setStopAtShutdown() {
		Runtime.getRuntime().addShutdownHook(new Thread("Selenium Shutdown Hook") {
			@Override
			public void run() {
				quit();
			}
		});
	}

	public void open(String url) {
		final String realUrl = url.indexOf("://") == -1 ? baseUrl + (url.startsWith("/") ? "" : "/") + url : url;
		driver.get(realUrl);
	}

	public String getLocation() {
		return driver.getCurrentUrl();
	}

	public void back() {
		driver.navigate().back();
	}

	public void refresh() {
		driver.navigate().refresh();
	}

	public String getTitle() {
		return driver.getTitle();
	}

	public void quit() {
		try {
			driver.quit();
		} catch (Exception e) {
			LOGGER.error("Error in quiting selenium", e);
		}
	}

	public WebDriver getDriver() {
		return driver;
	}

	public WebElement findElement(By by) {
		return driver.findElement(by);
	}

	public List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	public boolean isPresent(By by) {
		try {
			findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public boolean isVisible(By by) {
		return findElement(by).isDisplayed();
	}

	public void type(By by, String text) {
		final WebElement element = findElement(by);
		element.clear();
		element.sendKeys(text);
	}

	public void click(By by) {
		findElement(by).click();
	}

	public void check(By by) {
		final WebElement element = findElement(by);
		if (!element.isSelected()) {
			element.click();
		}
	}

	public void uncheck(By by) {
		final WebElement element = findElement(by);
		if (element.isSelected()) {
			element.click();
		}
	}

	public boolean isChecked(By by) {
		return findElement(by).isSelected();
	}

	public Select getSelect(By by) {
		return new Select(findElement(by));
	}

	public String getText(By by) {
		return findElement(by).getText();
	}

	public String getValue(By by) {
		return findElement(by).getAttribute("value");
	}

	public void screenshot(String path, String outputFilename) {
		final File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		final File targetFile = new File(path, outputFilename);
		try {
			FileUtils.copyFile(srcFile, targetFile);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public void waitTitleIs(String title) {
		waitTitleIs(title, defaultTimeout);
	}

	public void waitTitleIs(String title, int timeout) {
		waitCondition(ExpectedConditions.titleIs(title), timeout);
	}

	public void waitTitleContains(String title) {
		waitTitleContains(title, defaultTimeout);
	}

	public void waitTitleContains(String title, int timeout) {
		waitCondition(ExpectedConditions.titleContains(title), timeout);
	}

	public void waitVisible(By by) {
		waitVisible(by, defaultTimeout);
	}

	public void waitVisible(By by, int timeout) {
		waitCondition(ExpectedConditions.visibilityOfElementLocated(by), timeout);
	}

	public void waitTextPresent(By by, String text) {
		waitTextPresent(by, text, defaultTimeout);
	}

	public void waitTextPresent(By by, String text, int timeout) {
		waitCondition(ExpectedConditions.textToBePresentInElementLocated(by, text), timeout);
	}

	public void waitValuePresent(By by, String value) {
		waitValuePresent(by, value, defaultTimeout);
	}

	public void waitValuePresent(By by, String value, int timeout) {
		waitCondition(ExpectedConditions.textToBePresentInElementValue(by, value), timeout);
	}

	public void waitCondition(ExpectedCondition<?> condition, int timeout) {
		(new WebDriverWait(driver, timeout)).until(condition);
	}

	public boolean isTextPresent(String text) {
		final String bodyText = findElement(By.tagName("body")).getText();
		return bodyText.contains(text);
	}

	public String getTable(By by, int rowIndex, int columnIndex) {
		return findElement(by).findElement(By.xpath("//tr[" + (rowIndex + 1) + "]//td[" + (columnIndex + 1) + "]"))
				.getText();
	}
}
