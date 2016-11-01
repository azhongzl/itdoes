package com.itdoes.common.core.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * @author Jalen Zhong
 */
public class ByIndex extends By {
	public static By index(By by, int index) {
		return new ByIndex(by, index);
	}

	private final By by;
	private final int index;

	private ByIndex(By by, int index) {
		this.by = by;
		this.index = index;
	}

	@Override
	public List<WebElement> findElements(SearchContext context) {
		return by.findElements(context);
	}

	@Override
	public WebElement findElement(SearchContext context) {
		List<WebElement> allElements = findElements(context);
		if (allElements == null || allElements.isEmpty())
			throw new NoSuchElementException("Cannot locate an element using " + toString());
		return allElements.get(index);
	}

	@Override
	public String toString() {
		return by + ", By.index: " + index;
	}
}
