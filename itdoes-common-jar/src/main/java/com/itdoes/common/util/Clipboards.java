package com.itdoes.common.util;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author Jalen Zhong
 */
public class Clipboards {
	public static void copy(String content) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(content), null);
	}

	public static String paste() {
		try {
			return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	private Clipboards() {
	}
}
