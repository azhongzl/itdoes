package com.itdoes.common.core.util;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jalen Zhong
 */
public class Clipboards {
	private static final Logger LOGGER = LoggerFactory.getLogger(Clipboards.class);

	public static void copy(String content) {
		final StringSelection stringSelection = new StringSelection(content);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
	}

	public static void copy(String content, int retryTimes) {
		int retryNo = 0;
		while (true) {
			try {
				copy(content);
				break;
			} catch (IllegalStateException e) {
				retryNo++;
				if (retryNo > retryTimes) {
					throw e;
				} else {
					LOGGER.warn("Copy to clipboard fail, retry times is: {}/{}", retryNo, retryTimes, e);
				}
			}
		}
	}

	public static String paste() {
		try {
			return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static String paste(int retryTimes) {
		int retryNo = 0;
		while (true) {
			try {
				return paste();
			} catch (IllegalStateException e) {
				retryNo++;
				if (retryNo > retryTimes) {
					throw e;
				} else {
					LOGGER.warn("Paste from clipboard fail, retry times is: {}/{}", retryNo, retryTimes, e);
				}
			}
		}
	}

	private Clipboards() {
	}
}
