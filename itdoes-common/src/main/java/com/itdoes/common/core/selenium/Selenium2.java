package com.itdoes.common.core.selenium;

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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;
import com.itdoes.common.core.shutdownhook.ShutdownThread;
import com.itdoes.common.core.shutdownhook.ShutdownThread.ShutdownCallback;
import com.itdoes.common.core.util.Exceptions;
import com.itdoes.common.core.util.Urls;

/**
 * @author Jalen Zhong
 */
public class Selenium2 {
	private static final int DEFAULT_TIMEOUT = 5;

	private final WebDriver driver;
	private final String baseUrl;
	private final int defaultTimeout;

	private boolean stopAtShutdown;

	public Selenium2(String driverName, String baseUrl, int inputDefaultTimeout) {
		this(WebDrivers.createDriver(driverName), baseUrl, inputDefaultTimeout);
	}

	public Selenium2(WebDriver driver, String baseUrl, int inputDefaultTimeout) {
		this.driver = driver;
		this.baseUrl = baseUrl;
		this.defaultTimeout = inputDefaultTimeout > 0 ? inputDefaultTimeout : DEFAULT_TIMEOUT;
		driver.manage().timeouts().implicitlyWait(this.defaultTimeout, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		setStopAtShutdown(true);
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
		driver.quit();
	}

	public WebDriver getDriver() {
		return driver;
	}

	public WebElement findElementNoException(By by) {
		try {
			return findElement(by);
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public WebElement findElement(By by) {
		return driver.findElement(by);
	}

	public WebElement findElement(By by, int index) {
		return findElement(ByIndex.index(by, index));
	}

	public List<WebElement> findElements(By by) {
		return driver.findElements(by);
	}

	public boolean isVisible(By by) {
		return isVisible(findElement(by));
	}

	public boolean isVisible(By by, int index) {
		return isVisible(findElement(by, index));
	}

	public boolean isVisible(WebElement element) {
		return element.isDisplayed();
	}

	public void type(By by, String text) {
		type(findElement(by), text);
	}

	public void type(By by, int index, String text) {
		type(findElement(by, index), text);
	}

	public void type(WebElement element, String text) {
		actStaleElement(new Action() {
			@Override
			public void act() {
				typeInternal(element, text);
			}
		});
	}

	private void typeInternal(WebElement element, String text) {
		element.clear();
		element.sendKeys(text);
	}

	public void typeCtrlC(By by) {
		typeCtrlC(findElement(by));
	}

	public void typeCtrlC(By by, int index) {
		typeCtrlC(findElement(by, index));
	}

	public void typeCtrlC(WebElement element) {
		typeKeys(element, Keys.CONTROL + "c");
	}

	public void typeCtrlV(By by) {
		typeCtrlV(findElement(by));
	}

	public void typeCtrlV(By by, int index) {
		typeCtrlV(findElement(by, index));
	}

	public void typeCtrlV(WebElement element) {
		typeKeys(element, Keys.CONTROL + "v");
	}

	public void typeCtrlA(By by) {
		typeCtrlA(findElement(by));
	}

	public void typeCtrlA(By by, int index) {
		typeCtrlA(findElement(by, index));
	}

	public void typeCtrlA(WebElement element) {
		typeKeys(element, Keys.CONTROL + "a");
	}

	public void typeEnter(By by) {
		typeEnter(findElement(by));
	}

	public void typeEnter(By by, int index) {
		typeEnter(findElement(by, index));
	}

	public void typeEnter(WebElement element) {
		typeKeys(element, Keys.ENTER);
	}

	public void typeReturn(By by) {
		typeReturn(findElement(by));
	}

	public void typeReturn(By by, int index) {
		typeReturn(findElement(by, index));
	}

	public void typeReturn(WebElement element) {
		typeKeys(element, Keys.RETURN);
	}

	public void typeEsc(By by) {
		typeEsc(findElement(by));
	}

	public void typeEsc(By by, int index) {
		typeEsc(findElement(by, index));
	}

	public void typeEsc(WebElement element) {
		typeKeys(element, Keys.ESCAPE);
	}

	public void typeKeys(By by, CharSequence... keys) {
		typeKeys(findElement(by), keys);
	}

	public void typeKeys(By by, int index, CharSequence... keys) {
		typeKeys(findElement(by, index), keys);
	}

	public void typeKeys(WebElement element, CharSequence... keys) {
		actStaleElement(new Action() {
			@Override
			public void act() {
				sendKeysInternal(element, keys);
			}
		});
	}

	private void sendKeysInternal(WebElement element, CharSequence... keys) {
		element.sendKeys(keys);
	}

	public void click(By by) {
		click(findElement(by));
	}

	public void click(By by, int index) {
		click(findElement(by, index));
	}

	public void click(WebElement element) {
		actStaleElement(new Action() {
			@Override
			public void act() {
				clickInternal(element);
			}
		});
	}

	private void clickInternal(WebElement element) {
		element.click();
	}

	public void check(By by) {
		check(findElement(by));
	}

	public void check(By by, int index) {
		check(findElement(by, index));
	}

	public void check(WebElement element) {
		if (!element.isSelected()) {
			click(element);
		}
	}

	public void uncheck(By by) {
		uncheck(findElement(by));
	}

	public void uncheck(By by, int index) {
		uncheck(findElement(by, index));
	}

	public void uncheck(WebElement element) {
		if (element.isSelected()) {
			click(element);
		}
	}

	public boolean isChecked(By by) {
		return isChecked(findElement(by));
	}

	public boolean isChecked(By by, int index) {
		return isChecked(findElement(by, index));
	}

	public boolean isChecked(WebElement element) {
		return element.isSelected();
	}

	public Select getSelect(By by) {
		return getSelect(findElement(by));
	}

	public Select getSelect(By by, int index) {
		return getSelect(findElement(by, index));
	}

	public Select getSelect(WebElement element) {
		return new Select(element);
	}

	public String getText(By by) {
		return getText(findElement(by));
	}

	public String getText(By by, int index) {
		return getText(findElement(by, index));
	}

	public String getText(WebElement element) {
		return element.getText();
	}

	public String getValue(By by) {
		return getValue(findElement(by));
	}

	public String getValue(By by, int index) {
		return getValue(findElement(by, index));
	}

	public String getValue(WebElement element) {
		return element.getAttribute("value");
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

	public WebElement waitVisibleIndex(By by, int index) {
		return waitVisibleIndex(by, index, defaultTimeout);
	}

	public WebElement waitVisibleIndex(By by, int index, int timeout) {
		return waitVisible(ByIndex.index(by, index), timeout);
	}

	public void waitTextPresent(By by, String text) {
		waitTextPresent(by, text, defaultTimeout);
	}

	public void waitTextPresent(By by, String text, int timeout) {
		waitUntil(ExpectedConditions.textToBePresentInElementLocated(by, text), timeout);
	}

	public void waitTextPresentIndex(By by, int index, String text) {
		waitTextPresentIndex(by, index, text, defaultTimeout);
	}

	public void waitTextPresentIndex(By by, int index, String text, int timeout) {
		waitTextPresent(ByIndex.index(by, index), text, timeout);
	}

	public void waitTextNotBlank(By by) {
		waitTextNotBlank(by, defaultTimeout);
	}

	public void waitTextNotBlank(By by, int timeout) {
		waitUntil(textToBePresentInElementLocatedNotBlank(by), timeout);
	}

	public void waitTextNotBlankIndex(By by, int index) {
		waitTextNotBlankIndex(by, index, defaultTimeout);
	}

	public void waitTextNotBlankIndex(By by, int index, int timeout) {
		waitTextNotBlank(ByIndex.index(by, index), timeout);
	}

	public void waitValuePresent(By by, String value) {
		waitValuePresent(by, value, defaultTimeout);
	}

	public void waitValuePresent(By by, String value, int timeout) {
		waitUntil(ExpectedConditions.textToBePresentInElementValue(by, value), timeout);
	}

	public void waitValuePresentIndex(By by, int index, String value) {
		waitValuePresentIndex(by, index, value, defaultTimeout);
	}

	public void waitValuePresentIndex(By by, int index, String value, int timeout) {
		waitValuePresent(ByIndex.index(by, index), value, timeout);
	}

	public void waitValueNotBlank(By by) {
		waitValueNotBlank(by, defaultTimeout);
	}

	public void waitValueNotBlank(By by, int timeout) {
		waitUntil(textToBePresentInElementValueNotBlank(by), timeout);
	}

	public void waitValueNotBlankIndex(By by, int index) {
		waitValueNotBlankIndex(by, index, defaultTimeout);
	}

	public void waitValueNotBlankIndex(By by, int index, int timeout) {
		waitValueNotBlank(ByIndex.index(by, index), timeout);
	}

	public Alert getAlertNoException() {
		return getAlertNoException(defaultTimeout);
	}

	public Alert getAlertNoException(int timeout) {
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
		return getCell(findElement(by), rowIndex, columnIndex);
	}

	public String getCell(By by, int index, int rowIndex, int columnIndex) {
		return getCell(findElement(by, index), rowIndex, columnIndex);
	}

	public String getCell(WebElement element, int rowIndex, int columnIndex) {
		return element.findElement(By.xpath("//tr[" + (rowIndex + 1) + "]//td[" + (columnIndex + 1) + "]")).getText();
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
		final String mainWindow = driver.getWindowHandle();

		try {
			for (String newWindow : driver.getWindowHandles()) {
				if (newWindow.equals(mainWindow)) {
					continue;
				}

				driver.switchTo().window(newWindow);

				try {
					action.actInNewWindow();
				} finally {
					driver.close();
				}
			}
		} finally {
			driver.switchTo().window(mainWindow);
		}
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

	private ExpectedCondition<Boolean> textToBePresentInElementLocatedNotBlank(final By locator) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					String elementText = findElement(locator).getText();
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

	private ExpectedCondition<Boolean> textToBePresentInElementValueNotBlank(final By locator) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					String elementText = findElement(locator).getAttribute("value");
					return StringUtils.isNotBlank(elementText);
				} catch (StaleElementReferenceException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return String.format("text to be the value of element found by %s", locator);
			}
		};
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
