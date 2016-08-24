package com.itdoes.common.selenium;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.itdoes.common.shutdownhook.ShutdownThread;
import com.itdoes.common.shutdownhook.ShutdownThread.ShutdownCallback;
import com.itdoes.common.util.Exceptions;
import com.itdoes.common.util.Urls;

/**
 * @author Jalen Zhong
 */
public class Selenium2 {
	private static final int DEFAULT_TIMEOUT = 5;

	private static final Logger LOGGER = LoggerFactory.getLogger(Selenium2.class);

	private static ExpectedCondition<Boolean> textToBePresentInElementLocatedNotBlank(final By locator) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					String elementText = findElement(locator, driver).getText();
					return StringUtils.isNotBlank(elementText);
				} catch (StaleElementReferenceException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return String.format("text to be present in element found by %s", locator);
			}
		};
	}

	private static ExpectedCondition<Boolean> textToBePresentInElementValueNotBlank(final By locator) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					String elementText = findElement(locator, driver).getAttribute("value");
					return StringUtils.isNotBlank(elementText);
				} catch (StaleElementReferenceException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return String.format("text to be the value of element located by %s", locator);
			}
		};
	}

	private static WebElement findElement(By by, WebDriver driver) {
		try {
			return driver.findElement(by);
		} catch (NoSuchElementException e) {
			throw e;
		} catch (WebDriverException e) {
			LOGGER.warn(String.format("WebDriverException thrown by findElement(%s)", by), e);
			throw e;
		}
	}

	private final WebDriver driver;
	private final String baseUrl;
	private final int defaultTimeout;

	private boolean stopAtShutdown;

	public Selenium2(WebDriver driver, String baseUrl, int inputDefaultTimeout) {
		this.driver = driver;
		this.baseUrl = baseUrl;
		this.defaultTimeout = inputDefaultTimeout > 0 ? inputDefaultTimeout : DEFAULT_TIMEOUT;
		driver.manage().timeouts().implicitlyWait(this.defaultTimeout, TimeUnit.SECONDS);

		setStopAtShutdown(true);
	}

	public Selenium2(WebDriver driver, String baseUrl) {
		this(driver, baseUrl, DEFAULT_TIMEOUT);
	}

	public Selenium2(WebDriver driver, int defaultTimeout) {
		this(driver, "", defaultTimeout);
	}

	public void open(String url) {
		final String realUrl = Urls.concat(baseUrl, url);
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
		return findElement(by, driver);
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
		actStaleElement(new Action() {
			@Override
			public void act() {
				typeInternal(by, text);
			}
		});
	}

	private void typeInternal(By by, String text) {
		final WebElement element = findElement(by);
		element.clear();
		element.sendKeys(text);
	}

	public void ctrlC(By by) {
		type(by, Keys.CONTROL + "c");
	}

	public void ctrlV(By by) {
		type(by, Keys.CONTROL + "v");
	}

	public void click(By by) {
		actStaleElement(new Action() {
			@Override
			public void act() {
				clickInternal(by);
			}
		});
	}

	private void clickInternal(By by) {
		findElement(by).click();
	}

	public void check(By by) {
		final WebElement element = findElement(by);
		if (!element.isSelected()) {
			click(by);
		}
	}

	public void uncheck(By by) {
		final WebElement element = findElement(by);
		if (element.isSelected()) {
			click(by);
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
		waitUntil(ExpectedConditions.titleIs(title), timeout);
	}

	public void waitTitleContains(String title) {
		waitTitleContains(title, defaultTimeout);
	}

	public void waitTitleContains(String title, int timeout) {
		waitUntil(ExpectedConditions.titleContains(title), timeout);
	}

	public WebElement waitVisible(By by) {
		return waitVisible(by, defaultTimeout);
	}

	public WebElement waitVisible(By by, int timeout) {
		return waitUntil(ExpectedConditions.visibilityOfElementLocated(by), timeout);
	}

	public void waitTextPresent(By by, String text) {
		waitTextPresent(by, text, defaultTimeout);
	}

	public void waitTextPresent(By by, String text, int timeout) {
		waitUntil(ExpectedConditions.textToBePresentInElementLocated(by, text), timeout);
	}

	public void waitTextNotBlank(By by) {
		waitTextNotBlank(by, defaultTimeout);
	}

	public void waitTextNotBlank(By by, int timeout) {
		waitUntil(textToBePresentInElementLocatedNotBlank(by), timeout);
	}

	public void waitValuePresent(By by, String value) {
		waitValuePresent(by, value, defaultTimeout);
	}

	public void waitValuePresent(By by, String value, int timeout) {
		waitUntil(ExpectedConditions.textToBePresentInElementValue(by, value), timeout);
	}

	public void waitValueNotBlank(By by) {
		waitValueNotBlank(by, defaultTimeout);
	}

	public void waitValueNotBlank(By by, int timeout) {
		waitUntil(textToBePresentInElementValueNotBlank(by), timeout);
	}

	public Alert checkAlert() {
		return checkAlert(defaultTimeout);
	}

	public Alert checkAlert(int timeout) {
		try {
			return getAlert(timeout);
		} catch (TimeoutException e) {
			return null;
		}
	}

	public Alert getAlert() {
		return getAlert(defaultTimeout);
	}

	public Alert getAlert(int timeout) {
		return waitUntil(ExpectedConditions.alertIsPresent(), timeout);
	}

	public boolean isTextPresent(String text) {
		final String bodyText = findElement(By.tagName("body")).getText();
		return bodyText.contains(text);
	}

	public String getCell(By by, int rowIndex, int columnIndex) {
		return findElement(by).findElement(By.xpath("//tr[" + (rowIndex + 1) + "]//td[" + (columnIndex + 1) + "]"))
				.getText();
	}

	public static interface NewWindowAction {
		NewWindowAction NOP = new NewWindowAction() {
			@Override
			public void actInNewWindow() {
			}
		};

		void actInNewWindow();
	}

	public void actInNewWindow(NewWindowAction action) {
		final String parentWindowHandler = driver.getWindowHandle();

		for (String windowHandler : driver.getWindowHandles()) {
			driver.switchTo().window(windowHandler);
		}

		action.actInNewWindow();

		driver.close();

		driver.switchTo().window(parentWindowHandler);
	}

	public void closeNewWindow() {
		actInNewWindow(NewWindowAction.NOP);
	}

	private WebDriverWait waiter(int timeout) {
		return new WebDriverWait(driver, timeout);
	}

	private <T> T waitUntil(ExpectedCondition<T> condition, int timeout) {
		return waiter(timeout).until(condition);
	}

	private void actStaleElement(Action action) {
		waiter(defaultTimeout).ignoring(StaleElementReferenceException.class).until(new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver driver) {
				action.act();

				return true;
			}
		});
	}

	private static interface Action {
		void act();
	}

	private void setStopAtShutdown(boolean stop) {
		// if we now want to stop
		if (stop) {
			// and we weren't stopping before
			if (!stopAtShutdown) {
				ShutdownThread.getInstance().register(this, new ShutdownCallback() {
					@Override
					public void shutdown() {
						quit();
					}
				});
			}
		} else {
			if (stopAtShutdown) {
				ShutdownThread.getInstance().unregister(this);
			}
		}
		stopAtShutdown = stop;
	}
}
